package castendyck.processedcve;

import castendyck.callgraph.CallPath;
import castendyck.cve.CVE;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.vulnerablepoint.VulnerablePoint;

import java.util.List;

public class ProcessedCve {
    private final CVE cve;
    private final List<CallPath> callPaths;
    private final List<VulnerablePoint> vulnerablePoints;

    public ProcessedCve(CVE cve, List<CallPath> callPaths, List<VulnerablePoint> vulnerablePoints) {
        NotNullConstraintEnforcer.ensureNotNull(cve);
        this.cve = cve;
        NotNullConstraintEnforcer.ensureNotNull(callPaths);
        this.callPaths = callPaths;
        NotNullConstraintEnforcer.ensureNotNull(vulnerablePoints);
        this.vulnerablePoints = vulnerablePoints;
    }

    public CVE getCve() {
        return cve;
    }

    public List<CallPath> getCallPaths() {
        return callPaths;
    }

    public List<VulnerablePoint> getVulnerablePoints() {
        return vulnerablePoints;
    }
}
