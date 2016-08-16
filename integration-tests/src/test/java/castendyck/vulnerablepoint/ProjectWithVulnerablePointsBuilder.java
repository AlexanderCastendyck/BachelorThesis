package castendyck.vulnerablepoint;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.dependency.Dependency;
import castendyck.dependency.DependencyFactory;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraphing.DependencyGraphTestBuilder;
import castendyck.dependencygraphing.GraphNodeTestBuilder;
import castendyck.functionidentifier.FunctionIdentifierBuilder;
import castendyck.mavendependency.MavenDependency;
import castendyck.maven.pomfile.PomFile;
import castendyck.maven.pomfile.PomFileCreationException;
import castendyck.maven.pomfile.PomFileFactory;
import castendyck.repository.LocalRepository;
import castendyck.repository.LocalRepositoryFactory;
import castendyck.examplecves.ExampleCve;
import castendyck.vulnerablepoint.cveimporting.LocalRepositoryResolver;
import castendyck.vulnerablepoint.cveimporting.MavenProjectBuilder;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.resolution.DependencyResolutionException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static castendyck.vulnerablepoint.LocalStoredArtifactBuilder.aLocallyInstalledArtifactIdentifier;
import static junit.framework.TestCase.fail;

public class ProjectWithVulnerablePointsBuilder {
    private static final Path REPOSITORY_ROOT = Paths.get("target", "vulnerablePointsProviderTests");
    private MavenProject mavenProject;
    private List<VulnerablePoint> expectedVulnerablePoints;
    private DependencyGraph dependencyGraph;
    private ArtifactIdentifier artifactIdentifier;


    public ProjectWithVulnerablePointsBuilder(ArtifactIdentifier artifactIdentifier, MavenProject mavenProject,
                                              DependencyGraph dependencyGraph, List<VulnerablePoint> expectedVulnerablePoints) {
        this.artifactIdentifier = artifactIdentifier;
        this.mavenProject = mavenProject;
        this.dependencyGraph = dependencyGraph;
        this.expectedVulnerablePoints = expectedVulnerablePoints;
    }

    public static ProjectWithVulnerablePointsBuilder aMavenProjectWithNoDependencies() throws IOException, PomFileCreationException {
        final ArtifactIdentifier artifactIdentifier = aLocallyInstalledArtifactIdentifier()
                .forArtifact("local", "aMavenProjectWithNoDependencies", "1.0")
                .inRepo(REPOSITORY_ROOT)
                .withNoDependecies()
                .build();

        final MavenProject mavenProject = MavenProjectBuilder.aMavenProject("aMavenProjectWithNoDependencies")
                .withArtifact(artifactIdentifier)
                .withRepositoryRootDirectory(REPOSITORY_ROOT)
                .build();

        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(GraphNodeTestBuilder.aGraphNode()
                        .forArtifact(artifactIdentifier))
                .build();
        final List<VulnerablePoint> expectedVulnerablePoints = new ArrayList<>();
        return new ProjectWithVulnerablePointsBuilder(artifactIdentifier, mavenProject, dependencyGraph, expectedVulnerablePoints);
    }

    public static ProjectWithVulnerablePointsBuilder aMavenProjectWithNoVulnerabilities() throws IOException, PomFileCreationException {
        final ArtifactIdentifier artifactIdentifier = aLocallyInstalledArtifactIdentifier()
                .forArtifact("local", "aMavenProjectWithNoVulnerabilities", "1.0")
                .inRepo(REPOSITORY_ROOT)
                .withDependencies(
                        dependencyFor("junit", "junit", "4.12")
                )
                .build();

        final MavenProject mavenProject = MavenProjectBuilder.aMavenProject("ProjectWithNoVulnerabilities")
                .withArtifact(artifactIdentifier)
                .withRepositoryRootDirectory(REPOSITORY_ROOT)
                .build();

        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(GraphNodeTestBuilder.aGraphNode()
                        .forArtifact(artifactIdentifier))
                .build();


        final List<VulnerablePoint> expectedVulnerablePoints = new ArrayList<>();
        return new ProjectWithVulnerablePointsBuilder(artifactIdentifier, mavenProject, dependencyGraph, expectedVulnerablePoints);
    }

