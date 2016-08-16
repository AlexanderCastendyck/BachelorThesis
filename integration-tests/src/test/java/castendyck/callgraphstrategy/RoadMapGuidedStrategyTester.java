package castendyck.callgraphstrategy;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.callgraph.CallPath;
import castendyck.callgraph.CallPathBuilder;
import castendyck.dependency.Dependency;
import castendyck.dependencygraph.GraphNode;
import castendyck.dependencygraphing.GraphNodeWithVulnerablePointsBuilder;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functionidentifier.FunctionIdentifierBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoadMapGuidedStrategyTester {
    private final List<GraphNodeWithVulnerablePointsBuilder> nodesToTraverse;
    private boolean strategyShouldNotTraverseAnyNode;

    public RoadMapGuidedStrategyTester() {
        this.nodesToTraverse = new ArrayList<>();
        this.strategyShouldNotTraverseAnyNode = false;
    }

    public static RoadMapGuidedStrategyTester aStrategyThat() {
        return new RoadMapGuidedStrategyTester();
    }

    public RoadMapGuidedStrategyTester traverses(GraphNodeWithVulnerablePointsBuilder graphNodeBuilder) {
        nodesToTraverse.add(graphNodeBuilder);
        return this;
    }

    public RoadMapGuidedStrategyTester andThenTraverses(GraphNodeWithVulnerablePointsBuilder graphNodeBuilder) {
        return traverses(graphNodeBuilder);
    }

    public RoadMapGuidedStrategyTester traversesNoNode() {
        this.strategyShouldNotTraverseAnyNode = true;
        return this;
    }

    public boolean testStrategy(CallGraphStrategy strategy) throws StrategyUnexpectedStateException {
        if (strategyShouldNotTraverseAnyNode) {
            final boolean isFinished = expectStrategyToHaveAlreadyFinished(strategy);
            return isFinished;
        } else {
            final boolean allCorrect = checkThatStrategyTraversesAllNodesInCorrectOrder(strategy);
            return allCorrect;
        }
    }

    private boolean expectStrategyToHaveAlreadyFinished(CallGraphStrategy strategy) {
        final List<CallPath> emptyCallPathList = Collections.emptyList();
        return strategy.isFinished(emptyCallPathList);
    }

    private boolean checkThatStrategyTraversesAllNodesInCorrectOrder(CallGraphStrategy strategy) throws StrategyUnexpectedStateException {
        boolean isCorrect = true;
        for (GraphNodeWithVulnerablePointsBuilder currentNode : nodesToTraverse) {
            final ArtifactIdentifier artifactIdentifier = obtainArtifactIdentifierForCurrentNode(currentNode);
            letStrategyJumpToNextArtifact(strategy, artifactIdentifier);

            final ArtifactIdentifier strategyActiveArtifact = strategy.getActiveArtifact();
            isCorrect &= strategyActiveArtifact.equals(artifactIdentifier);
        }
        return isCorrect;
    }

    private ArtifactIdentifier obtainArtifactIdentifierForCurrentNode(GraphNodeWithVulnerablePointsBuilder currentNode) {
        final GraphNodeWithVulnerablePointsBuilder.GraphNodePackage currentNodeInformation = currentNode.build();
        final GraphNode currentGraphNode = currentNodeInformation.getGraphNode();
        final Dependency dependency = currentGraphNode.getRelatedDependency();
        return dependency.getArtifactIdentifier();
    }

    private void letStrategyJumpToNextArtifact(CallGraphStrategy strategy, ArtifactIdentifier artifactIdentifier) throws StrategyUnexpectedStateException {
        final FunctionIdentifier arbitraryFunctionInCurrentArtifact = FunctionIdentifierBuilder.aFunctionIdentifier()
                .withArtifactIdentifier(artifactIdentifier)
                .forClass("someClass.class")
                .forFunction("someFunction")
                .build();
        final CallPath callPath = CallPathBuilder.aCallPath()
                .containingFunctionIdentifier(arbitraryFunctionInCurrentArtifact)
                .build();
        final List<CallPath> callPaths = new ArrayList<>();
        callPaths.add(callPath);
        strategy.determineNextCallPath(null, callPaths);
    }

}
