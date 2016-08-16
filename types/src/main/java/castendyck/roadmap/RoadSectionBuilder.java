package castendyck.roadmap;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.vulnerablepoint.VulnerablePoint;

import java.util.ArrayList;
import java.util.List;

public class RoadSectionBuilder {
    private List<VulnerablePoint> vulnerablePoints = new ArrayList<>();
    private ArtifactIdentifier artifactIdentifier;

    public static RoadSectionBuilder aRoadSection() {
        return new RoadSectionBuilder();
    }

    public RoadSectionBuilder withArtifactIdentifier(ArtifactIdentifier artifactIdentifier) {
        this.artifactIdentifier = artifactIdentifier;
        return this;
    }

    public RoadSectionBuilder withVulnerablePoints(List<VulnerablePoint> vulnerablePoints) {
        this.vulnerablePoints = vulnerablePoints;
        return this;
    }

    public RoadSectionBuilder withAVulnerablePoint(VulnerablePoint vulnerablePoint) {
        this.vulnerablePoints.add(vulnerablePoint);
        return this;
    }

    public RoadSection build() {
        final RoadSection roadSection = new RoadSection(artifactIdentifier, vulnerablePoints);
        return roadSection;
    }
}
