package castendyck.analysisrequest;

import castendyck.callgraph.CallPath;
import castendyck.cve.CVE;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.vulnerablepoint.VulnerablePoint;

import java.util.List;

public class AnalysisRequest {
    private final List<VulnerablePoint> vulnerablePoints;
    private final List<CallPath> callPaths;
    private final List<CVE> cves;

    public AnalysisRequest(List<CVE> cves, List<VulnerablePoint> vulnerablePoints, List<CallPath> callPaths) {
        NotNullConstraintEnforcer.ensureNotNull(cves);
        this.cves = cves;
        NotNullConstraintEnforcer.ensureNotNull(vulnerablePoints);
        this.vulnerablePoints = vulnerablePoints;
        NotNullConstraintEnforcer.ensureNotNull(callPaths);
        this.callPaths = callPaths;
    }

    public List<VulnerablePoint> getVulnerablePoints() {
        return vulnerablePoints;
    }

    public List<CVE> getCves() {
        return cves;
    }

    public List<CallPath> getCallPaths() {
        return callPaths;
    }
}
