package com.mkstudio.student.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record UpdateAssessmentRequest(
        @DecimalMin(value = "20.0", message = "Peso inválido.")
        @DecimalMax(value = "350.0", message = "Peso inválido.")
        BigDecimal weightKg,

        @DecimalMin(value = "100.0", message = "Altura inválida (informe em cm).")
        @DecimalMax(value = "250.0", message = "Altura inválida (informe em cm).")
        BigDecimal heightCm,

        @DecimalMin(value = "1.0", message = "Percentual de gordura inválido.")
        @DecimalMax(value = "75.0", message = "Percentual de gordura inválido.")
        BigDecimal bodyFatPercentage,

        @DecimalMin(value = "10.0", message = "Massa muscular inválida.")
        @DecimalMax(value = "150.0", message = "Massa muscular inválida.")
        BigDecimal muscleMassKg,

        @Size(max = 20, message = "Pressão arterial inválida.")
        String bloodPressure,

        @Min(value = 30, message = "Frequência cardíaca inválida.")
        @Max(value = 220, message = "Frequência cardíaca inválida.")
        Integer restingHeartRate,

        @Size(max = 5000, message = "As observações do profissional devem ter no máximo 5000 caracteres.")
        String professionalNotes
) {
}