    public static ProjectWithVulnerablePointsBuilder aMavenProjectWithOneVulnerabilityWithOneLocation() throws Exception {
        final ArtifactIdentifier artifactIdentifier = aLocallyInstalledArtifactIdentifier()
                .forArtifact("local", "ProjectWithOneVulnerabilityWithOneLocation", "1.0")
                .inRepo(REPOSITORY_ROOT)
                .withDependencies(
                        dependencyFor("org.apache.sling", "org.apache.sling.servlets.post", "2.2.0")
                )
                .build();

        final MavenProject mavenProject = MavenProjectBuilder.aMavenProject("ProjectWithOneVulnerabilityWithOneLocation")
                .withArtifact(artifactIdentifier)
                .withArtifact(ArtifactIdentifierBuilder.buildShort("org.apache.sling", "org.apache.sling.servlets.post", "2.2.0"))
                .withRepositoryRootDirectory(REPOSITORY_ROOT)
                .build();

        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(GraphNodeTestBuilder.aGraphNode()
                        .forArtifact(artifactIdentifier))
                .build();

        final List<VulnerablePoint> expectedVulnerablePoints = Collections.singletonList(VulnerablePointBuilder.aVulnerablePoint()
                .forCve(ExampleCve.cveForCVE_2013_22541())
                .withFunctionIdentifier(FunctionIdentifierBuilder.aFunctionIdentifier()
                        .withArtifactIdentifier(ArtifactIdentifierBuilder.buildShort("apache", "org.apache.sling.servlets.post", "2.2.0"))
                        .forClass("impl/operations/AbstractCreateOperation.class")
                        .forFunction("deepGetOrCreateNode")
                        .build())
                .build());
        return new ProjectWithVulnerablePointsBuilder(artifactIdentifier, mavenProject, dependencyGraph, expectedVulnerablePoints);
    }

    public static ProjectWithVulnerablePointsBuilder aMavenProjectWithTwoVulnerabilityWithOneLocationEach() throws Exception {
        final ArtifactIdentifier artifactIdentifier = aLocallyInstalledArtifactIdentifier()
                .forArtifact("local", "ProjectWithTwoVulnerabilityWithOneLocationEach", "1.0")
                .inRepo(REPOSITORY_ROOT)
                .withDependencies(
                        dependencyFor("org.apache.sling", "org.apache.sling.servlets.post", "2.2.0"),
                        dependencyFor("org.richfaces", "richfaces", "5.0.0.Alpha3")
                )
                .build();

        final MavenProject mavenProject = MavenProjectBuilder.aMavenProject("ProjectWithTwoVulnerabilityWithOneLocationEach")
                .withArtifact(artifactIdentifier)
                .withArtifact(ArtifactIdentifierBuilder.buildShort("org.apache.sling", "org.apache.sling.servlets.post", "2.2.0"))
                .withArtifact(ArtifactIdentifierBuilder.buildShort("org.richfaces", "richfaces", "5.0.0.Alpha3"))
                .withRepositoryRootDirectory(REPOSITORY_ROOT)
                .build();

        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(GraphNodeTestBuilder.aGraphNode()
                        .forArtifact(artifactIdentifier)
                        .withChildren(GraphNodeTestBuilder.aGraphNode()
                                        .forArtifact(ArtifactIdentifierBuilder.buildShort("org.apache.sling", "org.apache.sling.servlets.post", "2.2.0")),
                                GraphNodeTestBuilder.aGraphNode()
                                        .forArtifact(ArtifactIdentifierBuilder.buildShort("org.richfaces", "richfaces", "5.0.0.Alpha3"))))
                .build();

        final List<VulnerablePoint> expectedVulnerablePoints = Arrays.asList(VulnerablePointBuilder.aVulnerablePoint()
                        .forCve(ExampleCve.cveForCVE_2013_22541())
                        .withFunctionIdentifier(FunctionIdentifierBuilder.aFunctionIdentifier()
                                .withArtifactIdentifier(ArtifactIdentifierBuilder.buildShort("apache", "org.apache.sling.servlets.post", "2.2.0"))
                                .forClass("impl/operations/AbstractCreateOperation.class")
                                .forFunction("deepGetOrCreateNode")
                                .build())
                        .build(),
                VulnerablePointBuilder.aVulnerablePoint()
                        .forCve(ExampleCve.cveForCVE_2014_0086())
                        .withFunctionIdentifier(FunctionIdentifierBuilder.aFunctionIdentifier()
                                .withArtifactIdentifier(ArtifactIdentifierBuilder.buildShort("org.richfaces", "richfaces", "5.0.0.Alpha3"))
                                .forClass("webapp/PushHandlerFilter.class")
                                .forFunction("doFilter")
                                .build())
                        .build()
        );
        return new ProjectWithVulnerablePointsBuilder(artifactIdentifier, mavenProject, dependencyGraph, expectedVulnerablePoints);
    }

