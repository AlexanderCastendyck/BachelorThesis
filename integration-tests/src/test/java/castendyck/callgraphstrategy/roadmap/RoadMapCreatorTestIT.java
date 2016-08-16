package castendyck.callgraphstrategy.roadmap;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.cve.CVE;
import castendyck.examplecves.ExampleCve;
import castendyck.functionidentifier.FunctionIdentifierBuilder;
import castendyck.vulnerablepoint.VulnerablePointBuilder;
import org.junit.Test;

import static castendyck.callgraphstrategy.roadmap.Given.given;
import static castendyck.dependencygraphing.GraphBuilder.aGraph;
import static castendyck.dependencygraphing.GraphNodeWithVulnerablePointsBuilder.aNode;

public class RoadMapCreatorTestIT {

    @Test
    public void noVulnerablePoints_resultsInEmptyRoadMap() throws Exception {
        given(aGraph()
                .withARootNode(aNode("A")
                        .withNoVulnerablePoints()))
                .whenRoadMapIsCreated()
                .thenRoadMapShouldHave()
                .noStages();
    }

    @Test
    public void graphWithOneNodeAndOneVulnerablePoint_resultsInRoadMapWithOneStageWithOneVulnerablePoint() throws Exception {
        given(aGraph()
                .withARootNode(aNode("R")
                        .withVulnerablePoints(aVulnerablePoint("V", "R"))))
                .whenRoadMapIsCreated()
                .thenRoadMapShouldHave()
                .asFirstStage(aNode("R").withVulnerablePoints(aVulnerablePoint("V", "R")))
                .listed();
    }

    @Test
    public void graphWithOneNodeAndThreeVulnerablePoints_resultsInRoadMapWithOneStageWithThreeVulnerablePoints() throws Exception {
        given(aGraph()
                .withARootNode(aNode("R")
                        .withVulnerablePoints(aVulnerablePoint("V1", "R"),
                                aVulnerablePoint("V2", "R"),
                                aVulnerablePoint("V3", "R"))))
                .whenRoadMapIsCreated()
                .thenRoadMapShouldHave()
                .asFirstStage(aNode("R").withVulnerablePoints(aVulnerablePoint("V1", "R"),
                        aVulnerablePoint("V2", "R"),
                        aVulnerablePoint("V3", "R")))
                .listed();
    }

    @Test
    public void graphWithTwoRelatedNodesAndOneVulnerablePointInChildNode_resultsInRoadMapWithTwoStagesWithOneVulnerablePointInChildNode() throws Exception {
        given(aGraph()
                .withARootNode(aNode("R")
                        .withChildren(
                                aNode("C1").withVulnerablePoints(aVulnerablePoint("V", "C1")))))
                .whenRoadMapIsCreated()
                .thenRoadMapShouldHave()
                .asFirstStage(aNode("C1").withVulnerablePoints(aVulnerablePoint("V", "C1")))
                .asSecondStage(aNode("R").withNoVulnerablePoints())
                .listed();
    }


    @Test
    public void graphWithTwoUnrelatedChildNodesAndOneVulnerablePointInOneNode_resultsInRoadMapWithTwoStagesWithOneVulnerablePointInChild() throws Exception {
        given(aGraph()
                .withARootNode(aNode("R")
                        .withChildren(
                                aNode("C1").withVulnerablePoints(aVulnerablePoint("V", "C1")),
                                aNode("C2").withNoVulnerablePoints())))
                .whenRoadMapIsCreated()
                .thenRoadMapShouldHave()
                .asFirstStage(aNode("C1").withVulnerablePoints(aVulnerablePoint("V", "C1")))
                .asSecondStage(aNode("R").withNoVulnerablePoints())
                .listed();
    }

    @Test
    public void graphWithTwoUnrelatedChildNodesAndOneVulnerablePointInEachNode_resultsInRoadMapWithThreeStagesWithOneVulnerablePointinEachChild() throws Exception {
        given(aGraph()
                .withARootNode(aNode("R")
                        .withChildren(
                                aNode("C1").withVulnerablePoints(aVulnerablePoint("V1", "C1")),
                                aNode("C2").withVulnerablePoints(aVulnerablePoint("V2", "C2")))))
                .whenRoadMapIsCreated()
                .thenRoadMapShouldHave()
                .asFirstStage(aNode("C1").withVulnerablePoints(aVulnerablePoint("V1", "C1")))
                .asSecondStage(aNode("C2").withVulnerablePoints(aVulnerablePoint("V2", "C2")))
                .asThirdStage(aNode("R").withNoVulnerablePoints())
                .listed();

    }

    @Test
    public void graphWithTwoRelatedNodesAndOneVulnerablePointInParentNode_resultsInRoadMapWithOneStageWithOneVulnerablePoint() throws Exception {
        given(aGraph()
                .withARootNode(aNode("R")
                        .withVulnerablePoints(aVulnerablePoint("V", "R"))
                        .withChildren(
                                aNode("C1").withNoVulnerablePoints(),
                                aNode("C2").withNoVulnerablePoints())))
                .whenRoadMapIsCreated()
                .thenRoadMapShouldHave()
                .asFirstStage(aNode("R").withVulnerablePoints(aVulnerablePoint("V", "R")))
                .listed();
    }

    @Test
    public void graphWithSeveralNodes_resultingRoadMapShouldContainOnlyNodesThatShouldBeTraversed() throws Exception {
        given(aGraph().withARootNode(aNode("R")
                .withVulnerablePoints(aVulnerablePoint("V_R", "R"))
                .withChildren(
                        aNode("C1").withVulnerablePoints(aVulnerablePoint("V_C1", "C1"))
                                .withChildren(
                                        aNode("C1_1").withVulnerablePoints(aVulnerablePoint("V_C1_1", "C1_1")),
                                        aNode("C1_2").withVulnerablePoints(aVulnerablePoint("V_C1_2", "C1_2"))),
                        aNode("C2").withNoVulnerablePoints())))
                .whenRoadMapIsCreated()
                .thenRoadMapShouldHave()
                .asFirstStage(aNode("C1_1").withVulnerablePoints(aVulnerablePoint("V_C1_1", "C1_1")))
                .asSecondStage(aNode("C1_2").withVulnerablePoints(aVulnerablePoint("V_C1_2", "C1_2")))
                .asThirdStage(aNode("C1").withVulnerablePoints(aVulnerablePoint("V_C1", "C1")))
                .asFourthStage(aNode("R").withVulnerablePoints(aVulnerablePoint("V_R", "R")))
                .listed();
    }

    private VulnerablePointBuilder aVulnerablePoint(String name, String nodeName) {
        final CVE cve = ExampleCve.cveForMailApi().get(0);
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("local", nodeName, "1.0");
        final VulnerablePointBuilder vulnerablePointBuilder = VulnerablePointBuilder.aVulnerablePoint()
                .withFunctionIdentifier(FunctionIdentifierBuilder.aFunctionIdentifier()
                        .withArtifactIdentifier(artifactIdentifier)
                        .forClass("someClass.class")
                        .forFunction(name)
                        .build())
                .forCve(cve);
        return vulnerablePointBuilder;
    }
}
