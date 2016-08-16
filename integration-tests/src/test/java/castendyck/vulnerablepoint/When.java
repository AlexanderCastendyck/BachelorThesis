package castendyck.vulnerablepoint;

import castendyck.dependencygraph.DependencyGraph;
import castendyck.maven.pomfile.PomFile;
import castendyck.repository.LocalRepository;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class When {
    private final VulnerablePointsProvider vulnerablePointsProvider;
    private final List<MavenProject> reactorProjects;
    private final MavenProject mavenProject;
    private final List<VulnerablePoint> expectedVulnerablePoints;
    private final PomFile pomFile;
    private final DependencyGraph dependencyGraph;
    private final LocalRepository localRepository;

    public When(ProjectWithVulnerablePointsBuilder.ProjectWithVulnerablePoints projectWithVulnerablePoints) {
        this.mavenProject = projectWithVulnerablePoints.getMavenProject();
        this.reactorProjects = projectWithVulnerablePoints.getReactorProjects();
        this.expectedVulnerablePoints = projectWithVulnerablePoints.getExpectedVulnerablePoints();
        this.pomFile = projectWithVulnerablePoints.getPomFile();
        this.dependencyGraph = projectWithVulnerablePoints.getDependencyGraph();
        this.localRepository = projectWithVulnerablePoints.getLocalRepository();
        final VulnerablePointsProviderConfiguration configuration = createConfiguration(projectWithVulnerablePoints);
        this.vulnerablePointsProvider = VulnerablePointsProviderFactory.newInstance(configuration);
    }

    private VulnerablePointsProviderConfiguration createConfiguration(ProjectWithVulnerablePointsBuilder.ProjectWithVulnerablePoints projectWithVulnerablePoints) {
        final Path suppressionFile = Paths.get("ignore.xml");
        final LocalRepository localRepository = projectWithVulnerablePoints.getLocalRepository();
        final VulnerablePointsProviderConfiguration configuration = new VulnerablePointsProviderConfiguration(localRepository, false, suppressionFile);
        return configuration;
    }

    public Then whenVulnerablePointsAreCollected() throws VulnerablePointsCollectingException {
        final VulnerablePointsRequestDto vulnerablePointsRequestDto = new VulnerablePointsRequestDto(mavenProject, reactorProjects, pomFile, dependencyGraph);
        final CollectedVulnerablePoints collectedVulnerablePoints = vulnerablePointsProvider.collectVulnerablePoints(vulnerablePointsRequestDto);
        final List<VulnerablePoint> vulnerablePoints = collectedVulnerablePoints.getVulnerablePoints();
        return new Then(vulnerablePoints, expectedVulnerablePoints);
    }

}
