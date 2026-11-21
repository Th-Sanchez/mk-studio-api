package com.mkstudio.student.model;

import com.mkstudio.student.SleepQuality;
import com.mkstudio.student.StressLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record MedicalHistory(
        @NotNull(message = "Informe as condições atuais de saúde.")
        @Valid
        CurrentConditions currentConditions,

        @NotNull(message = "Informe o histórico familiar.")
        @Valid
        FamilyHistory familyHistory,

        @NotNull(message = "Informe os hábitos de vida.")
        @Valid
        LifestyleHabits lifestyleHabits,

        @NotNull(message = "Informe o histórico de lesões.")
        @Valid
        InjuryHistory injuryHistory,

        @NotNull(message = "Informe os dados de medicamentos e acompanhamento.")
        @Valid
        MedicationsAndCare medicationsAndCare
) {
    public record CurrentConditions(
            @NotNull @Valid YesNoDetail diagnosedDisease,
            @NotNull @Valid YesNoDetail heartProblem,
            @NotNull @Valid YesNoDetail bloodPressureIssue,
            @NotNull @Valid YesNoDetail diabetes,
            @NotNull @Valid YesNoDetail respiratoryProblem,
            @NotNull @Valid YesNoDetail jointProblem,
            @NotNull @Valid YesNoDetail spineProblem,
            @NotNull @Valid YesNoDetail frequentPain,
            @NotNull @Valid YesNoDetail dizzinessOrFainting,
            @NotNull @Valid YesNoDetail shortnessOfBreathDuringExercise,
            @NotNull @Valid YesNoDetail otherHealthCondition
    ) {
    }

    public record FamilyHistory(
            boolean heartDisease,
            boolean diabetes,
            boolean hypertension,
            @NotNull @Valid YesNoDetail hereditaryDiseases,
            @Size(max = 500, message = "O histórico familiar adicional deve ter no máximo 500 caracteres.")
            String otherRelevantHistory
    ) {
    }

    public record LifestyleHabits(
            boolean smokes,
            boolean usedToSmoke,
            boolean frequentAlcoholConsumption,
            @NotNull(message = "Informe a qualidade do sono.")
            SleepQuality sleepQuality,
            @NotNull(message = "Informe quantas horas dorme por noite.")
            @DecimalMin(value = "0.0", message = "Horas de sono inválidas.")
            @DecimalMax(value = "24.0", message = "Horas de sono inválidas.")
            BigDecimal sleepHoursPerNight,
            @NotNull(message = "Informe o nível de estresse.")
            StressLevel stressLevel
    ) {
    }

    public record InjuryHistory(
            @NotNull @Valid YesNoDetail fracture,
            @NotNull @Valid YesNoDetail dislocation,
            @NotNull @Valid YesNoDetail muscleInjury,
            @NotNull @Valid YesNoDetail ligamentInjury,
            @NotNull @Valid YesNoDetail movementLimitation,
            @NotNull @Valid YesNoDetail painDuringSpecificMovements,
            @NotNull @Valid YesNoDetail surgeryOrMedicalProcedure,
            @NotNull @Valid YesNoDetail stoppedExerciseForHealthReason,
            @NotNull @Valid YesNoDetail medicalRestrictionForExercise
    ) {
    }

    public record MedicationsAndCare(
            boolean usesMedication,
            @Size(max = 500, message = "A lista de medicamentos deve ter no máximo 500 caracteres.")
            String medicationsList,
            boolean regularMedicalFollowUp,
            boolean physiotherapyFollowUp,
            @NotNull @Valid YesNoDetail medicalRecommendationOrRestriction,
            boolean hasRecentHealthExams
    ) {
    }
}
