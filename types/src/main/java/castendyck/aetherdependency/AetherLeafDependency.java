package castendyck.aetherdependency;

import castendyck.aetherdependency.internal.ArtifactIdentifierToRealAetherDependencyMapper;
import castendyck.artifactidentifier.ArtifactIdentifier;
import de.castendyck.enforcing.NotNullConstraintEnforcer;

public class AetherLeafDependency {
    private final ArtifactIdentifier artifactIdentifier;

    private AetherLeafDependency(ArtifactIdentifier artifactIdentifier) {
        NotNullConstraintEnforcer.ensureNotNull(artifactIdentifier);
        this.artifactIdentifier = artifactIdentifier;
    }

    public static AetherLeafDependency forArtifact(ArtifactIdentifier artifactIdentifier) {
        return new AetherLeafDependency(artifactIdentifier);
    }

    public org.eclipse.aether.graph.Dependency getRealAetherDependency() {
        final org.eclipse.aether.graph.Dependency realAetherDependency = ArtifactIdentifierToRealAetherDependencyMapper.getRealAetherDependency(artifactIdentifier);
        return realAetherDependency;
    }
}
