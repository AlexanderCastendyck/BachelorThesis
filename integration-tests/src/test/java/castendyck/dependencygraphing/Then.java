package castendyck.dependencygraphing;

import castendyck.dependencygraph.DependencyGraph;

import java.io.IOException;

import static castendyck.dependencygraphing.DependencyGraphTestBuilder.aDependencyGraph;
import static castendyck.dependencygraphing.DependencyGraphMatcher.matches;
import static castendyck.dependencygraphing.GraphNodeTestBuilder.aGraphNode;
import static org.junit.Assert.assertThat;

public class Then {
    private final DependencyGraph dependencyGraph;

    public Then(DependencyGraph dependencyGraph) {
        this.dependencyGraph = dependencyGraph;
    }

    public void thenGraphShouldContainOnlyOneRootNode() throws IOException {
        assertThat(dependencyGraph, matches(aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact("someGroup", "someArtifact", "1.0"))));

    }

    public void thenGraphShouldContainRootNodeAndTwoChildren() throws IOException {
        assertThat(dependencyGraph, matches(aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact("someGroup", "someArtifact", "1.0")
                        .withChildren(
                                aGraphNode()
                                        .forArtifact("groupA", "artifactA", "1.0"),
                                aGraphNode()
                                        .forArtifact("groupB", "artifactB", "1.0")))));

    }


    public void thenGraphShouldContainRootNodeOneDirectChildAndOneChildChild() throws IOException {
        assertThat(dependencyGraph, matches(aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact("someGroup", "someArtifact", "1.0")
                        .withChildren(
                                aGraphNode()
                                        .forArtifact("junit", "junit", "4.12")
                                        .withChildren(
                                                aGraphNode()
                                                        .forArtifact("org.hamcrest", "hamcrest-core", "1.3"))))));

    }

    public void thenGraphShouldContainAllNodesCorrectly() throws IOException {
        assertThat(dependencyGraph, matches(aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact("someGroup", "someArtifact", "1.0")
                        .withChildren(
                                aGraphNode()
                                        .forArtifact("junit", "junit", "4.12")
                                        .withChildren(
                                                aGraphNode()
                                                        .forArtifact("org.hamcrest", "hamcrest-core", "1.3")),
                                aGraphNode()
                                        .forArtifact("org.eclipse.aether", "aether-transport-http", "1.1.0")
                                        .withChildren(aGraphNode()
                                                        .forArtifact("org.eclipse.aether", "aether-api", "1.1.0"),
                                                aGraphNode()
                                                        .forArtifact("org.eclipse.aether", "aether-spi", "1.1.0")
                                                        .withChildren(aGraphNode()
                                                                .forArtifact("org.eclipse.aether", "aether-api", "1.1.0")),
                                                aGraphNode()
                                                        .forArtifact("org.eclipse.aether", "aether-util", "1.1.0")
                                                        .withChildren(aGraphNode()
                                                                .forArtifact("org.eclipse.aether", "aether-api", "1.1.0")),
                                                aGraphNode()
                                                        .forArtifact("org.apache.httpcomponents", "httpclient", "4.3.5")
                                                        .withChildren(aGraphNode()
                                                                        .forArtifact("org.apache.httpcomponents", "httpcore", "4.3.2"),
                                                                aGraphNode()
                                                                        .forArtifact("commons-codec", "commons-codec", "1.6")),
                                                aGraphNode()
                                                        .forArtifact("org.slf4j", "jcl-over-slf4j", "1.6.2")
                                                        .withChildren(aGraphNode()
                                                                .forArtifact("org.slf4j", "slf4j-api", "1.6.2")))))));

    }

    public void thenGraphShouldContainBothDependenciesAsSingleNodes() throws IOException {
        assertThat(dependencyGraph, matches(aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact("someGroup", "someArtifact", "1.0")
                        .withChildren(
                                aGraphNode()
                                        .forArtifact("junit", "junit", "4.12")
                                        .withChildren(
                                                aGraphNode()
                                                        .forArtifact("org.hamcrest", "hamcrest-core", "1.3")),
                                aGraphNode()
                                        .forArtifact("junit", "junit", "4.10")
                                        .withChildren(
                                                aGraphNode()
                                                        .forArtifact("org.hamcrest", "hamcrest-core", "1.1"))))));

    }

    public void thenGraphShouldContainOneGraphWithAllSubModulesAndTheirDependencies() throws IOException {
        assertThat(dependencyGraph, matches(aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact("someGroup", "someArtifact", "1.0")
                        .withChildren(aGraphNode()
                                        .forArtifact("someGroupA", "someArtifactA", "1.0")
                                        .withChildren(aGraphNode()
                                                .forArtifact("junit", "junit", "4.12")
                                                .withChildren(aGraphNode()
                                                        .forArtifact("org.hamcrest", "hamcrest-core", "1.3"))),
                                aGraphNode()
                                        .forArtifact("someGroupB", "someArtifactB", "1.0")
                                        .withChildren(aGraphNode()
                                                .forArtifact("junit", "junit", "4.10")
                                                .withChildren(
                                                        aGraphNode()
                                                                .forArtifact("org.hamcrest", "hamcrest-core", "1.1")))))));

    }

    public void thenGraphShouldContainOneGraphWithAllSubModulesAndTheirSubModules() throws IOException {
        assertThat(dependencyGraph, matches(aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact("someGroup", "someArtifact", "1.0")
                        .withChildren(aGraphNode()
                                        .forArtifact("someGroup", "subModule1", "1.0")
                                        .withChildren(aGraphNode()
                                                        .forArtifact("someGroup", "subModule1.1", "1.0")
                                                        .withChildren(aGraphNode()
                                                                .forArtifact("junit", "junit", "4.12")
                                                                .withChildren(aGraphNode()
                                                                        .forArtifact("org.hamcrest", "hamcrest-core", "1.3"))),
                                                aGraphNode()
                                                        .forArtifact("someGroup", "subModule1.2", "1.0")),
                                aGraphNode()
                                        .forArtifact("someGroup", "subModule2", "1.0")
                                        .withChildren(aGraphNode()
                                                .forArtifact("someGroup", "subModule2.1", "1.0")
                                                .withChildren(aGraphNode()
                                                        .forArtifact("org.mockito", "mockito-all", "1.10.19")))))));

    }

    public void thenGraphShouldContainOneGraphWithAllSubModulesAndTheirDependenciesAndCrossReferencedOnes() {
        assertThat(dependencyGraph, matches(aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact("someGroup", "someArtifact", "1.0")
                        .withChildren(aGraphNode()
                                        .forArtifact("someGroup", "someArtifactA", "1.0")
                                        .withChildren(aGraphNode()
                                                .forArtifact("junit", "junit", "4.12")
                                                .withChildren(aGraphNode()
                                                        .forArtifact("org.hamcrest", "hamcrest-core", "1.3"))),
                                aGraphNode()
                                        .forArtifact("someGroup", "someArtifactB", "1.0")
                                        .withChildren(aGraphNode()
                                                .forArtifact("someGroup", "someArtifactA", "1.0")
                                                .withChildren(aGraphNode()
                                                        .forArtifact("junit", "junit", "4.12")
                                                        .withChildren(aGraphNode()
                                                                .forArtifact("org.hamcrest", "hamcrest-core", "1.3"))))))));
    }

    public void thenGraphShouldContainOneGraphWithAllSubModulesAndTheirDependenciesAndCrossReferencedOnesAndTheirSubModules() {
        assertThat(dependencyGraph, matches(aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact("someGroup", "someArtifact", "1.0")
                        .withChildren(
                                aGraphNode()
                                        .forArtifact("someGroup", "subModule2", "1.0")
                                        .withChildren(aGraphNode()
                                                .forArtifact("someGroup", "subModule1", "1.0")
                                                .withChildren(
                                                        aGraphNode()
                                                                .forArtifact("someGroup", "subModule1.1", "1.0")
                                                                .withChildren(aGraphNode()
                                                                        .forArtifact("junit", "junit", "4.12")
                                                                        .withChildren(aGraphNode()
                                                                                .forArtifact("org.hamcrest", "hamcrest-core", "1.3"))),
                                                        aGraphNode()
                                                                .forArtifact("someGroup", "subModule1.2", "1.0"))),
                                aGraphNode()
                                        .forArtifact("someGroup", "subModule1", "1.0")
                                        .withChildren(
                                                aGraphNode()
                                                        .forArtifact("someGroup", "subModule1.1", "1.0")
                                                        .withChildren(aGraphNode()
                                                                .forArtifact("junit", "junit", "4.12")
                                                                .withChildren(aGraphNode()
                                                                        .forArtifact("org.hamcrest", "hamcrest-core", "1.3"))),
                                                aGraphNode()
                                                        .forArtifact("someGroup", "subModule1.2", "1.0"))))));
    }

    public void thenGraphShouldContainTestDependencies() throws IOException {
        assertThat(dependencyGraph, matches(aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact("someGroup", "someArtifact", "1.0")
                        .withChildren(
                                aGraphNode()
                                        .forArtifact("junit", "junit", "4.12")
                                        .withChildren(
                                                aGraphNode()
                                                        .forArtifact("org.hamcrest", "hamcrest-core", "1.3"))))));
    }
}
