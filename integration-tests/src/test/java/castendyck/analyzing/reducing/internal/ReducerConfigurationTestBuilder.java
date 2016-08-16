package castendyck.analyzing.reducing.internal;

import castendyck.analyzing.reducing.ReducerConfiguration;
import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistry;
import castendyck.maventestpathidentifier.MavenTestPathIdentifier;
import castendyck.maven.pomfile.PomFile;
import org.mockito.Mockito;

import java.nio.file.Path;

import static castendyck.analyzing.reducing.internal.MockedPomFileBuilder.aPomFile;

public class ReducerConfigurationTestBuilder {
    private MockedPomFileBuilder mockedPomFileBuilder;
    private MavenTestPathIdentifier generalMavenTestPathPart;
    private Path projectRootPath;

    public ReducerConfigurationTestBuilder() {
        projectRootPath = Mockito.mock(Path.class);
        generalMavenTestPathPart = Mockito.mock(MavenTestPathIdentifier.class);
        mockedPomFileBuilder = MockedPomFileBuilder.aPomFile();
    }

    public static ReducerConfigurationTestBuilder aConfiguration() {
        return new ReducerConfigurationTestBuilder();
    }

    public static ReducerConfiguration aSimpleConfigurationFor(ArtifactIdentifier artifactIdentifier) {
        final ReducerConfiguration configuration = new ReducerConfigurationTestBuilder()
                .with(aPomFile()
                        .forArtifact(artifactIdentifier))
                .build();
        return configuration;
    }

    public ReducerConfigurationTestBuilder with(MockedPomFileBuilder mockedPomFileBuilder) {
        this.mockedPomFileBuilder = mockedPomFileBuilder;
        return this;
    }

    public ReducerConfigurationTestBuilder withProjectRootPath(Path projectRootPath) {
        this.projectRootPath = projectRootPath;
        return this;
    }

    public ReducerConfigurationTestBuilder withGeneralMavenTestPathPart(String generalMavenTestPathPart) {
        final MavenTestPathIdentifier mavenTestPathIdentifier = MavenTestPathIdentifier.parse(generalMavenTestPathPart);
        return withGeneralMavenTestPathPart(mavenTestPathIdentifier);
    }
    public ReducerConfigurationTestBuilder withGeneralMavenTestPathPart(MavenTestPathIdentifier generalMavenTestPathPart) {
        this.generalMavenTestPathPart = generalMavenTestPathPart;
        return this;
    }

    public ReducerConfiguration build() {
        final PomFile pomFile = mockedPomFileBuilder.build();
        final DependencyRegistry dependencyRegistry = Mockito.mock(DependencyRegistry.class);
        final SourceCodeRegistry sourceCodeRegistry = Mockito.mock(SourceCodeRegistry.class);
        final ReducerConfiguration configuration = new ReducerConfiguration(pomFile, dependencyRegistry, sourceCodeRegistry, projectRootPath, generalMavenTestPathPart);
        return configuration;
    }
}
