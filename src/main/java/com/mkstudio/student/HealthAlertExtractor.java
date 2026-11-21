package com.mkstudio.student;

import com.mkstudio.common.NormalizationUtils;
import com.mkstudio.student.model.MedicalHistory;
import com.mkstudio.student.model.YesNoDetail;
import java.util.ArrayList;
import java.util.List;

public final class HealthAlertExtractor {

    private HealthAlertExtractor() {
    }

    public static List<String> extractAlerts(MedicalHistory history) {
        if (history == null) {
            return List.of();
        }

        List<String> alerts = new ArrayList<>();

        MedicalHistory.CurrentConditions c = history.currentConditions();
        if (c != null) {
            addIfYes(alerts, "Doença diagnosticada", c.diagnosedDisease());
            addIfYes(alerts, "Problema cardíaco", c.heartProblem());
            addIfYes(alerts, "Alteração de pressão arterial", c.bloodPressureIssue());
            addIfYes(alerts, "Diabetes", c.diabetes());
            addIfYes(alerts, "Problema respiratório", c.respiratoryProblem());
            addIfYes(alerts, "Problema articular", c.jointProblem());
            addIfYes(alerts, "Problema na coluna", c.spineProblem());
            addIfYes(alerts, "Dores frequentes", c.frequentPain());
            addIfYes(alerts, "Tontura ou desmaios", c.dizzinessOrFainting());
            addIfYes(alerts, "Falta de ar durante exercícios", c.shortnessOfBreathDuringExercise());
            addIfYes(alerts, "Outra condição de saúde", c.otherHealthCondition());
        }

        MedicalHistory.InjuryHistory i = history.injuryHistory();
        if (i != null) {
            addIfYes(alerts, "Histórico de fratura", i.fracture());
            addIfYes(alerts, "Histórico de luxação", i.dislocation());
            addIfYes(alerts, "Lesão muscular", i.muscleInjury());
            addIfYes(alerts, "Lesão ligamentar", i.ligamentInjury());
            addIfYes(alerts, "Limitação de movimento", i.movementLimitation());
            addIfYes(alerts, "Dor durante movimentos específicos", i.painDuringSpecificMovements());
            addIfYes(alerts, "Cirurgia ou procedimento médico", i.surgeryOrMedicalProcedure());
            addIfYes(alerts, "Já interrompeu exercícios por saúde", i.stoppedExerciseForHealthReason());
            addIfYes(alerts, "Restrição médica para exercícios", i.medicalRestrictionForExercise());
        }

        MedicalHistory.MedicationsAndCare m = history.medicationsAndCare();
        if (m != null) {
            if (m.usesMedication()) {
                String meds = NormalizationUtils.trimToNull(m.medicationsList());
                alerts.add(meds != null ? "Usa medicamento: " + meds : "Usa medicamento atualmente");
            }
            addIfYes(alerts, "Recomendação/restrição médica", m.medicalRecommendationOrRestriction());
        }

        return List.copyOf(alerts);
    }

    private static void addIfYes(List<String> alerts, String label, YesNoDetail item) {
        if (item != null && item.answer()) {
            String details = NormalizationUtils.trimToNull(item.details());
            alerts.add(details != null ? label + ": " + details : label);
        }
    }
}
