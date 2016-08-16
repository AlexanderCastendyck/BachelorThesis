package castendyck.analyzing.reducing;

import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistry;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.maventestpathidentifier.MavenTestPathIdentifier;
import castendyck.maven.pomfile.PomFile;

import java.nio.file.Path;

public class ReducerConfiguration {
    private final PomFile pomFile;
    private final DependencyRegistry dependencyRegistry;
    private final SourceCodeRegistry sourceCodeRegistry;
    private final Path projectRootPath;
    private final MavenTestPathIdentifier generalMavenTestPathPart;


    public ReducerConfiguration(PomFile pomFile, DependencyRegistry dependencyRegistry, SourceCodeRegistry sourceCodeRegistry,
                                Path projectRootPath, MavenTestPathIdentifier generalMavenTestPathPart) {
        NotNullConstraintEnforcer.ensureNotNull(pomFile);
        this.pomFile = pomFile;
        NotNullConstraintEnforcer.ensureNotNull(dependencyRegistry);
        this.dependencyRegistry = dependencyRegistry;
        NotNullConstraintEnforcer.ensureNotNull(sourceCodeRegistry);
        this.sourceCodeRegistry = sourceCodeRegistry;
        NotNullConstraintEnforcer.ensureNotNull(projectRootPath);
        this.projectRootPath = projectRootPath;
        NotNullConstraintEnforcer.ensureNotNull(generalMavenTestPathPart);
        this.generalMavenTestPathPart = generalMavenTestPathPart;
    }

    public PomFile getPomFile() {
        return pomFile;
    }

    public DependencyRegistry getDependencyRegistry() {
        return dependencyRegistry;
    }

    public SourceCodeRegistry getSourceCodeRegistry() {
        return sourceCodeRegistry;
    }

    public Path getProjectRootPath() {
        return projectRootPath;
    }

    public MavenTestPathIdentifier getGeneralMavenTestPathPart() {
        return generalMavenTestPathPart;
    }
}
