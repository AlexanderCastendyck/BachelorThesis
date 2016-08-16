package castendyck.roadmap;

import castendyck.artifactidentifier.ArtifactIdentifier;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.vulnerablepoint.VulnerablePoint;

import java.util.List;

public class RoadSection {
    private final ArtifactIdentifier artifactIdentifier;
    private final List<VulnerablePoint> vulnerablePoints;

    public RoadSection(ArtifactIdentifier artifactIdentifier, List<VulnerablePoint> vulnerablePoints) {
        NotNullConstraintEnforcer.ensureNotNull(artifactIdentifier);
        this.artifactIdentifier = artifactIdentifier;
        NotNullConstraintEnforcer.ensureNotNull(vulnerablePoints);
        this.vulnerablePoints = vulnerablePoints;
    }

    public ArtifactIdentifier getArtifactIdentifier() {
        return artifactIdentifier;
    }

    public List<VulnerablePoint> getVulnerablePoints() {
        return vulnerablePoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoadSection that = (RoadSection) o;

        if (!artifactIdentifier.equals(that.artifactIdentifier)) return false;
        return vulnerablePoints.equals(that.vulnerablePoints);

    }

    @Override
    public int hashCode() {
        int result = artifactIdentifier.hashCode();
        result = 31 * result + vulnerablePoints.hashCode();
        return result;
    }
}
