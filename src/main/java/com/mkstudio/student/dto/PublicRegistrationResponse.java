package com.mkstudio.student.dto;

import com.mkstudio.student.StudentStatus;
import java.time.Instant;
import java.util.UUID;

public record PublicRegistrationResponse(
        UUID studentId,
        UUID anamnesisId,
        String fullName,
        StudentStatus status,
        Instant signedAt
) {
}
