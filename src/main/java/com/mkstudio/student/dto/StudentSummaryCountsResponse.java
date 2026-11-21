package com.mkstudio.student.dto;

public record StudentSummaryCountsResponse(
        long total,
        long newCount,
        long activeCount,
        long inactiveCount,
        long withHealthAlertsCount
) {
}
