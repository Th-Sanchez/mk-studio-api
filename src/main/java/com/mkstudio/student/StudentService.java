package com.mkstudio.student;

import com.mkstudio.common.ConflictException;
import com.mkstudio.common.NormalizationUtils;
import com.mkstudio.common.ResourceNotFoundException;
import com.mkstudio.student.dto.PagedResponse;
import com.mkstudio.student.dto.PublicRegistrationRequest;
import com.mkstudio.student.dto.PublicRegistrationResponse;
import com.mkstudio.student.dto.StudentDetailResponse;
import com.mkstudio.student.dto.StudentListItemResponse;
import com.mkstudio.student.dto.StudentSummaryCountsResponse;
import com.mkstudio.student.dto.UpdateAssessmentRequest;
import com.mkstudio.student.dto.UpdateStudentRequest;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private static final int MAX_SIGNATURE_LENGTH = 500_000;

    private final StudentRepository studentRepository;
    private final StudentAnamnesisRepository anamnesisRepository;

    public StudentService(
            StudentRepository studentRepository,
            StudentAnamnesisRepository anamnesisRepository
    ) {
        this.studentRepository = studentRepository;
        this.anamnesisRepository = anamnesisRepository;
    }

    @Transactional
    public PublicRegistrationResponse register(
            PublicRegistrationRequest request,
            String clientIp,
            String userAgent
    ) {
        PublicRegistrationRequest.PersonalData personal = request.personalData();
        PublicRegistrationRequest.AddressData address = personal.address();
        PublicRegistrationRequest.EmergencyContact emergency = request.emergencyContact();
        PublicRegistrationRequest.GoalsAndExpectations goals = request.goalsAndExpectations();
        PublicRegistrationRequest.PhysicalAssessment assessment = request.physicalAssessment();
        PublicRegistrationRequest.ConsentData consent = request.consent();

        String normalizedPhone = validateAndNormalizePhone(personal.phone(), "WhatsApp do aluno");
        String normalizedEmergencyPhone = validateAndNormalizePhone(emergency.phone(), "Telefone de emergência");
        String normalizedZipCode = validateAndNormalizeZipCode(address.zipCode());
        String normalizedState = validateAndNormalizeState(address.state());

        if (goals.primaryGoal() == PrimaryGoal.OTHER
                && NormalizationUtils.trimToNull(goals.primaryGoalOther()) == null) {
            throw new IllegalArgumentException("Descreva seu objetivo principal ao selecionar 'Outros'.");
        }

        validateSignatureImage(consent.signatureImage());

        if (studentRepository.existsByPhone(normalizedPhone)) {
            throw new ConflictException(
                    "Já existe um cadastro com este WhatsApp. Caso precise atualizar seus dados, avise o personal."
            );
        }

        Student student = new Student();
        student.setFullName(personal.fullName().trim());
        student.setBirthDate(personal.birthDate());
        student.setGender(personal.gender());
        student.setPhone(normalizedPhone);
        student.setEmail(NormalizationUtils.normalizeEmail(personal.email()));
        student.setAddressZipCode(normalizedZipCode);
        student.setAddressStreet(address.street().trim());
        student.setAddressNumber(address.number().trim());
        student.setAddressComplement(NormalizationUtils.trimToNull(address.complement()));
        student.setAddressNeighborhood(address.neighborhood().trim());
        student.setAddressCity(address.city().trim());
        student.setAddressState(normalizedState);
        student.setEmergencyContactName(emergency.fullName().trim());
        student.setEmergencyContactRelationship(emergency.relationship().trim());
        student.setEmergencyContactPhone(normalizedEmergencyPhone);
        student.setStatus(StudentStatus.NEW);

        Student savedStudent = studentRepository.save(student);

        List<String> alerts = HealthAlertExtractor.extractAlerts(request.medicalHistory());
        Instant signedAt = Instant.now();

        StudentAnamnesis anamnesis = new StudentAnamnesis();
        anamnesis.setStudent(savedStudent);
        anamnesis.setMedicalHistory(request.medicalHistory());
        anamnesis.setPhysicalActivityHistory(request.physicalActivityHistory());
        anamnesis.setPrimaryGoal(goals.primaryGoal());
        anamnesis.setPrimaryGoalOther(NormalizationUtils.trimToNull(goals.primaryGoalOther()));
        anamnesis.setSecondaryGoal(NormalizationUtils.trimToNull(goals.secondaryGoal()));
        anamnesis.setFocusBodyArea(NormalizationUtils.trimToNull(goals.focusBodyArea()));
        anamnesis.setWeeklyAvailability(goals.weeklyAvailability().trim());
        anamnesis.setIntendedWeeklyFrequency(goals.intendedWeeklyFrequency().shortValue());

        if (assessment != null) {
            anamnesis.setWeightKg(assessment.weightKg());
            anamnesis.setHeightCm(assessment.heightCm());
            anamnesis.setBodyFatPercentage(assessment.bodyFatPercentage());
            anamnesis.setMuscleMassKg(assessment.muscleMassKg());
            anamnesis.setBloodPressure(NormalizationUtils.trimToNull(assessment.bloodPressure()));
            anamnesis.setRestingHeartRate(
                    assessment.restingHeartRate() == null ? null : assessment.restingHeartRate().shortValue()
            );
        }

        anamnesis.setHasHealthAlerts(!alerts.isEmpty());
        anamnesis.setHealthAlertsCount((short) alerts.size());
        anamnesis.setTermsVersion(consent.termsVersion().trim());
        anamnesis.setTermsAccepted(consent.termsAccepted());
        anamnesis.setLgpdConsentAccepted(consent.lgpdConsentAccepted());
        anamnesis.setSignerName(consent.signerName().trim());
        anamnesis.setSignatureImage(consent.signatureImage().trim());
        anamnesis.setSignedAt(signedAt);
        anamnesis.setSignerIp(truncate(NormalizationUtils.trimToNull(clientIp), 64));
        anamnesis.setSignerUserAgent(truncate(NormalizationUtils.trimToNull(userAgent), 300));

        StudentAnamnesis savedAnamnesis = anamnesisRepository.save(anamnesis);

        return new PublicRegistrationResponse(
                savedStudent.getId(),
                savedAnamnesis.getId(),
                savedStudent.getFullName(),
                savedStudent.getStatus(),
                savedAnamnesis.getSignedAt()
        );
    }

    @Transactional(readOnly = true)
    public StudentSummaryCountsResponse getSummary() {
        long total = studentRepository.count();
        long newCount = studentRepository.countByStatus(StudentStatus.NEW);
        long activeCount = studentRepository.countByStatus(StudentStatus.ACTIVE);
        long inactiveCount = studentRepository.countByStatus(StudentStatus.INACTIVE);
        long withHealthAlertsCount = studentRepository.countWithHealthAlerts();

        return new StudentSummaryCountsResponse(
                total,
                newCount,
                activeCount,
                inactiveCount,
                withHealthAlertsCount
        );
    }

    @Transactional(readOnly = true)
    public PagedResponse<StudentListItemResponse> list(
            String search,
            StudentStatus status,
            boolean onlyHealthAlerts,
            int page,
            int size
    ) {
        int safePage = Math.max(0, page);
        int safeSize = Math.min(100, Math.max(1, size));
        String searchPattern = buildSearchPattern(search);

        PageRequest pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Student> studentPage = studentRepository.searchStudents(status, searchPattern, onlyHealthAlerts, pageable);

        List<UUID> studentIds = studentPage.getContent().stream().map(Student::getId).toList();
        Map<UUID, StudentAnamnesis> latestByStudent = studentIds.isEmpty()
                ? Map.of()
                : anamnesisRepository.findLatestByStudentIds(studentIds).stream()
                        .collect(Collectors.toMap(
                                a -> a.getStudent().getId(),
                                Function.identity(),
                                (first, second) -> first
                        ));

        Page<StudentListItemResponse> mapped = studentPage.map(student -> {
            StudentAnamnesis anamnesis = latestByStudent.get(student.getId());
            return new StudentListItemResponse(
                    student.getId(),
                    student.getFullName(),
                    student.calculateAge(),
                    student.getPhone(),
                    anamnesis == null ? null : anamnesis.getPrimaryGoal(),
                    anamnesis == null ? null : anamnesis.getPrimaryGoalOther(),
                    anamnesis == null ? null : anamnesis.getWeeklyAvailability(),
                    anamnesis == null || anamnesis.getIntendedWeeklyFrequency() == null
                            ? null
                            : anamnesis.getIntendedWeeklyFrequency().intValue(),
                    anamnesis != null && anamnesis.isHasHealthAlerts(),
                    anamnesis == null || anamnesis.getHealthAlertsCount() == null
                            ? 0
                            : anamnesis.getHealthAlertsCount().intValue(),
                    student.getStatus(),
                    student.getCreatedAt()
            );
        });

        return PagedResponse.from(mapped);
    }

    @Transactional(readOnly = true)
    public StudentDetailResponse getDetail(UUID studentId) {
        Student student = findStudentOrThrow(studentId);
        StudentAnamnesis anamnesis = anamnesisRepository
                .findFirstByStudentIdOrderByCreatedAtDesc(studentId)
                .orElse(null);

        return toDetailResponse(student, anamnesis);
    }

    @Transactional
    public StudentDetailResponse updateStudent(UUID studentId, UpdateStudentRequest request) {
        Student student = findStudentOrThrow(studentId);

        String normalizedPhone = validateAndNormalizePhone(request.phone(), "WhatsApp do aluno");
        String normalizedEmergencyPhone = validateAndNormalizePhone(
                request.emergencyContact().phone(),
                "Telefone de emergência"
        );
        String normalizedZipCode = validateAndNormalizeZipCode(request.address().zipCode());
        String normalizedState = validateAndNormalizeState(request.address().state());

        if (studentRepository.existsByPhoneAndIdNot(normalizedPhone, studentId)) {
            throw new ConflictException("Já existe outro aluno cadastrado com este número de WhatsApp.");
        }

        student.setFullName(request.fullName().trim());
        student.setBirthDate(request.birthDate());
        student.setGender(request.gender());
        student.setPhone(normalizedPhone);
        student.setEmail(NormalizationUtils.normalizeEmail(request.email()));
        student.setAddressZipCode(normalizedZipCode);
        student.setAddressStreet(request.address().street().trim());
        student.setAddressNumber(request.address().number().trim());
        student.setAddressComplement(NormalizationUtils.trimToNull(request.address().complement()));
        student.setAddressNeighborhood(request.address().neighborhood().trim());
        student.setAddressCity(request.address().city().trim());
        student.setAddressState(normalizedState);
        student.setEmergencyContactName(request.emergencyContact().fullName().trim());
        student.setEmergencyContactRelationship(request.emergencyContact().relationship().trim());
        student.setEmergencyContactPhone(normalizedEmergencyPhone);
        student.setStatus(request.status());

        studentRepository.save(student);
        return getDetail(studentId);
    }

    @Transactional
    public StudentDetailResponse updateAssessment(
            UUID studentId,
            UUID anamnesisId,
            UpdateAssessmentRequest request
    ) {
        Student student = findStudentOrThrow(studentId);
        StudentAnamnesis anamnesis = anamnesisRepository.findByIdAndStudentId(anamnesisId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Anamnese não encontrada para este aluno."));

        anamnesis.setWeightKg(request.weightKg());
        anamnesis.setHeightCm(request.heightCm());
        anamnesis.setBodyFatPercentage(request.bodyFatPercentage());
        anamnesis.setMuscleMassKg(request.muscleMassKg());
        anamnesis.setBloodPressure(NormalizationUtils.trimToNull(request.bloodPressure()));
        anamnesis.setRestingHeartRate(
                request.restingHeartRate() == null ? null : request.restingHeartRate().shortValue()
        );
        anamnesis.setProfessionalNotes(NormalizationUtils.trimToNull(request.professionalNotes()));

        StudentAnamnesis saved = anamnesisRepository.save(anamnesis);
        return toDetailResponse(student, saved);
    }

    @Transactional
    public StudentDetailResponse updateStatus(UUID studentId, StudentStatus status) {
        Student student = findStudentOrThrow(studentId);
        student.setStatus(status);
        studentRepository.save(student);
        return getDetail(studentId);
    }

    @Transactional
    public void deleteStudent(UUID studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Aluno não encontrado.");
        }
        studentRepository.deleteById(studentId);
    }

    private Student findStudentOrThrow(UUID studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado."));
    }

    private StudentDetailResponse toDetailResponse(Student student, StudentAnamnesis anamnesis) {
        StudentDetailResponse.AnamnesisDetailResponse anamnesisDto = null;
        if (anamnesis != null) {
            List<String> alerts = HealthAlertExtractor.extractAlerts(anamnesis.getMedicalHistory());
            anamnesisDto = new StudentDetailResponse.AnamnesisDetailResponse(
                    anamnesis.getId(),
                    anamnesis.getMedicalHistory(),
                    anamnesis.getPhysicalActivityHistory(),
                    anamnesis.getPrimaryGoal(),
                    anamnesis.getPrimaryGoalOther(),
                    anamnesis.getSecondaryGoal(),
                    anamnesis.getFocusBodyArea(),
                    anamnesis.getWeeklyAvailability(),
                    anamnesis.getIntendedWeeklyFrequency() == null ? 0 : anamnesis.getIntendedWeeklyFrequency().intValue(),
                    new StudentDetailResponse.AssessmentResponse(
                            anamnesis.getWeightKg(),
                            anamnesis.getHeightCm(),
                            anamnesis.getBodyFatPercentage(),
                            anamnesis.getMuscleMassKg(),
                            anamnesis.getBloodPressure(),
                            anamnesis.getRestingHeartRate() == null ? null : anamnesis.getRestingHeartRate().intValue(),
                            anamnesis.getProfessionalNotes()
                    ),
                    anamnesis.isHasHealthAlerts(),
                    anamnesis.getHealthAlertsCount() == null ? alerts.size() : anamnesis.getHealthAlertsCount().intValue(),
                    alerts,
                    new StudentDetailResponse.ConsentResponse(
                            anamnesis.getTermsVersion(),
                            anamnesis.isTermsAccepted(),
                            anamnesis.isLgpdConsentAccepted(),
                            anamnesis.getSignerName(),
                            anamnesis.getSignatureImage(),
                            anamnesis.getSignedAt()
                    ),
                    anamnesis.getCreatedAt(),
                    anamnesis.getUpdatedAt()
            );
        }

        return new StudentDetailResponse(
                student.getId(),
                student.getFullName(),
                student.getBirthDate(),
                student.calculateAge(),
                student.getGender(),
                student.getPhone(),
                student.getEmail(),
                new StudentDetailResponse.AddressResponse(
                        student.getAddressZipCode(),
                        student.getAddressStreet(),
                        student.getAddressNumber(),
                        student.getAddressComplement(),
                        student.getAddressNeighborhood(),
                        student.getAddressCity(),
                        student.getAddressState()
                ),
                new StudentDetailResponse.EmergencyContactResponse(
                        student.getEmergencyContactName(),
                        student.getEmergencyContactRelationship(),
                        student.getEmergencyContactPhone()
                ),
                student.getStatus(),
                student.getCreatedAt(),
                student.getUpdatedAt(),
                anamnesisDto
        );
    }

    private String validateAndNormalizePhone(String rawPhone, String fieldLabel) {
        String digits = NormalizationUtils.digitsOnly(rawPhone);
        if (digits == null || (digits.length() != 10 && digits.length() != 11)) {
            throw new IllegalArgumentException(fieldLabel + " deve conter DDD + número (10 ou 11 dígitos).");
        }
        return digits;
    }

    private String validateAndNormalizeZipCode(String rawZipCode) {
        String digits = NormalizationUtils.digitsOnly(rawZipCode);
        if (digits == null || digits.length() != 8) {
            throw new IllegalArgumentException("CEP inválido. Informe 8 dígitos.");
        }
        return digits;
    }

    private String validateAndNormalizeState(String rawState) {
        String state = NormalizationUtils.normalizeState(rawState);
        if (state == null || !state.matches("^[A-Z]{2}$")) {
            throw new IllegalArgumentException("Estado (UF) inválido. Informe 2 letras (ex.: SP).");
        }
        return state;
    }

    private void validateSignatureImage(String signatureImage) {
        String trimmed = NormalizationUtils.trimToNull(signatureImage);
        if (trimmed == null || !trimmed.startsWith("data:image/")) {
            throw new IllegalArgumentException("Assinatura digital inválida. Assine no campo indicado.");
        }
        if (trimmed.length() > MAX_SIGNATURE_LENGTH) {
            throw new IllegalArgumentException("A imagem da assinatura excede o tamanho máximo permitido.");
        }
    }

    private String buildSearchPattern(String search) {
        String trimmed = NormalizationUtils.trimToNull(search);
        if (trimmed == null) {
            return "%";
        }
        String digits = NormalizationUtils.digitsOnly(trimmed);
        if (digits != null && digits.length() >= 4 && trimmed.matches("^[0-9()\\s+-]+$")) {
            return "%" + digits + "%";
        }
        return "%" + trimmed.toLowerCase(Locale.ROOT) + "%";
    }

    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
