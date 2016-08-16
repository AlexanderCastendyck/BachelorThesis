package castendyck.vulnerablepoint.cveimporting;

import castendyck.cve.CVE;
import org.apache.maven.project.MavenProject;

import java.util.List;

public interface CveImporter {

    List<CVE> findCvesFor(MavenProject project, List<MavenProject> reactorProjects) throws CveImportingException;
}
