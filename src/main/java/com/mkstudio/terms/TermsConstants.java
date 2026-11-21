package com.mkstudio.terms;

import java.util.List;

public final class TermsConstants {

    public static final String CURRENT_VERSION = "v1";
    public static final String TITLE = "TERMO DE RESPONSABILIDADE – MK STUDIO";

    public static final List<String> PARAGRAPHS = List.of(
            "Declaro que todas as informações fornecidas nesta anamnese são verdadeiras e completas, comprometendo-me a informar ao MK STUDIO qualquer alteração no meu estado de saúde, surgimento de lesões, dores, limitações físicas ou diagnóstico médico durante o período em que estiver realizando atividades no estabelecimento.",
            "Declaro estar ciente de que a prática de atividades físicas exige atenção às minhas condições individuais de saúde e que devo respeitar as orientações dos profissionais responsáveis.",
            "Comprometo-me a comunicar imediatamente qualquer desconforto, dor ou mal-estar durante a realização dos exercícios.",
            "Declaro também que estou ciente de que a omissão de informações relevantes sobre minha saúde pode comprometer a segurança e a qualidade da prescrição dos exercícios."
    );

    public static final String TERMS_ACCEPTANCE_LABEL =
            "Declaro que li e concordo com as informações acima.";

    public static final String LGPD_CONSENT_LABEL =
            "Autorizo o tratamento dos meus dados pessoais e dados de saúde para fins de avaliação, planejamento e acompanhamento das atividades físicas, conforme a LGPD.";

    private TermsConstants() {
    }
}
