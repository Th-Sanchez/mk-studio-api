package com.mkstudio.student.dto;

import com.mkstudio.student.StudentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStudentStatusRequest(
        @NotNull(message = "Informe o novo status.")
        StudentStatus status
) {
}
