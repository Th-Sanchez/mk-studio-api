package com.mkstudio.student.dto;

import com.mkstudio.student.PrimaryGoal;
import com.mkstudio.student.StudentStatus;
import java.time.Instant;
import java.util.UUID;

public record StudentListItemResponse(
        UUID id,
        String fullName,
        int age,
        String phone,
        PrimaryGoal primaryGoal,
        String primaryGoalOther,
        String weeklyAvailability,
        Integer intendedWeeklyFrequency,
        boolean hasHealthAlerts,
        int healthAlertsCount,
        StudentStatus status,
        Instant createdAt
) {
}
