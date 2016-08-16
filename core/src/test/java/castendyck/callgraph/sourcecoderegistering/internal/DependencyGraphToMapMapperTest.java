package castendyck.callgraph.sourcecoderegistering.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.dependencygraphing.DependencyGraphTestBuilder;
import castendyck.dependencygraphing.GraphNodeTestBuilder;
import castendyck.dependencygraphing.dependencyregistry.internal.DependencyGraphToMapMapper;
import castendyck.dependencygraph.DependencyGraph;
import org.junit.Test;

import java.util.*;

import static de.castendyck.collections.EntryBuilder.anEntry;
import static de.castendyck.collections.MapBuilder.aMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DependencyGraphToMapMapperTest {


    @Test
    public void map_mapsOneNodeToOneEntryWithNoDependentArtifacts() throws Exception {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("local", "someArtifact", "1.0");
        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(GraphNodeTestBuilder.aGraphNode()
                        .forArtifact(artifactIdentifier))
                .build();

        final Map<ArtifactIdentifier, List<ArtifactIdentifier>> map = act(dependencyGraph);

        final Map expectedMap = aMap().withAnEntry(anEntry()
                        .withKey(artifactIdentifier)
                        .withValue(Collections.emptyList()))
                .build();
        assertThat(map, equalTo(expectedMap));
    }

    @Test
    public void map_mapsParentToADependingArtifact() throws Exception {
        final ArtifactIdentifier rootArtifact = ArtifactIdentifierBuilder.buildShort("local", "someArtifact", "1.0");
        final ArtifactIdentifier artifactChild1 = ArtifactIdentifierBuilder.buildShort("local", "child1", "1.0");
        final ArtifactIdentifier artifactChild2 = ArtifactIdentifierBuilder.buildShort("local", "child2", "1.0");
        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(GraphNodeTestBuilder.aGraphNode()
                        .forArtifact(rootArtifact)
                        .withChildren(
                                GraphNodeTestBuilder.aGraphNode().forArtifact(artifactChild1),
                                GraphNodeTestBuilder.aGraphNode().forArtifact(artifactChild2)))
                .build();

        final Map<ArtifactIdentifier, List<ArtifactIdentifier>> map = act(dependencyGraph);

        final Map expectedMap = aMap()
                .withAnEntry(anEntry()
                        .withKey(rootArtifact)
                        .withValue(Collections.emptyList()))
                .withAnEntry(anEntry()
                        .withKey(artifactChild1)
                        .withValue(listFor(rootArtifact)))
                .withAnEntry(anEntry()
                        .withKey(artifactChild2)
                        .withValue(listFor(rootArtifact)))
                .build();
        assertThat(map, equalTo(expectedMap));
    }

    @Test
    public void map_findsSeveralDependingArtifacts() throws Exception {
        final ArtifactIdentifier rootArtifact = ArtifactIdentifierBuilder.buildShort("local", "root", "1.0");
        final ArtifactIdentifier artifactChild1 = ArtifactIdentifierBuilder.buildShort("local", "child1", "1.0");
        final ArtifactIdentifier artifactChild2 = ArtifactIdentifierBuilder.buildShort("local", "child2", "1.0");
        final ArtifactIdentifier artifact = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(GraphNodeTestBuilder.aGraphNode()
                        .forArtifact(rootArtifact)
                        .withChildren(
                                GraphNodeTestBuilder.aGraphNode()
                                        .forArtifact(artifactChild1)
                                        .withChildren(GraphNodeTestBuilder.aGraphNode()
                                                .forArtifact(artifact)),
                                GraphNodeTestBuilder.aGraphNode()
                                        .forArtifact(artifactChild2)
                                        .withChildren(GraphNodeTestBuilder.aGraphNode()
                                                .forArtifact(artifact))))
                .build();

        final Map<ArtifactIdentifier, List<ArtifactIdentifier>> map = act(dependencyGraph);

        final Map expectedMap = aMap()
                .withAnEntry(anEntry()
                        .withKey(rootArtifact)
                        .withValue(Collections.emptyList()))
                .withAnEntry(anEntry()
                        .withKey(artifactChild1)
                        .withValue(listFor(rootArtifact)))
                .withAnEntry(anEntry()
                        .withKey(artifactChild2)
                        .withValue(listFor(rootArtifact)))
                .withAnEntry(anEntry()
                        .withKey(artifact)
                        .withValue(listFor(artifactChild1, artifactChild2)))
                .build();

        assertThat(map, equalTo(expectedMap));
    }

    private Map<ArtifactIdentifier, List<ArtifactIdentifier>> act(DependencyGraph dependencyGraph) {
        return DependencyGraphToMapMapper.mapToMap(dependencyGraph);
    }

    private List<ArtifactIdentifier> listFor(ArtifactIdentifier... artifactIdentifiers) {
        List<ArtifactIdentifier> list = new ArrayList<>();
        Arrays.asList(artifactIdentifiers)
                .forEach(list::add);

        return list;
    }

}