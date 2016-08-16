package castendyck.dependency.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.dependency.Dependency;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import org.eclipse.aether.artifact.Artifact;

public class DependencyImpl implements Dependency {
    private final ArtifactIdentifier artifactIdentifier;

    private DependencyImpl(ArtifactIdentifier artifactIdentifier) {
        NotNullConstraintEnforcer.ensureNotNull(artifactIdentifier);
        this.artifactIdentifier = artifactIdentifier;
    }

    public static DependencyImpl forArtifact(ArtifactIdentifier artifactIdentifier) {
        return new DependencyImpl(artifactIdentifier);
    }


    public static Dependency forAetherDependency(org.eclipse.aether.graph.Dependency dependency) {
        final Artifact artifact = dependency.getArtifact();
        final String groupId = artifact.getGroupId();
        final String artifactId = artifact.getArtifactId();
        final String version = artifact.getVersion();

        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.anArtifactIdentifier()
                .withGroupId(groupId)
                .withArtifactId(artifactId)
                .withVersion(version)
                .build();

        return new DependencyImpl(artifactIdentifier);
    }

    @Override
    public ArtifactIdentifier getArtifactIdentifier() {
        return artifactIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final DependencyImpl that = (DependencyImpl) o;

        return artifactIdentifier.equals(that.artifactIdentifier);

    }

    @Override
    public int hashCode() {
        return artifactIdentifier.hashCode();
    }

    @Override
    public String toString() {
        return "DependencyImpl{" +
                "artifactIdentifier=" + artifactIdentifier +
                '}';
    }
}
