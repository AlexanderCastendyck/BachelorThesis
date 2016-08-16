package castendyck.vulnerablepoint.cveimporting.internal;

import castendyck.cve.CVE;
import castendyck.vulnerablepoint.VulnerablePointsProviderConfiguration;
import castendyck.vulnerablepoint.cveimporting.CveImporter;
import castendyck.vulnerablepoint.cveimporting.CveImportingException;
import org.apache.log4j.Logger;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import org.owasp.dependencycheck.Engine;
import org.owasp.dependencycheck.data.nvdcve.DatabaseException;
import org.owasp.dependencycheck.dependency.Dependency;
import org.owasp.dependencycheck.dependency.Vulnerability;
import org.owasp.dependencycheck.utils.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

import static de.castendyck.collections.ObjectListToStringCollector.collectToString;


public class CveImporterImpl implements CveImporter {
    private final Logger logger = Logger.getLogger(CveImporterImpl.class);
    private final VulnerablePointsProviderConfiguration configuration;

    public CveImporterImpl(VulnerablePointsProviderConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public List<CVE> findCvesFor(MavenProject project, List<MavenProject> reactorProjects) throws CveImportingException {
        try {
            final Engine engine = createEngine(project, reactorProjects);

            final List<Dependency> dependencies = listDependencies(project, reactorProjects, engine);
            logger.info("Dependencies: "+dependencies.stream().map(Dependency::getFileName).collect(collectToString()));
            final List<Dependency> analyzedDependencies = analyzeDeps(dependencies, engine);
            final List<Vulnerability> vulnerabilities = extractVulnerabilities(analyzedDependencies);
            logger.info("vulnerabilities: "+vulnerabilities.stream().map(Vulnerability::getName).collect(collectToString()));
            final List<CVE> cves = mapToCves(vulnerabilities);
            logger.info("cves: "+cves.stream().map(CVE::getName).collect(collectToString()));
            return cves;
        } catch (DatabaseException e) {
            throw new CveImportingException(e);
        }
    }

    private Engine createEngine(MavenProject project, List<MavenProject> reactorProjects) throws DatabaseException {
        Settings.initialize();
        /*Settings.setStringIfNotEmpty(Settings.KEYS.SUPPRESSION_FILE, configuration.getSuppressionFile().toString());
        Settings.setBoolean(Settings.KEYS.AUTO_UPDATE, true);
        Settings.setBoolean(Settings.KEYS.ANALYZER_JAR_ENABLED, true);
        Settings.setBoolean(Settings.KEYS.ANALYZER_ARCHIVE_ENABLED, true);
        Settings.setBoolean(Settings.KEYS.ANALYZER_OPENSSL_ENABLED, false);
        Settings.setBoolean(Settings.KEYS.ANALYZER_NODE_PACKAGE_ENABLED, false);
        Settings.setBoolean(Settings.KEYS.ANALYZER_COMPOSER_LOCK_ENABLED, false);
        Settings.setBoolean(Settings.KEYS.ANALYZER_CENTRAL_ENABLED, true);
        */

        return new DependencyAnalyzeEngine(project, reactorProjects);
    }

    private List<Dependency> listDependencies(MavenProject project, List<MavenProject> reactorProjects, Engine engine) throws DatabaseException, CveImportingException {
        final List<Artifact> artifacts = collectArtifacts(project, reactorProjects);
        List<Dependency> dependencies = new ArrayList<>();
        for (Artifact artifact : artifacts) {
            final File file = artifact.getFile();
            final File absoluteFile = file.getAbsoluteFile();
            final List<Dependency> dependenciesOfArtifact = scanForDependencies(engine, absoluteFile);
            dependencies.addAll(dependenciesOfArtifact);
        }
        return dependencies;
    }

    private List<Artifact> collectArtifacts(MavenProject project, List<MavenProject> reactorProjects) {
        List<Artifact> artifacts = new ArrayList<>();
        final Set<Artifact> projectArtifacts = project.getArtifacts();
        storeWithoutDuplicates(projectArtifacts, artifacts);
        for (MavenProject reactorProject : reactorProjects) {
            final Set<Artifact> reactorProjectArtifacts = reactorProject.getArtifacts();
            storeWithoutDuplicates(reactorProjectArtifacts, artifacts);
        }
        return artifacts;
    }

    private void storeWithoutDuplicates(Set<Artifact> artifactsToStore, List<Artifact> artifacts) {
        artifactsToStore.stream()
                .filter(artifact -> artifactNotAlreadyStored(artifact, artifacts))
                .forEach(artifacts::add);
    }

    private boolean artifactNotAlreadyStored(Artifact artifact, List<Artifact> artifacts) {
        for (Artifact storedArtifact : artifacts) {
            boolean equal = artifact.getGroupId().equals(storedArtifact.getGroupId());
            equal &= artifact.getArtifactId().equals(storedArtifact.getArtifactId());
            equal &= artifact.getVersion().equals(storedArtifact.getVersion());
            equal &= artifact.getScope().equals(storedArtifact.getScope());
            equal &= artifact.getType().equals(storedArtifact.getType());
            if (equal) {
                return false;
            }
        }
        return true;
    }

    private List<Dependency> scanForDependencies(Engine engine, File absoluteFile) throws CveImportingException {
        final List<Dependency> dependencies = engine.scan(absoluteFile);
        if (dependencies != null) {
            return dependencies;
        } else {
            throw new CveImportingException();
        }
    }

    private static List<Dependency> analyzeDeps(List<Dependency> dependencies, Engine engine) {
        final List<Dependency> oldDependencies = engine.getDependencies();
        oldDependencies.clear();
        oldDependencies.addAll(dependencies);
        engine.analyzeDependencies();
        return oldDependencies;
    }

    private List<Vulnerability> extractVulnerabilities(List<Dependency> dependencies) {
        List<Vulnerability> vulnerabilities = new ArrayList<>();
        for (Dependency dependency : dependencies) {
            final SortedSet<Vulnerability> vulnerabilitiesOfDependency = dependency.getVulnerabilities();
            vulnerabilities.addAll(vulnerabilitiesOfDependency);
        }
        return vulnerabilities;
    }

    private List<CVE> mapToCves(List<Vulnerability> vulnerabilities) {
        final List<CVE> cves = vulnerabilities.stream()
                .map(VulnerabilityToCveMapper::mapToCve)
                .collect(Collectors.toList());

        return cves;
    }

}
