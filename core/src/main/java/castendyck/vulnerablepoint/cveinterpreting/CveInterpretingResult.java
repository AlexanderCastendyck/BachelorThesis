package castendyck.vulnerablepoint.cveinterpreting;

import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.vulnerablepoint.VulnerablePoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CveInterpretingResult {
    public enum RESULT {HAS_RESULT, NO_USABLE_RESULT}

    private final CveInterpretingResult.RESULT result;
    private final List<VulnerablePoint> vulnerablePoints;

    public CveInterpretingResult(RESULT result, List<VulnerablePoint> vulnerablePoints) {
        NotNullConstraintEnforcer.ensureNotNull(result);
        this.result = result;
        NotNullConstraintEnforcer.ensureNotNull(vulnerablePoints);
        this.vulnerablePoints = vulnerablePoints;
    }
    public CveInterpretingResult(RESULT result, Set<VulnerablePoint> vulnerablePointSet) {
        NotNullConstraintEnforcer.ensureNotNull(result);
        this.result = result;
        NotNullConstraintEnforcer.ensureNotNull(vulnerablePointSet);
        this.vulnerablePoints = new ArrayList<>(vulnerablePointSet);
    }

    public boolean gotAResult() {
        return result == RESULT.HAS_RESULT;
    }

    public List<VulnerablePoint> getVulnerablePoints() {
        return vulnerablePoints;
    }
}
