package com.mkstudio.student.dto;

import com.mkstudio.student.Gender;
import com.mkstudio.student.PrimaryGoal;
import com.mkstudio.student.model.MedicalHistory;
import com.mkstudio.student.model.PhysicalActivityHistory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PublicRegistrationRequest(
        @NotNull(message = "Informe os dados pessoais.")
        @Valid
        PersonalData personalData,

        @NotNull(message = "Informe o contato de emergência.")
        @Valid
        EmergencyContact emergencyContact,

        @NotNull(message = "Informe o histórico médico.")
        @Valid
        MedicalHistory medicalHistory,

        @NotNull(message = "Informe o histórico de atividade física.")
        @Valid
        PhysicalActivityHistory physicalActivityHistory,

        @NotNull(message = "Informe os objetivos e expectativas.")
        @Valid
        GoalsAndExpectations goalsAndExpectations,

        @Valid
        PhysicalAssessment physicalAssessment,

        @NotNull(message = "O aceite do termo e assinatura são obrigatórios.")
        @Valid
        ConsentData consent
) {
    public record PersonalData(
            @NotBlank(message = "Informe o nome completo.")
            @Size(min = 3, max = 150, message = "O nome completo deve ter entre 3 e 150 caracteres.")
            String fullName,

            @NotNull(message = "Informe a data de nascimento.")
            @Past(message = "A data de nascimento deve estar no passado.")
            LocalDate birthDate,

            @NotNull(message = "Informe o sexo.")
            Gender gender,

            @NotBlank(message = "Informe o telefone/WhatsApp.")
            @Size(max = 25, message = "Telefone inválido.")
            String phone,

            @Email(message = "Informe um e-mail válido.")
            @Size(max = 160, message = "O e-mail deve ter no máximo 160 caracteres.")
            String email,

            @NotNull(message = "Informe o endereço.")
            @Valid
            AddressData address
    ) {
    }

    public record AddressData(
            @NotBlank(message = "Informe o CEP.")
            @Size(max = 12, message = "CEP inválido.")
            String zipCode,

            @NotBlank(message = "Informe o endereço.")
            @Size(max = 180, message = "O endereço deve ter no máximo 180 caracteres.")
            String street,

            @NotBlank(message = "Informe o número.")
            @Size(max = 20, message = "O número deve ter no máximo 20 caracteres.")
            String number,

            @Size(max = 100, message = "O complemento deve ter no máximo 100 caracteres.")
            String complement,

            @NotBlank(message = "Informe o bairro.")
            @Size(max = 120, message = "O bairro deve ter no máximo 120 caracteres.")
            String neighborhood,

            @NotBlank(message = "Informe a cidade.")
            @Size(max = 120, message = "A cidade deve ter no máximo 120 caracteres.")
            String city,

            @NotBlank(message = "Informe o estado (UF).")
            @Size(min = 2, max = 2, message = "Informe a sigla do estado com 2 letras (ex.: SP).")
            String state
    ) {
    }

    public record EmergencyContact(
            @NotBlank(message = "Informe o nome do contato de emergência.")
            @Size(min = 2, max = 150, message = "O nome do contato deve ter entre 2 e 150 caracteres.")
            String fullName,

            @NotBlank(message = "Informe o grau de parentesco.")
            @Size(max = 60, message = "O grau de parentesco deve ter no máximo 60 caracteres.")
            String relationship,

            @NotBlank(message = "Informe o telefone do contato de emergência.")
            @Size(max = 25, message = "Telefone de emergência inválido.")
            String phone
    ) {
    }

    public record GoalsAndExpectations(
            @NotNull(message = "Selecione o objetivo principal.")
            PrimaryGoal primaryGoal,

            @Size(max = 150, message = "A descrição do objetivo deve ter no máximo 150 caracteres.")
            String primaryGoalOther,

            @Size(max = 150, message = "O objetivo secundário deve ter no máximo 150 caracteres.")
            String secondaryGoal,

            @Size(max = 150, message = "A região do corpo deve ter no máximo 150 caracteres.")
            String focusBodyArea,

            @NotBlank(message = "Informe sua disponibilidade semanal para treinar.")
            @Size(max = 255, message = "A disponibilidade deve ter no máximo 255 caracteres.")
            String weeklyAvailability,

            @NotNull(message = "Informe quantas vezes por semana pretende treinar.")
            @Min(value = 1, message = "A frequência deve ser entre 1 e 7 vezes por semana.")
            @Max(value = 7, message = "A frequência deve ser entre 1 e 7 vezes por semana.")
            Integer intendedWeeklyFrequency
    ) {
    }

    public record PhysicalAssessment(
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
            Integer restingHeartRate
    ) {
    }

    public record ConsentData(
            @NotBlank(message = "Versão do termo não informada.")
            @Size(max = 20)
            String termsVersion,

            @AssertTrue(message = "É obrigatório concordar com o Termo de Responsabilidade.")
            boolean termsAccepted,

            @AssertTrue(message = "É obrigatório autorizar o tratamento de dados conforme a LGPD.")
            boolean lgpdConsentAccepted,

            @NotBlank(message = "Informe o nome completo para identificação e assinatura.")
            @Size(min = 3, max = 150, message = "O nome do assinante deve ter entre 3 e 150 caracteres.")
            String signerName,

            @NotBlank(message = "A assinatura digital é obrigatória.")
            String signatureImage
    ) {
    }
}
