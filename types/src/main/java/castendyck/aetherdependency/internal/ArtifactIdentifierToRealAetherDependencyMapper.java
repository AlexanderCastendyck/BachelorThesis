package castendyck.aetherdependency.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;

public class ArtifactIdentifierToRealAetherDependencyMapper {

    public static org.eclipse.aether.graph.Dependency getRealAetherDependency(ArtifactIdentifier artifactIdentifier) {
        Artifact artifact = mapToAetherArtifact(artifactIdentifier);
        final String scope = null;
        org.eclipse.aether.graph.Dependency aetherDependency = new org.eclipse.aether.graph.Dependency(artifact, scope);
        return aetherDependency;
    }

    private static DefaultArtifact mapToAetherArtifact(ArtifactIdentifier artifactIdentifier) {
        final String groupId = artifactIdentifier.getGroupId();
        final String artifactId = artifactIdentifier.getArtifactId();
        final String extension = null;
        final String version = artifactIdentifier.getVersion();
        return new DefaultArtifact(groupId, artifactId, extension, version);
    }
}
