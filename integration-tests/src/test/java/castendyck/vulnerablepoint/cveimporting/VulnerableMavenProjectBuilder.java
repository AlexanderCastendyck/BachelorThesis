package castendyck.vulnerablepoint.cveimporting;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.cve.CVE;
import castendyck.examplecves.ExampleCve;
import castendyck.vulnerablepoint.ModuleWithSubModuleBuilder;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.resolution.DependencyResolutionException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static castendyck.vulnerablepoint.ModuleWithSubModuleBuilder.aMultiModuleProject;
import static castendyck.vulnerablepoint.cveimporting.MavenProjectBuilder.aMavenProject;

public class VulnerableMavenProjectBuilder {
    private static final Path REPOSITORY_ROOT = Paths.get("target", "cveImportingTests");

    private final MavenProject mavenProject;
    private final List<MavenProject> reactorProjects;
    private final List<ArtifactIdentifier> dependenciesToResolve;
    private final List<CVE> expectedCves;

    public VulnerableMavenProjectBuilder(MavenProject mavenProject, List<ArtifactIdentifier> dependenciesToResolve, List<CVE> expectedCves) {
        this.mavenProject = mavenProject;
        this.reactorProjects = new ArrayList<>();
        this.dependenciesToResolve = dependenciesToResolve;
        this.expectedCves = expectedCves;
    }

    public VulnerableMavenProjectBuilder(MavenProject mavenProject, List<MavenProject> reactorProjects, List<ArtifactIdentifier> dependenciesToResolve, List<CVE> expectedCves) {
        this.mavenProject = mavenProject;
        this.reactorProjects = reactorProjects;
        this.dependenciesToResolve = dependenciesToResolve;
        this.expectedCves = expectedCves;
    }

    public static VulnerableMavenProjectBuilder aMavenProjectWithNoArtifacts() {
        final MavenProject mavenProject = aMavenProject("ProjectWithNoVulnerabilities")
                .withNoArtifact()
                .withRepositoryRootDirectory(REPOSITORY_ROOT)
                .build();

        final List<ArtifactIdentifier> dependenciesToResolve = new ArrayList<>();
        final List<CVE> expectedCves = new ArrayList<>();

        return new VulnerableMavenProjectBuilder(mavenProject, dependenciesToResolve, expectedCves);
    }

    public static VulnerableMavenProjectBuilder aMavenProjectWithNoVulnerabilities() {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("junit", "junit", "4.12");

        final MavenProject mavenProject = aMavenProject("ProjectWithNoVulnerabilities")
                .withArtifact(artifactIdentifier)
                .withRepositoryRootDirectory(REPOSITORY_ROOT)
                .build();

        final List<ArtifactIdentifier> dependenciesToResolve = Collections.singletonList(artifactIdentifier);
        final List<CVE> expectedCves = new ArrayList<>();

        return new VulnerableMavenProjectBuilder(mavenProject, dependenciesToResolve, expectedCves);
    }


    public static VulnerableMavenProjectBuilder aMavenProjectWithOneVulnerability() {
        ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("com.sun.mail", "mailapi", "1.5.5");

        final MavenProject mavenProject = aMavenProject("ProjectWithOneVulnerability")
                .withArtifact(artifactIdentifier)
                .withRepositoryRootDirectory(REPOSITORY_ROOT)
                .build();

        final List<ArtifactIdentifier> dependenciesToResolve = Collections.singletonList(artifactIdentifier);
        final List<CVE> expectedCves = ExampleCve.cveForMailApi();

        return new VulnerableMavenProjectBuilder(mavenProject, dependenciesToResolve, expectedCves);
    }

