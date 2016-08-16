package castendyck.dependencygraphing.artifactsindgraphcollecting;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraphing.DependencyGraphTestBuilder;
import castendyck.dependencygraphing.GraphNodeTestBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static castendyck.artifactidentifier.ArtifactIdentifierBuilder.buildShort;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ArtifactsInDependencyGraphCollectorTest {
    private ArtifactsInDependencyGraphCollector artifactsInDependencyGraphCollector = ArtifactsInDependencyGraphCollectorFactory.newInstance();

    @Test
    public void collectArtifactsThatAreInDependencyGraph_foundsNoneForNoSearchedArtifact() throws Exception {
        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(GraphNodeTestBuilder.aGraphNode()
                        .forArtifact(buildShort("someGroup", "someArtifact", "1.0")))
                .build();
        final List<ArtifactIdentifier> foundArtifacts = artifactsInDependencyGraphCollector.collectArtifactsThatAreInDependencyGraph(dependencyGraph, Collections.emptyList());
        assertThat(foundArtifacts, hasSize(0));
    }

    @Test
    public void collectArtifactsThatAreInDependencyGraph_foundsSingleOne() throws Exception {
        final ArtifactIdentifier artifactIdentifier = buildShort("someGroup", "someArtifact", "1.0");
        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(GraphNodeTestBuilder.aGraphNode()
                        .forArtifact(artifactIdentifier))
                .build();
        final List<ArtifactIdentifier> artifactsToCheck = asList(artifactIdentifier);
        final List<ArtifactIdentifier> foundArtifacts = artifactsInDependencyGraphCollector.collectArtifactsThatAreInDependencyGraph(dependencyGraph, artifactsToCheck);
        assertThat(foundArtifacts, hasSize(1));
        assertThat(foundArtifacts, contains(artifactIdentifier));
    }

    @Test
    public void collectArtifactsThatAreInDependencyGraph_foundsSingleOneInChild() throws Exception {
        final ArtifactIdentifier artifactIdentifier = buildShort("someGroup", "someArtifact", "1.0");
        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(GraphNodeTestBuilder.aGraphNode()
                        .forArtifact("someGroup", "rootNode", "1.0")
                        .withChildren(GraphNodeTestBuilder.aGraphNode()
                                .forArtifact("someGroup", "intermediateNode", "1.0")
                                .withChildren(GraphNodeTestBuilder.aGraphNode()
                                        .forArtifact(artifactIdentifier))))
                .build();
        final List<ArtifactIdentifier> artifactsToCheck = asList(artifactIdentifier);
        final List<ArtifactIdentifier> foundArtifacts = artifactsInDependencyGraphCollector.collectArtifactsThatAreInDependencyGraph(dependencyGraph, artifactsToCheck);
        assertThat(foundArtifacts, hasSize(1));
        assertThat(foundArtifacts, contains(artifactIdentifier));
    }

    @Test
    public void collectArtifactsThatAreInDependencyGraph_findsAll() throws Exception {
        final ArtifactIdentifier searchedArtifact_1 = buildShort("someGroup", "searchedArtifact_1", "1.0");
        final ArtifactIdentifier searchedArtifact_2 = buildShort("someGroup", "searchedArtifact_2", "1.0");
        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(GraphNodeTestBuilder.aGraphNode()
                        .forArtifact("someGroup", "rootNode", "1.0")
                        .withChildren(GraphNodeTestBuilder.aGraphNode()
                                        .forArtifact(searchedArtifact_2)
                                        .withChildren(GraphNodeTestBuilder.aGraphNode()
                                                .forArtifact(searchedArtifact_1)),
                                GraphNodeTestBuilder.aGraphNode()
                                        .forArtifact("someGroup", "intermediateNode", "1.0")
                                        .withChildren(GraphNodeTestBuilder.aGraphNode()
                                                .forArtifact("someGroup", "someNode", "1.0"))))
                .build();
        final List<ArtifactIdentifier> artifactsToCheck = asList(searchedArtifact_1,
                searchedArtifact_2);
        final List<ArtifactIdentifier> foundArtifacts = artifactsInDependencyGraphCollector.collectArtifactsThatAreInDependencyGraph(dependencyGraph, artifactsToCheck);
        assertThat(foundArtifacts, hasSize(2));
        assertThat(foundArtifacts, containsInAnyOrder(searchedArtifact_1, searchedArtifact_2));
    }

    @Test
    public void collectArtifactsThatAreInDependencyGraph_doesNotReturnArtifactsNotContained() throws Exception {
        final ArtifactIdentifier artifactContained = buildShort("someGroup", "searchedArtifact_1", "1.0");
        final ArtifactIdentifier artifactNotContained = buildShort("someGroup", "searchedArtifact_2", "1.0");
        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(GraphNodeTestBuilder.aGraphNode()
                        .forArtifact("someGroup", "rootNode", "1.0")
                        .withChildren(GraphNodeTestBuilder.aGraphNode()
                                .forArtifact("someGroup", "intermediateNode", "1.0")
                                .withChildren(GraphNodeTestBuilder.aGraphNode()
                                        .forArtifact(artifactContained))))
                .build();
        final List<ArtifactIdentifier> artifactsToCheck = asList(
                artifactContained,
                artifactNotContained);
        final List<ArtifactIdentifier> foundArtifacts = artifactsInDependencyGraphCollector.collectArtifactsThatAreInDependencyGraph(dependencyGraph, artifactsToCheck);
        assertThat(foundArtifacts, hasSize(1));
        assertThat(foundArtifacts, contains(artifactContained));
    }

    private List<ArtifactIdentifier> asList(ArtifactIdentifier... artifactIdentifiers) {
        final List<ArtifactIdentifier> list = Arrays.stream(artifactIdentifiers)
                .collect(Collectors.toList());
        return list;
    }

}