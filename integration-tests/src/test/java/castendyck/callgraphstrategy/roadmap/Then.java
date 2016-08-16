package castendyck.callgraphstrategy.roadmap;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.dependencygraphing.GraphNodeWithVulnerablePointsBuilder;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.roadmap.RoadMap;
import castendyck.roadmap.RoadMapBuilder;
import castendyck.roadmap.RoadSectionBuilder;
import castendyck.vulnerablepoint.VulnerablePoint;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Then {
    private final RoadMap roadMap;
    private final RoadMapBuilder roadMapBuilder;

    public Then(RoadMap roadMap) {
        this.roadMap = roadMap;
        this.roadMapBuilder = new RoadMapBuilder();
    }

    public Then asFirstStage(GraphNodeWithVulnerablePointsBuilder graphNodeBuilder) {
        addSectionToExpectedRoadMap(graphNodeBuilder);
        return this;
    }

    public Then asSecondStage(GraphNodeWithVulnerablePointsBuilder graphNodeBuilder) {
        addSectionToExpectedRoadMap(graphNodeBuilder);
        return this;
    }

    public Then asThirdStage(GraphNodeWithVulnerablePointsBuilder graphNodeBuilder) {
        addSectionToExpectedRoadMap(graphNodeBuilder);
        return this;
    }

    public Then asFourthStage(GraphNodeWithVulnerablePointsBuilder graphNodeBuilder) {
        addSectionToExpectedRoadMap(graphNodeBuilder);
        return this;
    }

    public void noStages() {
        assertThat(roadMap.hasNext(), is(false));
    }

    public void listed() {
        final RoadMap expectedRoadMap = roadMapBuilder.build();
        assertThat(roadMap, equalTo(expectedRoadMap));
    }

    private void addSectionToExpectedRoadMap(GraphNodeWithVulnerablePointsBuilder graphNodeBuilder) {
        final GraphNodeWithVulnerablePointsBuilder.GraphNodePackage graphNodePackage = graphNodeBuilder.build();
        final List<VulnerablePoint> vulnerablePoints = graphNodePackage.getVulnerablePoints();


        final String name = graphNodeBuilder.getName();
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("local", name, "1.0");

        roadMapBuilder.withARoadSection(RoadSectionBuilder.aRoadSection()
                .withArtifactIdentifier(artifactIdentifier)
                .withVulnerablePoints(vulnerablePoints));
    }
}
