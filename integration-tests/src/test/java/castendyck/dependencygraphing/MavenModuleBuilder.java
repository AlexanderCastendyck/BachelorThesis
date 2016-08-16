package castendyck.dependencygraphing;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.mavendependency.MavenDependency;
import castendyck.mavendependency.MavenDependencyBuilder;
import castendyck.temporarypomfile.TemporaryPomFileBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MavenModuleBuilder {
    private ArtifactIdentifier artifact;
    private List<MavenDependency> dependencies = new ArrayList<>();
    private Path parentDirectory;

    public static MavenModuleBuilder aMavenModule() {
        return new MavenModuleBuilder();
    }

    public MavenModuleBuilder forArtifact(String groupId, String artifactId, String version) {
        artifact = ArtifactIdentifierBuilder.buildShort(groupId, artifactId, version);
        return this;
    }

    public MavenModuleBuilder withDependency(ArtifactIdentifier artifactIdentifier) {
        final MavenDependency mavenDependency = MavenDependencyBuilder.aMavenDependency()
                .forArtifact(artifactIdentifier)
                .withScope("compile")
                .build();
        return withDependency(mavenDependency);
    }
    public MavenModuleBuilder withDependency(MavenDependency dependency) {
        dependencies.add(dependency);
        return this;
    }

    public String getArtifactName() {
        return artifact.getArtifactId();
    }

    public void setParentDirectory(Path parentDirectory) {
        this.parentDirectory = parentDirectory;
    }

    public File build() throws IOException {
        final String artifactId = artifact.getArtifactId();
        final Path workingDirectory = parentDirectory.resolve(artifactId);
        final File file = TemporaryPomFileBuilder.aPomFile()
                .forArtifact(artifact)
                .withMavenDependencies(dependencies)
                .inDirectory(workingDirectory)
                .build();
        return file;
    }
}
