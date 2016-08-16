package castendyck.dependencygraphing.dependencyregistry;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;

import static castendyck.dependencygraphing.dependencyregistry.DependencyRegistryTestBuilder.aDependencyRegistry;
import static castendyck.dependencygraphing.dependencyregistry.DependentArtifactsBuilder.anArtifact;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class DependencyRegistryTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getArtifactsThatDependOn_returnsEmptyListForNoDependentArtifacts() throws Exception {
        ArtifactIdentifier artifact = artifactFor( "someArtifact");
        final DependencyRegistry dependencyRegistry = aDependencyRegistry()
                .with(anArtifact(artifact)
                        .notBeingUsed())
                .build();

        final List<ArtifactIdentifier> artifactsThatDependOn = act(dependencyRegistry, artifact);

        assertThat(artifactsThatDependOn, hasSize(0));
    }

    @Test
    public void getArtifactsThatDependOn_returnsListWithOneElementForOneDependentArtifacts() throws Exception {
        ArtifactIdentifier artifact = artifactFor("someArtifact");
        ArtifactIdentifier dependingArtifact = artifactFor("dependingArtifact");
        final DependencyRegistry dependencyRegistry = aDependencyRegistry()
                .with(anArtifact(artifact)
                        .beingUsedBy(dependingArtifact))
                .build();

        final List<ArtifactIdentifier> artifactsThatDependOn = act(dependencyRegistry, artifact);

        assertThat(artifactsThatDependOn, hasSize(1));
        assertThat(artifactsThatDependOn.get(0), equalTo(dependingArtifact));
    }

    @Test
    public void getArtifactsThatDependOn_returnsListWithThreeElementForThreeDependentArtifacts() throws Exception {
        ArtifactIdentifier artifact = artifactFor("someArtifact");
        final List<ArtifactIdentifier> dependingArtifacts = Arrays.asList(
                artifactFor("dependingArtifact_A"),
                artifactFor("dependingArtifact_B"),
                artifactFor("dependingArtifact_C")
        );
        final DependencyRegistry dependencyRegistry = aDependencyRegistry()
                .with(anArtifact(artifact)
                        .beingUsedBy(dependingArtifacts))
                .build();

        final List<ArtifactIdentifier> artifactsThatDependOn = act(dependencyRegistry, artifact);

        assertThat(artifactsThatDependOn, hasSize(3));
        assertThat(artifactsThatDependOn, equalTo(dependingArtifacts));
    }

    @Test
    public void getArtifactsThatDependOn_throwsErrorForNotExistentArtifact() throws Exception {
        ArtifactIdentifier artifact = artifactFor("someArtifact");
        final DependencyRegistry dependencyRegistry = aDependencyRegistry()
                .withNoData()
                .build();

        expectedException.expect(NotRegisteredArtifactException.class);
        act(dependencyRegistry, artifact);
    }

    private List<ArtifactIdentifier> act(DependencyRegistry dependencyRegistry, ArtifactIdentifier artifact) throws NotRegisteredArtifactException {
        return dependencyRegistry.getArtifactsThatDependOn(artifact);
    }

    private ArtifactIdentifier artifactFor(String artifactName) {
        return ArtifactIdentifierBuilder.buildShort("local", artifactName, "1.0");
    }
}