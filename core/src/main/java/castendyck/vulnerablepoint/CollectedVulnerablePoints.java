package castendyck.vulnerablepoint;

import castendyck.cve.CVE;
import de.castendyck.enforcing.NotNullConstraintEnforcer;

import java.util.List;

public class CollectedVulnerablePoints {
    private final List<CVE> cves;
    private final List<VulnerablePoint> vulnerablePoints;

    public CollectedVulnerablePoints(List<CVE> cves, List<VulnerablePoint> vulnerablePoints) {
        NotNullConstraintEnforcer.ensureNotNull(cves);
        this.cves = cves;
        NotNullConstraintEnforcer.ensureNotNull(vulnerablePoints);
        this.vulnerablePoints = vulnerablePoints;
    }

    public List<CVE> getCves() {
        return cves;
    }

    public List<VulnerablePoint> getVulnerablePoints() {
        return vulnerablePoints;
    }
}
