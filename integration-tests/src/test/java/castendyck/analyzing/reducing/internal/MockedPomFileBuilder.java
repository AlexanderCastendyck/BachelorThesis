package castendyck.analyzing.reducing.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.mavendependency.MavenDependency;
import castendyck.mavendependency.MavenDependencyBuilder;
import castendyck.maven.pomfile.PomFile;
import castendyck.maven.pomfile.PomFileCreationException;
import castendyck.pomfilelocator.PomFileLocationException;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

public class MockedPomFileBuilder {
    private final List<MavenDependency> dependencies;
    private final List<MockedPomFileBuilder> subModules;
    private ArtifactIdentifier artifactIdentifier;

    public MockedPomFileBuilder() {
        this.dependencies = new ArrayList<>();
        this.subModules = new ArrayList<>();
    }

    public static MockedPomFileBuilder aPomFile() {
        return new MockedPomFileBuilder();
    }
    public static MockedPomFileBuilder aSubModule() {
        return new MockedPomFileBuilder();
    }

    public MockedPomFileBuilder forArtifact(ArtifactIdentifier artifactIdentifier){
        this.artifactIdentifier = artifactIdentifier;
        return this;
    }

    public MockedPomFileBuilder withADependency(String groupId, String artifactId, String version) {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort(groupId, artifactId, version);
        return withADependency(artifactIdentifier, "test");
    }

    public MockedPomFileBuilder withADependency(ArtifactIdentifier artifactIdentifier, String scope) {
        final MavenDependency dependency = MavenDependencyBuilder.aMavenDependency()
                .forArtifact(artifactIdentifier)
                .withScope(scope)
                .build();
        dependencies.add(dependency);
        return this;
    }

    public MockedPomFileBuilder withNoDependencies() {
        dependencies.clear();
        return this;
    }
    public MockedPomFileBuilder withASubmodule(MockedPomFileBuilder mockedPomFileBuilder) {
        subModules.add(mockedPomFileBuilder);
        return this;
    }

    public MockedPomFileBuilder withSubmodules(MockedPomFileBuilder ... mockedPomFileBuilders) {
        Arrays.stream(mockedPomFileBuilders)
                .forEach(subModules::add);
        return this;
    }

    public PomFile build() {
        final PomFile pomFile = Mockito.mock(PomFile.class);
        if(artifactIdentifier != null){
            when(pomFile.getArtifactIdentifier()).thenReturn(artifactIdentifier);
        }

        when(pomFile.getDependencies()).thenReturn(dependencies);

        final List<PomFile> subModules = this.subModules.stream()
                .map(MockedPomFileBuilder::build)
                .collect(Collectors.toList());
        try {
            when(pomFile.getModules()).thenReturn(subModules);
        } catch (PomFileLocationException | PomFileCreationException ignored) {}

        return pomFile;
    }
}
