package castendyck.judgement.internal;

import castendyck.certainty.Certainty;
import castendyck.cve.CVE;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.judgement.Judgement;
import castendyck.media.Media;
import castendyck.media.ResultLogLevel;
import castendyck.processedcve.ProcessedCve;
import castendyck.reduction.Reduction;
import castendyck.risklevel.RiskLevel;

public class JudgementImpl implements Judgement {
    private final Certainty certainty;
    private final RiskLevel riskLevel;
    private final ProcessedCve processedCve;
    private final Reduction reduction;

    public JudgementImpl(Certainty certainty, RiskLevel riskLevel, ProcessedCve processedCve, Reduction reduction) {
        NotNullConstraintEnforcer.ensureNotNull(certainty);
        this.certainty = certainty;
        NotNullConstraintEnforcer.ensureNotNull(riskLevel);
        this.riskLevel = riskLevel;
        NotNullConstraintEnforcer.ensureNotNull(processedCve);
        this.processedCve = processedCve;
        NotNullConstraintEnforcer.ensureNotNull(reduction);
        this.reduction = reduction;
    }

    @Override
    public Reduction getReduction() {
        return reduction;
    }

    @Override
    public void print(Media media) {
        final ResultLogLevel resultLogLevel = media.getResultLogLevel();
        if (thisResultShouldBeLogged(resultLogLevel)) {
            final CVE cve = processedCve.getCve();
            media.with("cveName", cve.getName())
                    .withText(": ")
                    .with("risk", riskLevel.toString())
                    .withText(", ")
                    .with("certainty", certainty.toString());
        }
    }

    private boolean thisResultShouldBeLogged(ResultLogLevel resultLogLevel) {
        switch (resultLogLevel) {
            case ONLY_CERTAIN_RESULTS:
                return certainty.equals(Certainty.CERTAIN);
            case ONLY_RISK_RESULTS:
                return riskLevel.equals(RiskLevel.HIGH);
            case ONLY_CERTAIN_RISK_RESULTS:
                return certainty.equals(Certainty.CERTAIN) && riskLevel.equals(RiskLevel.HIGH);
            case ALL:
                return true;
            default: return true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JudgementImpl judgement = (JudgementImpl) o;

        if (certainty != judgement.certainty) return false;
        if (riskLevel != judgement.riskLevel) return false;
        if (!processedCve.equals(judgement.processedCve)) return false;
        return reduction.equals(judgement.reduction);

    }

    @Override
    public int hashCode() {
        int result = certainty.hashCode();
        result = 31 * result + riskLevel.hashCode();
        result = 31 * result + processedCve.hashCode();
        result = 31 * result + reduction.hashCode();
        return result;
    }
}
