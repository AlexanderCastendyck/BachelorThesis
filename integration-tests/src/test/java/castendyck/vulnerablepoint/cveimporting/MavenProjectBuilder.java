package castendyck.vulnerablepoint.cveimporting;

import castendyck.artifactidentifier.ArtifactIdentifier;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.project.MavenProject;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

public class MavenProjectBuilder {
    private final String name;
    private final List<ArtifactIdentifier> artifacts;
    private Path repositoryRootDirectory;

    public MavenProjectBuilder(String name) {
        this.name = name;
        this.artifacts = new ArrayList<>();
    }

    public static MavenProjectBuilder aMavenProject(String name){
        return new MavenProjectBuilder(name);
    }

    public MavenProjectBuilder withArtifacts(List<ArtifactIdentifier> artifacts){
        artifacts.stream()
                .forEach(this::withArtifact);
        return this;
    }

    public MavenProjectBuilder withArtifact(ArtifactIdentifier artifactIdentifier) {
        artifacts.add(artifactIdentifier);
        return this;
    }

    public MavenProjectBuilder withNoArtifact() {
        artifacts.clear();
        return this;
    }

    public MavenProjectBuilder withRepositoryRootDirectory(Path repositoryRootDirectory){
        this.repositoryRootDirectory = repositoryRootDirectory;
        return this;
    }

    public MavenProject build(){
        MavenProject mavenProject = Mockito.mock(MavenProject.class);
        Set<Artifact> mavenArtifacts = artifacts.stream()
                .map(this::mapToMavenArtifact)
                .collect(Collectors.toSet());

        when(mavenProject.getArtifacts()).thenReturn(mavenArtifacts);
        return mavenProject;
    }

    private Artifact mapToMavenArtifact(ArtifactIdentifier artifactIdentifier) {
        final String groupId = artifactIdentifier.getGroupId();
        final String artifactId = artifactIdentifier.getArtifactId();
        final String version = artifactIdentifier.getVersion();
        final DefaultArtifactHandler artifactHandler = new DefaultArtifactHandler();
        Artifact artifact = new DefaultArtifact(groupId, artifactId, version, "runtime", "jar", null, artifactHandler);
        artifact.setFile(locateFileFor(artifactIdentifier));
        return artifact;
    }

    private File locateFileFor(ArtifactIdentifier artifactIdentifier) {
        final String groupId = artifactIdentifier.getGroupId();
        final String artifactId = artifactIdentifier.getArtifactId();
        final String version = artifactIdentifier.getVersion();

        final String relativePathOfGroupInRepo = groupId.replace(".", "/");
        final Path absolutePathOfGroup = repositoryRootDirectory.resolve(relativePathOfGroupInRepo);

        final Path specificArtifactIdPath = absolutePathOfGroup.resolve(artifactId);
        final Path pathOfExactVersion = specificArtifactIdPath.resolve(version);

        final String jarFileName = artifactId + "-" + version + ".jar";
        final Path pathToArtifact = pathOfExactVersion.resolve(jarFileName);

        return pathToArtifact.toFile();
    }

}
