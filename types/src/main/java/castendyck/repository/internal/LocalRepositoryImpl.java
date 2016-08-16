package castendyck.repository.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.repository.LocalRepository;

import java.nio.file.Path;

public class LocalRepositoryImpl implements LocalRepository {
    private final Path path;

    public LocalRepositoryImpl(Path path) {
        NotNullConstraintEnforcer.ensureNotNull(path);
        this.path = path;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public Path potentialJarFileLocationFor(ArtifactIdentifier artifact) {
        final Path jarLocation = resolve(artifact, "jar");
        return jarLocation;
    }

    @Override
    public Path potentialPomFileLocationFor(ArtifactIdentifier artifact) {
        final Path pomLocation = resolve(artifact, "pom");
        return pomLocation;
    }

    private Path resolve(ArtifactIdentifier artifactIdentifier, String fileType) {
        final String groupId = artifactIdentifier.getGroupId();
        String groupRelativePath = groupId.replace(".", "/");
        final String artifactId = artifactIdentifier.getArtifactId();
        final String version = artifactIdentifier.getVersion();
        String fileName = artifactId + "-" + version + "." + fileType;

        final Path path = this.path.resolve(groupRelativePath)
                .resolve(artifactId)
                .resolve(version)
                .resolve(fileName);
        return path;
    }
}