    public static ProjectWithVulnerablePointsBuilder aMavenProjectWithOneVulnerabilityWithSeveralLocations() throws Exception {
        final ArtifactIdentifier artifactIdentifier = aLocallyInstalledArtifactIdentifier()
                .forArtifact("local", "ProjectWithOneVulnerabilityWithSeveralLocations", "1.0")
                .inRepo(REPOSITORY_ROOT)
                .withDependencies(
                        dependencyFor("org.jbpm", "jbpm-designer", "6.2.0.Final")
                )
                .build();

        final MavenProject mavenProject = MavenProjectBuilder.aMavenProject("ProjectWithOneVulnerabilityWithSeveralLocations")
                .withArtifact(artifactIdentifier)
                .withArtifact(ArtifactIdentifierBuilder.buildShort("org.jbpm", "jbpm-designer", "6.2.0.Final"))
                .withRepositoryRootDirectory(REPOSITORY_ROOT)
                .build();

        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(GraphNodeTestBuilder.aGraphNode()
                        .forArtifact(artifactIdentifier))
                .build();

        final List<VulnerablePoint> expectedVulnerablePoints = Collections.singletonList(VulnerablePointBuilder.aVulnerablePoint()
                .forCve(ExampleCve.cveForCVE_2013_22541())
                .withFunctionIdentifier(FunctionIdentifierBuilder.aFunctionIdentifier()
                        .withArtifactIdentifier(ArtifactIdentifierBuilder.buildShort("apache", "org.apache.sling.servlets.post", "2.2.0"))
                        .forClass("impl/operations/AbstractCreateOperation.class")
                        .forFunction("deepGetOrCreateNode")
                        .build())
                .build());
        return new ProjectWithVulnerablePointsBuilder(artifactIdentifier, mavenProject, dependencyGraph, expectedVulnerablePoints);
    }

    public static ProjectWithVulnerablePointsBuilder aMultiModuleProjectWithVulnerabilities() {
        fail();
        return null;
    }

    private static Dependency dependencyFor(String groupId, String artifactId, String version) {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort(groupId, artifactId, version);
        final Dependency dependency = DependencyFactory.dependencyFor(artifactIdentifier);
        return dependency;
    }

    public ProjectWithVulnerablePoints build() throws DependencyCollectionException, DependencyResolutionException, PomFileCreationException, IOException {
        final LocalRepository localRepository = LocalRepositoryFactory.createNewInPath(REPOSITORY_ROOT);
        final Path pathForPomFile = localRepository.potentialPomFileLocationFor(artifactIdentifier);
        final PomFile pomFile = PomFileFactory.createFromPath(pathForPomFile);
        final List<ArtifactIdentifier> dependenciesToResolve = pomFile.getDependencies()
                .stream()
                .map(MavenDependency::getArtifactIdentifier)
                .collect(Collectors.toList());
        LocalRepositoryResolver resolver = new LocalRepositoryResolver(REPOSITORY_ROOT);
        resolver.resolve(dependenciesToResolve);
        final List<MavenProject> reactorProjects = new ArrayList<>();
        return new ProjectWithVulnerablePoints(mavenProject, reactorProjects, expectedVulnerablePoints, pomFile, dependencyGraph, localRepository);
    }

    class ProjectWithVulnerablePoints {
        private final MavenProject mavenProject;
        private final List<MavenProject> reactorProjects;
        private final List<VulnerablePoint> expectedVulnerablePoints;
        private final PomFile pomFile;
        private final DependencyGraph dependencyGraph;
        private final LocalRepository localRepository;
        private VulnerablePointsProviderConfiguration configuration;

        public ProjectWithVulnerablePoints(MavenProject mavenProject, List<MavenProject> reactorProjects, List<VulnerablePoint> expectedVulnerablePoints,
                                           PomFile pomFile, DependencyGraph dependencyGraph, LocalRepository localRepository) {
            this.mavenProject = mavenProject;
            this.reactorProjects = reactorProjects;
            this.expectedVulnerablePoints = expectedVulnerablePoints;
            this.pomFile = pomFile;
            this.dependencyGraph = dependencyGraph;
            this.localRepository = localRepository;
        }

        public MavenProject getMavenProject() {
            return mavenProject;
        }

        public List<MavenProject> getReactorProjects() {
            return reactorProjects;
        }

        public List<VulnerablePoint> getExpectedVulnerablePoints() {
            return expectedVulnerablePoints;
        }

        public PomFile getPomFile() {
            return pomFile;
        }

        public DependencyGraph getDependencyGraph() {
            return dependencyGraph;
        }

        public LocalRepository getLocalRepository() {
            return localRepository;
        }

        public VulnerablePointsProviderConfiguration getConfiguration() {
            return configuration;
        }
    }
}
