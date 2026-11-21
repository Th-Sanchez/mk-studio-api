package com.mkstudio.terms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/terms")
public class TermsController {

    @GetMapping("/current")
    public TermsResponse getCurrentTerms() {
        return new TermsResponse(
                TermsConstants.CURRENT_VERSION,
                TermsConstants.TITLE,
                TermsConstants.PARAGRAPHS,
                TermsConstants.TERMS_ACCEPTANCE_LABEL,
                TermsConstants.LGPD_CONSENT_LABEL
        );
    }
}
