package castendyck.vulnerablepoint.cveimporting;

import castendyck.cve.CVE;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.util.List;

public class VulnerableMavenProjectState {
    private MavenProject mavenProject;
    private List<MavenProject> reactorProjects;

    private List<CVE> expectedCves;
    private Path repositoryRoot;

    public void setMavenProject(MavenProject mavenProject) {
        this.mavenProject = mavenProject;
    }

    public MavenProject getMavenProject() {
        return mavenProject;
    }

    public void setReactorProjects(List<MavenProject> reactorProjects) {
        this.reactorProjects = reactorProjects;
    }

    public List<MavenProject> getReactorProjects() {
        return reactorProjects;
    }

    public void setExpectedCves(List<CVE> expectedCves) {
        this.expectedCves = expectedCves;
    }

    public List<CVE> getExpectedCves() {
        return expectedCves;
    }

    public void setRepositoryRoot(Path repositoryRoot) {
        this.repositoryRoot = repositoryRoot;
    }

    public Path getRepositoryRoot() {
        return repositoryRoot;
    }
}
