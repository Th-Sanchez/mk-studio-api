package com.mkstudio.student.model;

import jakarta.validation.constraints.Size;

public record YesNoDetail(
        boolean answer,
        @Size(max = 500, message = "Os detalhes devem ter no máximo 500 caracteres.")
        String details
) {
    public static YesNoDetail no() {
        return new YesNoDetail(false, null);
    }
}
