package castendyck.vulnerablepoint.internal;

import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistryFactory;
import castendyck.cve.CVE;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.maven.pomfile.PomFile;
import castendyck.repository.LocalRepository;
import castendyck.vulnerablepoint.*;
import castendyck.vulnerablepoint.cveimporting.CveImporter;
import castendyck.vulnerablepoint.cveimporting.CveImportingException;
import castendyck.vulnerablepoint.vulnerablepointslocation.VulnerablePointLocator;
import castendyck.vulnerablepoint.vulnerablepointslocation.VulnerablePointsLocatorFactory;
import org.apache.maven.project.MavenProject;

import java.util.List;

public class VulnerablePointsProviderImpl implements VulnerablePointsProvider {
    private final CveImporter cveImporter;
    private final VulnerablePointsProviderConfiguration configuration;

    public VulnerablePointsProviderImpl(CveImporter cveImporter, VulnerablePointsProviderConfiguration configuration) {
        this.cveImporter = cveImporter;
        this.configuration = configuration;
    }

    @Override
    public CollectedVulnerablePoints collectVulnerablePoints(VulnerablePointsRequestDto vulnerablePointsRequestDto) throws VulnerablePointsCollectingException {
        final MavenProject mavenProject = vulnerablePointsRequestDto.getMavenProject();
        final List<MavenProject> reactorProjects = vulnerablePointsRequestDto.getReactorProjects();
        final List<CVE> cves = collectCves(mavenProject, reactorProjects);

        final List<VulnerablePoint> vulnerablePoints = locateVulnerablePointsFor(cves, vulnerablePointsRequestDto);
        return new CollectedVulnerablePoints(cves, vulnerablePoints);
    }

    private List<CVE> collectCves(MavenProject mavenProject, List<MavenProject> reactorProjects) throws VulnerablePointsCollectingException {
        try {
            return cveImporter.findCvesFor(mavenProject, reactorProjects);
        } catch (CveImportingException e) {
            throw new VulnerablePointsCollectingException(e);
        }
    }

    private List<VulnerablePoint> locateVulnerablePointsFor(List<CVE> cves, VulnerablePointsRequestDto vulnerablePointsRequestDto) {
        final PomFile pomFile = vulnerablePointsRequestDto.getPomFile();
        final DependencyGraph dependencyGraph = vulnerablePointsRequestDto.getDependencyGraph();
        final LocalRepository localRepository= configuration.getLocalRepository();
        final SourceCodeRegistry sourceCodeRegistry = SourceCodeRegistryFactory.newInstance(dependencyGraph, localRepository);
        final VulnerablePointLocator vulnerablePointLocator = VulnerablePointsLocatorFactory.createDefaultOne(sourceCodeRegistry);

        final List<VulnerablePoint> vulnerablePoints = vulnerablePointLocator.locateVulnerablePoints(cves, pomFile);
        return vulnerablePoints;
    }
}
