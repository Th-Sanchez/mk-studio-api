package com.mkstudio.student.dto;

import com.mkstudio.student.Gender;
import com.mkstudio.student.PrimaryGoal;
import com.mkstudio.student.StudentStatus;
import com.mkstudio.student.model.MedicalHistory;
import com.mkstudio.student.model.PhysicalActivityHistory;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record StudentDetailResponse(
        UUID id,
        String fullName,
        LocalDate birthDate,
        int age,
        Gender gender,
        String phone,
        String email,
        AddressResponse address,
        EmergencyContactResponse emergencyContact,
        StudentStatus status,
        Instant createdAt,
        Instant updatedAt,
        AnamnesisDetailResponse latestAnamnesis
) {
    public record AddressResponse(
            String zipCode,
            String street,
            String number,
            String complement,
            String neighborhood,
            String city,
            String state
    ) {
    }

    public record EmergencyContactResponse(
            String fullName,
            String relationship,
            String phone
    ) {
    }

    public record AnamnesisDetailResponse(
            UUID id,
            MedicalHistory medicalHistory,
            PhysicalActivityHistory physicalActivityHistory,
            PrimaryGoal primaryGoal,
            String primaryGoalOther,
            String secondaryGoal,
            String focusBodyArea,
            String weeklyAvailability,
            int intendedWeeklyFrequency,
            AssessmentResponse physicalAssessment,
            boolean hasHealthAlerts,
            int healthAlertsCount,
            List<String> healthAlerts,
            ConsentResponse consent,
            Instant createdAt,
            Instant updatedAt
    ) {
    }

    public record AssessmentResponse(
            BigDecimal weightKg,
            BigDecimal heightCm,
            BigDecimal bodyFatPercentage,
            BigDecimal muscleMassKg,
            String bloodPressure,
            Integer restingHeartRate,
            String professionalNotes
    ) {
    }

    public record ConsentResponse(
            String termsVersion,
            boolean termsAccepted,
            boolean lgpdConsentAccepted,
            String signerName,
            String signatureImage,
            Instant signedAt
    ) {
    }
}
