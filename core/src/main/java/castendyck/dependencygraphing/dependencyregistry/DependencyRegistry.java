package castendyck.dependencygraphing.dependencyregistry;

import castendyck.artifactidentifier.ArtifactIdentifier;

import java.util.List;

public interface DependencyRegistry {
    List<ArtifactIdentifier> getArtifactsThatDependOn(ArtifactIdentifier artifactIdentifier) throws NotRegisteredArtifactException;
}
