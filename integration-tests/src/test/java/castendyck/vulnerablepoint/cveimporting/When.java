package castendyck.vulnerablepoint.cveimporting;


import castendyck.cve.CVE;
import castendyck.localrepository.LocalRepositoryBuilder;
import castendyck.repository.LocalRepository;
import castendyck.vulnerablepoint.VulnerablePointsProviderConfiguration;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class When {
    private final MavenProject mavenProject;
    private final List<MavenProject> reactorProjects;
    private final List<CVE> expectedCves;
    private final Path repositoryRoot;

    public When(VulnerableMavenProjectState projectState) {
        this.mavenProject = projectState.getMavenProject();
        this.reactorProjects = projectState.getReactorProjects();
        this.expectedCves = projectState.getExpectedCves();
        this.repositoryRoot = projectState.getRepositoryRoot();
    }

    public Then whenCvesAreAnalyzed() throws IOException {
        final VulnerablePointsProviderConfiguration configuration = createConfiguration();
        CveImporter cveImporter = CveImporterFactory.newInstance(configuration);

        try {
            final List<CVE> cves = cveImporter.findCvesFor(mavenProject, reactorProjects);
            return new Then(cves, expectedCves);
        } catch (Exception e) {
            return new Then(e);
        }
    }

    private VulnerablePointsProviderConfiguration createConfiguration() throws IOException {
        final LocalRepository localRepository = LocalRepositoryBuilder.aLocalRepository()
                .locatedAt(repositoryRoot)
                .build();
        final Path suppressionFile = Paths.get("ignore.xml");
        return new VulnerablePointsProviderConfiguration(localRepository, false, suppressionFile);
    }
}
