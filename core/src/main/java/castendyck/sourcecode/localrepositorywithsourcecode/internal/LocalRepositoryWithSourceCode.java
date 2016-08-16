package castendyck.sourcecode.localrepositorywithsourcecode.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.repository.LocalRepository;

import java.nio.file.Path;
import java.util.Map;

public class LocalRepositoryWithSourceCode implements LocalRepository {
    private final LocalRepository mavenLocalRepository;
    private final Map<ArtifactIdentifier, Path> sourceCodeJars;

    public LocalRepositoryWithSourceCode(LocalRepository mavenLocalRepository, Map<ArtifactIdentifier, Path> sourceCodeJars) {
        NotNullConstraintEnforcer.ensureNotNull(mavenLocalRepository);
        this.mavenLocalRepository = mavenLocalRepository;
        NotNullConstraintEnforcer.ensureNotNull(sourceCodeJars);
        this.sourceCodeJars = sourceCodeJars;
    }

    @Override
    public Path getPath() {
        return mavenLocalRepository.getPath();
    }

    @Override
    public Path potentialJarFileLocationFor(ArtifactIdentifier artifact) {
        if (sourceCodeJars.containsKey(artifact)) {
            final Path sourceCodeJarPath = sourceCodeJars.get(artifact);
            final Path absolutePath = sourceCodeJarPath.toAbsolutePath();
            return absolutePath;
        } else {
            final Path potentialJarFileLocationFor = mavenLocalRepository.potentialJarFileLocationFor(artifact);
            return potentialJarFileLocationFor;
        }
    }

    @Override
    public Path potentialPomFileLocationFor(ArtifactIdentifier artifact) {
        return mavenLocalRepository.potentialPomFileLocationFor(artifact);
    }
}
