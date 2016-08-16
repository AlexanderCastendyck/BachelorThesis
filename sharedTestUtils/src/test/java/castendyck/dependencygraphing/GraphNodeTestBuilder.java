package castendyck.dependencygraphing;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.dependency.DependencyFactory;
import castendyck.dependencygraph.GraphNode;
import castendyck.dependencygraph.GraphNodeBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GraphNodeTestBuilder {
    private final List<GraphNodeTestBuilder> childrenBuilder = new ArrayList<>();
    private ArtifactIdentifier artifactIdentifier;

    public static GraphNodeTestBuilder aGraphNode() {
        return new GraphNodeTestBuilder();
    }

    public GraphNodeTestBuilder forArtifact(String group, String artifact, String version) {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort(group, artifact, version);
        return forArtifact(artifactIdentifier);
    }

    public GraphNodeTestBuilder forArtifact(ArtifactIdentifier artifactIdentifier) {
        this.artifactIdentifier = artifactIdentifier;
        return this;
    }

    public GraphNodeTestBuilder withChildren(GraphNodeTestBuilder... children) {
        Arrays.stream(children)
                .forEach(this.childrenBuilder::add);
        return this;
    }


    public GraphNode build() {
        List<GraphNode> children = childrenBuilder.stream()
                .map(GraphNodeTestBuilder::build)
                .collect(Collectors.toList());

        GraphNode graphNode = GraphNodeBuilder.aGraphNode()
                .withDependency(DependencyFactory.dependencyFor(artifactIdentifier))
                .withChildren(children)
                .build();
        return graphNode;
    }
}
