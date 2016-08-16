package castendyck.vulnerablepoint;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.callgraph.JarFileFromByteCodeBuilder;
import castendyck.dependency.Dependency;
import castendyck.repository.LocalRepositoryFactory;
import castendyck.temporarypomfile.TemporaryPomFileBuilder;
import castendyck.repository.LocalRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalStoredArtifactBuilder {
    private LocalRepository localRepository;
    private ArtifactIdentifier artifactIdentifier;
    private final List<Dependency> dependencies = new ArrayList<>();

    public static LocalStoredArtifactBuilder aLocallyInstalledArtifactIdentifier(){
        return new LocalStoredArtifactBuilder();
    }

    public LocalStoredArtifactBuilder forArtifact(String groupId, String artifactId, String version) {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort(groupId, artifactId, version);
        return forArtifact(artifactIdentifier);
    }

    public LocalStoredArtifactBuilder forArtifact(ArtifactIdentifier artifactIdentifier){
        this.artifactIdentifier = artifactIdentifier;
        return this;
    }

    public LocalStoredArtifactBuilder withDependencies(Dependency ... dependencies){
        Arrays.stream(dependencies)
                .forEach(this::withDependency);
        return this;
    }

    private LocalStoredArtifactBuilder withDependency(Dependency dependency) {
        this.dependencies.add(dependency);
        return this;
    }

    public LocalStoredArtifactBuilder withNoDependecies() {
        this.dependencies.clear();
        return this;
    }

    public LocalStoredArtifactBuilder inRepo(Path repositoryRoot) {
        final LocalRepository localRepository = LocalRepositoryFactory.createNewInPath(repositoryRoot);
        return inRepo(localRepository);
    }

    public LocalStoredArtifactBuilder inRepo(LocalRepository localRepository){
        this.localRepository = localRepository;
        return this;
    }

    public ArtifactIdentifier build() throws IOException {
        final String artifactId = artifactIdentifier.getArtifactId();
        final String version = artifactIdentifier.getVersion();
        final String name = artifactId+"-"+version;

        final String groupId = artifactIdentifier.getGroupId();
        final String groupIdPath = groupId.replace(".", "/");
        final Path directory = localRepository.getPath()
                .resolve(groupIdPath)
                .resolve(artifactId)
                .resolve(version);

        TemporaryPomFileBuilder.aPomFile()
                .inDirectory(directory)
                .forArtifact(artifactIdentifier)
                .withDependencies(dependencies)
                .named(name + ".pom")
                .build();

        final Path jarFile = directory.resolve(name + ".jar");
        JarFileFromByteCodeBuilder.aJarFile()
                .storedAs(jarFile)
                .build();


        return artifactIdentifier;
    }
}
