package com.mkstudio.student.dto;

import com.mkstudio.student.Gender;
import com.mkstudio.student.StudentStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UpdateStudentRequest(
        @NotBlank(message = "Informe o nome completo.")
        @Size(min = 3, max = 150)
        String fullName,

        @NotNull(message = "Informe a data de nascimento.")
        @Past(message = "A data de nascimento deve estar no passado.")
        LocalDate birthDate,

        @NotNull(message = "Informe o sexo.")
        Gender gender,

        @NotBlank(message = "Informe o telefone/WhatsApp.")
        @Size(max = 25)
        String phone,

        @Email(message = "Informe um e-mail válido.")
        @Size(max = 160)
        String email,

        @NotNull(message = "Informe o endereço.")
        @Valid
        PublicRegistrationRequest.AddressData address,

        @NotNull(message = "Informe o contato de emergência.")
        @Valid
        PublicRegistrationRequest.EmergencyContact emergencyContact,

        @NotNull(message = "Informe o status do aluno.")
        StudentStatus status
) {
}
