package com.mkstudio.student.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PhysicalActivityHistory(
        boolean practicedWeightTraining,

        @Size(max = 120, message = "O tempo de prática de musculação deve ter no máximo 120 caracteres.")
        String weightTrainingDuration,

        boolean currentlyActive,

        @Size(max = 150, message = "A atividade atual deve ter no máximo 150 caracteres.")
        String currentActivity,

        @Min(value = 1, message = "A frequência semanal deve ser entre 1 e 7.")
        @Max(value = 7, message = "A frequência semanal deve ser entre 1 e 7.")
        Integer currentWeeklyFrequency,

        @Size(max = 120, message = "O tempo parado deve ter no máximo 120 caracteres.")
        String timeInactive,

        @NotNull(message = "Informe se já teve alguma experiência negativa durante exercícios.")
        @Valid
        YesNoDetail negativeExperienceDuringExercise
) {
}
