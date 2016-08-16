package castendyck.vulnerablepoint;

import castendyck.cve.CVE;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;

public class Then {
    private final List<VulnerablePoint> vulnerablePoints;
    private final List<VulnerablePoint> expectedVulnerablePoints;

    public Then(List<VulnerablePoint> vulnerablePoints, List<VulnerablePoint> expectedVulnerablePoints) {
        this.vulnerablePoints = vulnerablePoints;
        this.expectedVulnerablePoints = expectedVulnerablePoints;
    }

    public void thenNoVulnerablePointsShouldBeFound() {
        assertThat(expectedVulnerablePoints, hasSize(0));
    }

    public void thenOneVulnerablePointShouldBeFound() {
        assertThat(vulnerablePoints, hasSize(1));
        final VulnerablePoint v = vulnerablePoints.get(0);
        final VulnerablePoint e = expectedVulnerablePoints.get(0);

        assertThat(vulnerablePoints, equalTo(expectedVulnerablePoints));
    }

    public void thenAllVulnerablePointShouldBeFound() {
        assertThat(vulnerablePoints, equalTo(expectedVulnerablePoints));
    }

    public void thenTwoVulnerablePointShouldBeFound() {
        assertThat(vulnerablePoints, hasSize(2));
        final VulnerablePoint v = vulnerablePoints.get(0);
        final VulnerablePoint e = expectedVulnerablePoints.get(0);

        final boolean e1 = v.getArtifactIdentifier().equals(e.getArtifactIdentifier());
        final CVE cV = v.getAssociatedCve();
        final CVE cE = e.getAssociatedCve();

        final boolean name = cV.getName().equals(cE.getName());
        final boolean cwe = cV.getCwe().equals(cE.getCwe());
        final boolean desc = cV.getDescription().equals(cE.getDescription());
        final boolean cpes = cV.getCpes().equals(cE.getCpes());
        final boolean cvss = cV.getCvss().equals(cE.getCvss());
        final boolean vulns = cV.getVulnerableSoftwareList().equals(cE.getVulnerableSoftwareList());
        final boolean refs = cV.getReferences().equals(cE.getReferences());

    }
}
