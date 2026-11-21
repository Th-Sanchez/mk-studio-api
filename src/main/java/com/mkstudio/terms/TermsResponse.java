package com.mkstudio.terms;

import java.util.List;

public record TermsResponse(
        String version,
        String title,
        List<String> paragraphs,
        String termsAcceptanceLabel,
        String lgpdConsentLabel
) {
}
