package castendyck.repository;

import castendyck.artifactidentifier.ArtifactIdentifier;

import java.nio.file.Path;

public interface LocalRepository {
    Path getPath();

    Path potentialJarFileLocationFor(ArtifactIdentifier artifact);

    Path potentialPomFileLocationFor(ArtifactIdentifier artifact);
}
