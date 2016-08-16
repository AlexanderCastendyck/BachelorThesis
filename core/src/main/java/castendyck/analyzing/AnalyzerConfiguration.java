package castendyck.analyzing;

import castendyck.dependencygraph.DependencyGraph;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.maventestpathidentifier.MavenTestPathIdentifier;
import castendyck.maven.pomfile.PomFile;
import castendyck.repository.LocalRepository;

import java.nio.file.Path;

public class AnalyzerConfiguration {
    private final PomFile pomFile;
    private final DependencyGraph dependencyGraph;
    private final LocalRepository localRepository;
    private final Path projectRootPath;
    private final MavenTestPathIdentifier mavenTestPathIdentifier;

    public AnalyzerConfiguration(PomFile pomFile, DependencyGraph dependencyGraph, LocalRepository localRepository, Path projectRootPath, MavenTestPathIdentifier mavenTestPathIdentifier) {
        NotNullConstraintEnforcer.ensureNotNull(pomFile);
        this.pomFile = pomFile;
        NotNullConstraintEnforcer.ensureNotNull(dependencyGraph);
        this.dependencyGraph = dependencyGraph;
        NotNullConstraintEnforcer.ensureNotNull(localRepository);
        this.localRepository = localRepository;
        NotNullConstraintEnforcer.ensureNotNull(projectRootPath);
        this.projectRootPath = projectRootPath;
        NotNullConstraintEnforcer.ensureNotNull(mavenTestPathIdentifier);
        this.mavenTestPathIdentifier = mavenTestPathIdentifier;
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

    public Path getProjectRootPath() {
        return projectRootPath;
    }

    public MavenTestPathIdentifier getMavenTestPathIdentifier() {
        return mavenTestPathIdentifier;
    }
}
