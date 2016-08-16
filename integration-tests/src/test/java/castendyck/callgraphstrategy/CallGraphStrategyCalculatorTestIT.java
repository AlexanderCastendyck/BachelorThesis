package castendyck.callgraphstrategy;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.cve.CVE;
import castendyck.functionidentifier.FunctionIdentifierBuilder;
import castendyck.vulnerablepoint.VulnerablePointBuilder;
import castendyck.examplecves.ExampleCve;
import org.junit.Test;

import static castendyck.callgraphstrategy.Given.given;
import static castendyck.callgraphstrategy.RoadMapGuidedStrategyTester.aStrategyThat;
import static castendyck.dependencygraphing.GraphBuilder.aGraph;
import static castendyck.dependencygraphing.GraphNodeWithVulnerablePointsBuilder.aNode;

public class CallGraphStrategyCalculatorTestIT {
    @Test
    public void noVulnerablePoints_resultsInEmptyRoadMap() throws Exception {
        given(aGraph()
                .withARootNode(aNode("A")
                        .withNoVulnerablePoints()))
                .whenStrategyIsCreated()
                .thenExpect(aStrategyThat()
                        .traversesNoNode());
    }

    @Test
    public void graphWithOneNodeAndOneVulnerablePoint_resultsInRoadMapWithOneStageWithOneVulnerablePoint() throws Exception {
        given(aGraph()
                .withARootNode(aNode("R").withVulnerablePoints(aVulnerablePoint("V", "R"))))
                .whenStrategyIsCreated()
                .thenExpect(aStrategyThat()
                        .traverses(aNode("R")));
    }

    @Test
    public void graphWithOneNodeAndThreeVulnerablePoints_resultsInRoadMapWithOneStageWithThreeVulnerablePoints() throws Exception {
        given(aGraph()
                .withARootNode(aNode("R")
                        .withVulnerablePoints(aVulnerablePoint("V1", "R"),
                                aVulnerablePoint("V2", "R"),
                                aVulnerablePoint("V3", "R"))))
                .whenStrategyIsCreated()
                .thenExpect(aStrategyThat()
                        .traverses(aNode("R")));
    }

    @Test
    public void graphWithTwoRelatedNodesAndOneVulnerablePointInChildNode_resultsInRoadMapWithTwoStagesWithOneVulnerablePointInChildNode() throws Exception {
        given(aGraph()
                .withARootNode(aNode("R")
                        .withChildren(
                                aNode("C1").withVulnerablePoints(aVulnerablePoint("V", "C1")))))
                .whenStrategyIsCreated()
                .thenExpect(aStrategyThat()
                        .traverses(aNode("C1"))
                        .andThenTraverses(aNode("R")));
    }


    @Test
    public void graphWithTwoUnrelatedChildNodesAndOneVulnerablePointInOneNode_resultsInRoadMapWithTwoStagesWithOneVulnerablePointInChild() throws Exception {
        given(aGraph()
                .withARootNode(aNode("R")
                        .withChildren(
                                aNode("C1").withVulnerablePoints(aVulnerablePoint("V", "C1")),
                                aNode("C2").withNoVulnerablePoints())))
                .whenStrategyIsCreated()
                .thenExpect(aStrategyThat()
                        .traverses(aNode("C1"))
                        .andThenTraverses(aNode("R")));
    }

    @Test
    public void graphWithTwoUnrelatedChildNodesAndOneVulnerablePointInEachNode_resultsInRoadMapWithThreeStagesWithOneVulnerablePointinEachChild() throws Exception {
        given(aGraph()
                .withARootNode(aNode("R")
                        .withChildren(
                                aNode("C1").withVulnerablePoints(aVulnerablePoint("V1", "C1")),
                                aNode("C2").withVulnerablePoints(aVulnerablePoint("V2", "C2")))))
                .whenStrategyIsCreated()
                .thenExpect(aStrategyThat()
                        .traverses(aNode("C1"))
                        .andThenTraverses(aNode("C2"))
                        .andThenTraverses(aNode("R")));

    }

    @Test
    public void graphWithTwoRelatedNodesAndOneVulnerablePointInParentNode_resultsInRoadMapWithOneStageWithOneVulnerablePoint() throws Exception {
        given(aGraph()
                .withARootNode(aNode("R")
                        .withVulnerablePoints(aVulnerablePoint("V", "R"))
                        .withChildren(
                                aNode("C1").withNoVulnerablePoints(),
                                aNode("C2").withNoVulnerablePoints())))
                .whenStrategyIsCreated()
                .thenExpect(aStrategyThat()
                        .traverses(aNode("R")));
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
                .whenStrategyIsCreated()
                .thenExpect(aStrategyThat()
                        .traverses(aNode("C1_1"))
                        .andThenTraverses(aNode("C1_2"))
                        .andThenTraverses(aNode("C1"))
                        .andThenTraverses(aNode("R")));
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
