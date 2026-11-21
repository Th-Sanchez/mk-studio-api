package com.mkstudio.student;

import static org.assertj.core.api.Assertions.assertThat;

import com.mkstudio.student.model.MedicalHistory;
import com.mkstudio.student.model.YesNoDetail;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class HealthAlertExtractorTest {

    @Test
    void returnsEmptyListWhenNoHealthAlertIsPresent() {
        MedicalHistory history = new MedicalHistory(
                new MedicalHistory.CurrentConditions(
                        YesNoDetail.no(), YesNoDetail.no(), YesNoDetail.no(), YesNoDetail.no(),
                        YesNoDetail.no(), YesNoDetail.no(), YesNoDetail.no(), YesNoDetail.no(),
                        YesNoDetail.no(), YesNoDetail.no(), YesNoDetail.no()
                ),
                new MedicalHistory.FamilyHistory(false, false, false, YesNoDetail.no(), null),
                new MedicalHistory.LifestyleHabits(false, false, false, SleepQuality.GOOD, new BigDecimal("7.5"), StressLevel.LOW),
                new MedicalHistory.InjuryHistory(
                        YesNoDetail.no(), YesNoDetail.no(), YesNoDetail.no(), YesNoDetail.no(),
                        YesNoDetail.no(), YesNoDetail.no(), YesNoDetail.no(), YesNoDetail.no(),
                        YesNoDetail.no()
                ),
                new MedicalHistory.MedicationsAndCare(false, null, true, false, YesNoDetail.no(), true)
        );

        assertThat(HealthAlertExtractor.extractAlerts(history)).isEmpty();
    }

    @Test
    void extractsAlertsWithDetailsAcrossConditionsInjuriesAndMedications() {
        MedicalHistory history = new MedicalHistory(
                new MedicalHistory.CurrentConditions(
                        YesNoDetail.no(), YesNoDetail.no(), YesNoDetail.no(), YesNoDetail.no(),
                        YesNoDetail.no(), new YesNoDetail(true, "Joelho direito"), YesNoDetail.no(), YesNoDetail.no(),
                        YesNoDetail.no(), YesNoDetail.no(), YesNoDetail.no()
                ),
                new MedicalHistory.FamilyHistory(true, false, false, YesNoDetail.no(), null),
                new MedicalHistory.LifestyleHabits(false, false, false, SleepQuality.REGULAR, new BigDecimal("6.0"), StressLevel.MODERATE),
                new MedicalHistory.InjuryHistory(
                        YesNoDetail.no(), YesNoDetail.no(), YesNoDetail.no(), YesNoDetail.no(),
                        YesNoDetail.no(), new YesNoDetail(true, "Agachamento"), YesNoDetail.no(), YesNoDetail.no(),
                        new YesNoDetail(true, null)
                ),
                new MedicalHistory.MedicationsAndCare(true, "Losartana 50mg", true, false, YesNoDetail.no(), true)
        );

        List<String> alerts = HealthAlertExtractor.extractAlerts(history);

        assertThat(alerts).containsExactly(
                "Problema articular: Joelho direito",
                "Dor durante movimentos específicos: Agachamento",
                "Restrição médica para exercícios",
                "Usa medicamento: Losartana 50mg"
        );
    }
}