    public static VulnerableMavenProjectBuilder aMavenProjectWithComponentWithTwoVulnerabilities() {
        ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("org.eclipse.aether", "aether-spi", "1.0.2.v20150114");

        final MavenProject mavenProject = aMavenProject("ProjectWithComponentWithTwoVulnerabilities")
                .withArtifact(artifactIdentifier)
                .withRepositoryRootDirectory(REPOSITORY_ROOT)
                .build();

        final List<ArtifactIdentifier> dependenciesToResolve = Collections.singletonList(artifactIdentifier);
        final List<CVE> expectedCves = ExampleCve.cvesForAether();

        return new VulnerableMavenProjectBuilder(mavenProject, dependenciesToResolve, expectedCves);
    }

    public static VulnerableMavenProjectBuilder aMavenProjectWithSeveralVulnerabilities() {
        final List<ArtifactIdentifier> artifacts = Arrays.asList(
                ArtifactIdentifierBuilder.buildShort("org.eclipse.aether", "aether-spi", "1.0.2.v20150114"),
                ArtifactIdentifierBuilder.buildShort("commons-collections", "commons-collections", "3.2.1"),
                ArtifactIdentifierBuilder.buildShort("com.sun.mail", "mailapi", "1.5.5")
        );

        final MavenProject mavenProject = aMavenProject("ProjectWithComponentWithTwoVulnerabilities")
                .withArtifacts(artifacts)
                .withRepositoryRootDirectory(REPOSITORY_ROOT)
                .build();

        final List<CVE> expectedCves = asList(
                ExampleCve.cveForMailApi(),
                ExampleCve.cveForCommonsCollection(),
                ExampleCve.cvesForAether()
        );

        return new VulnerableMavenProjectBuilder(mavenProject, artifacts, expectedCves);
    }

    public static VulnerableMavenProjectBuilder aMavenProjectWithTwoSubModulesHavingAVulnerabilityEach() {
        final ArtifactIdentifier artifactMailApi = ArtifactIdentifierBuilder.buildShort("com.sun.mail", "mailapi", "1.5.5");
        final ArtifactIdentifier artifactCommonsCollection = ArtifactIdentifierBuilder.buildShort("commons-collections", "commons-collections", "3.2.1");

        final ModuleWithSubModuleBuilder.MultiModule multiModule = aMultiModuleProject()
                .withRootProject(aMavenProject("aMavenProjectWithTwoSubModulesHavingAVulnerabilityEach")
                        .withNoArtifact()
                        .withRepositoryRootDirectory(REPOSITORY_ROOT))
                .withASubModule(aMavenProject("submodule1")
                        .withArtifact(artifactMailApi)
                        .withRepositoryRootDirectory(REPOSITORY_ROOT))
                .withASubModule(aMavenProject("submodule2")
                        .withArtifact(artifactCommonsCollection)
                        .withRepositoryRootDirectory(REPOSITORY_ROOT))
                .build();

        final List<CVE> expectedCves = asList(
                ExampleCve.cveForMailApi(),
                ExampleCve.cveForCommonsCollection()
        );
        final List<ArtifactIdentifier> artifactToResolve = Arrays.asList(
                artifactMailApi,
                artifactCommonsCollection
        );
        return new VulnerableMavenProjectBuilder(multiModule.rootProject, multiModule.reactorProjects, artifactToResolve, expectedCves);
    }

    public VulnerableMavenProjectState build() throws DependencyCollectionException, DependencyResolutionException {
        LocalRepositoryResolver resolver = new LocalRepositoryResolver(REPOSITORY_ROOT);
        resolver.resolve(dependenciesToResolve);

        VulnerableMavenProjectState projectState = new VulnerableMavenProjectState();
        projectState.setMavenProject(mavenProject);
        projectState.setReactorProjects(reactorProjects);
        projectState.setExpectedCves(expectedCves);
        projectState.setRepositoryRoot(REPOSITORY_ROOT);
        return projectState;
    }

    @SafeVarargs
    private static List<CVE> asList(List<CVE>... cvesLists) {
        List<CVE> allCves = Arrays.stream(cvesLists)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return allCves;
    }
}
