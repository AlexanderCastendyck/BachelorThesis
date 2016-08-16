package castendyck.vulnerablepoint.cveimporting.internal;

import org.apache.maven.project.MavenProject;
import org.owasp.dependencycheck.Engine;
import org.owasp.dependencycheck.data.nvdcve.DatabaseException;

import java.util.List;

public class DependencyAnalyzeEngine extends Engine {
    private final MavenProject currentProject;
    private final List<MavenProject> reactorProjects;

    public DependencyAnalyzeEngine(MavenProject currentProject, List<MavenProject> reactorProjects) throws DatabaseException {
        super();
        this.currentProject = currentProject;
        this.reactorProjects = reactorProjects;
    }
}
