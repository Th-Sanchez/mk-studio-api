package com.mkstudio.student;

import com.mkstudio.student.model.MedicalHistory;
import com.mkstudio.student.model.PhysicalActivityHistory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "student_anamneses")
public class StudentAnamnesis {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "medical_history", nullable = false, columnDefinition = "jsonb")
    private MedicalHistory medicalHistory;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "physical_activity_history", nullable = false, columnDefinition = "jsonb")
    private PhysicalActivityHistory physicalActivityHistory;

    @Enumerated(EnumType.STRING)
    @Column(name = "primary_goal", nullable = false, length = 40)
    private PrimaryGoal primaryGoal;

    @Column(name = "primary_goal_other", length = 150)
    private String primaryGoalOther;

    @Column(name = "secondary_goal", length = 150)
    private String secondaryGoal;

    @Column(name = "focus_body_area", length = 150)
    private String focusBodyArea;

    @Column(name = "weekly_availability", nullable = false, length = 255)
    private String weeklyAvailability;

    @Column(name = "intended_weekly_frequency", nullable = false)
    private Short intendedWeeklyFrequency;

    @Column(name = "weight_kg", precision = 5, scale = 2)
    private BigDecimal weightKg;

    @Column(name = "height_cm", precision = 5, scale = 2)
    private BigDecimal heightCm;

    @Column(name = "body_fat_percentage", precision = 5, scale = 2)
    private BigDecimal bodyFatPercentage;

    @Column(name = "muscle_mass_kg", precision = 5, scale = 2)
    private BigDecimal muscleMassKg;

    @Column(name = "blood_pressure", length = 20)
    private String bloodPressure;

    @Column(name = "resting_heart_rate")
    private Short restingHeartRate;

    @Column(name = "professional_notes", columnDefinition = "TEXT")
    private String professionalNotes;

    @Column(name = "has_health_alerts", nullable = false)
    private boolean hasHealthAlerts;

    @Column(name = "health_alerts_count", nullable = false)
    private Short healthAlertsCount = 0;

    @Column(name = "terms_version", nullable = false, length = 20)
    private String termsVersion;

    @Column(name = "terms_accepted", nullable = false)
    private boolean termsAccepted;

    @Column(name = "lgpd_consent_accepted", nullable = false)
    private boolean lgpdConsentAccepted;

    @Column(name = "signer_name", nullable = false, length = 150)
    private String signerName;

    @Column(name = "signature_image", nullable = false, columnDefinition = "TEXT")
    private String signatureImage;

    @Column(name = "signed_at", nullable = false)
    private Instant signedAt;

    @Column(name = "signer_ip", length = 64)
    private String signerIp;

    @Column(name = "signer_user_agent", length = 300)
    private String signerUserAgent;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public MedicalHistory getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(MedicalHistory medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public PhysicalActivityHistory getPhysicalActivityHistory() {
        return physicalActivityHistory;
    }

    public void setPhysicalActivityHistory(PhysicalActivityHistory physicalActivityHistory) {
        this.physicalActivityHistory = physicalActivityHistory;
    }

    public PrimaryGoal getPrimaryGoal() {
        return primaryGoal;
    }

    public void setPrimaryGoal(PrimaryGoal primaryGoal) {
        this.primaryGoal = primaryGoal;
    }

    public String getPrimaryGoalOther() {
        return primaryGoalOther;
    }

    public void setPrimaryGoalOther(String primaryGoalOther) {
        this.primaryGoalOther = primaryGoalOther;
    }

    public String getSecondaryGoal() {
        return secondaryGoal;
    }

    public void setSecondaryGoal(String secondaryGoal) {
        this.secondaryGoal = secondaryGoal;
    }

    public String getFocusBodyArea() {
        return focusBodyArea;
    }

    public void setFocusBodyArea(String focusBodyArea) {
        this.focusBodyArea = focusBodyArea;
    }

    public String getWeeklyAvailability() {
        return weeklyAvailability;
    }

    public void setWeeklyAvailability(String weeklyAvailability) {
        this.weeklyAvailability = weeklyAvailability;
    }

    public Short getIntendedWeeklyFrequency() {
        return intendedWeeklyFrequency;
    }

    public void setIntendedWeeklyFrequency(Short intendedWeeklyFrequency) {
        this.intendedWeeklyFrequency = intendedWeeklyFrequency;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    public BigDecimal getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(BigDecimal heightCm) {
        this.heightCm = heightCm;
    }

    public BigDecimal getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(BigDecimal bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public BigDecimal getMuscleMassKg() {
        return muscleMassKg;
    }

    public void setMuscleMassKg(BigDecimal muscleMassKg) {
        this.muscleMassKg = muscleMassKg;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public Short getRestingHeartRate() {
        return restingHeartRate;
    }

    public void setRestingHeartRate(Short restingHeartRate) {
        this.restingHeartRate = restingHeartRate;
    }

    public String getProfessionalNotes() {
        return professionalNotes;
    }

    public void setProfessionalNotes(String professionalNotes) {
        this.professionalNotes = professionalNotes;
    }

    public boolean isHasHealthAlerts() {
        return hasHealthAlerts;
    }

    public void setHasHealthAlerts(boolean hasHealthAlerts) {
        this.hasHealthAlerts = hasHealthAlerts;
    }

    public Short getHealthAlertsCount() {
        return healthAlertsCount;
    }

    public void setHealthAlertsCount(Short healthAlertsCount) {
        this.healthAlertsCount = healthAlertsCount;
    }

    public String getTermsVersion() {
        return termsVersion;
    }

    public void setTermsVersion(String termsVersion) {
        this.termsVersion = termsVersion;
    }

    public boolean isTermsAccepted() {
        return termsAccepted;
    }

    public void setTermsAccepted(boolean termsAccepted) {
        this.termsAccepted = termsAccepted;
    }

    public boolean isLgpdConsentAccepted() {
        return lgpdConsentAccepted;
    }

    public void setLgpdConsentAccepted(boolean lgpdConsentAccepted) {
        this.lgpdConsentAccepted = lgpdConsentAccepted;
    }

    public String getSignerName() {
        return signerName;
    }

    public void setSignerName(String signerName) {
        this.signerName = signerName;
    }

    public String getSignatureImage() {
        return signatureImage;
    }

    public void setSignatureImage(String signatureImage) {
        this.signatureImage = signatureImage;
    }

    public Instant getSignedAt() {
        return signedAt;
    }

    public void setSignedAt(Instant signedAt) {
        this.signedAt = signedAt;
    }

    public String getSignerIp() {
        return signerIp;
    }

    public void setSignerIp(String signerIp) {
        this.signerIp = signerIp;
    }

    public String getSignerUserAgent() {
        return signerUserAgent;
    }

    public void setSignerUserAgent(String signerUserAgent) {
        this.signerUserAgent = signerUserAgent;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
