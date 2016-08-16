package castendyck.vulnerablepoint;

import castendyck.dependencygraph.DependencyGraph;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.maven.pomfile.PomFile;
import org.apache.maven.project.MavenProject;

import java.util.List;

public class VulnerablePointsRequestDto {
    private final MavenProject mavenProject;
    private final List<MavenProject> reactorProjects;
    private final PomFile pomFile;
    private final DependencyGraph dependencyGraph;

    public VulnerablePointsRequestDto(MavenProject mavenProject, List<MavenProject> reactorProjects, PomFile pomFile,
                                      DependencyGraph dependencyGraph) {
        NotNullConstraintEnforcer.ensureNotNull(mavenProject);
        this.mavenProject = mavenProject;
        NotNullConstraintEnforcer.ensureNotNull(mavenProject);
        this.reactorProjects = reactorProjects;
        NotNullConstraintEnforcer.ensureNotNull(pomFile);
        this.pomFile = pomFile;
        NotNullConstraintEnforcer.ensureNotNull(dependencyGraph);
        this.dependencyGraph = dependencyGraph;
    }

    public MavenProject getMavenProject() {
        return mavenProject;
    }

    public List<MavenProject> getReactorProjects() {
        return reactorProjects;
    }

    public PomFile getPomFile() {
        return pomFile;
    }

    public DependencyGraph getDependencyGraph() {
        return dependencyGraph;
    }

}
