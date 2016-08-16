package castendyck.analyzing.reducing;

import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistry;
import castendyck.maventestpathidentifier.MavenTestPathIdentifier;
import castendyck.maven.pomfile.PomFile;

import java.nio.file.Path;

public class ReducerConfigurationBuilder {
    private PomFile pomFile;
    private DependencyRegistry dependencyRegistry;
    private SourceCodeRegistry sourceCodeRegistry;
    private Path projectRootPath;
    private MavenTestPathIdentifier generalMavenTestPathPart;

    public static ReducerConfigurationBuilder aConfiguration() {
        return new ReducerConfigurationBuilder();
    }

    public ReducerConfigurationBuilder withPomFile(PomFile pomFile){
        this.pomFile = pomFile;
        return this;
    }

    public ReducerConfigurationBuilder withDependencyRegistry(DependencyRegistry dependencyRegistry){
        this.dependencyRegistry = dependencyRegistry;
        return this;
    }

    public ReducerConfigurationBuilder withSourceCodeRegistry(SourceCodeRegistry sourceCodeRegistry){
        this.sourceCodeRegistry = sourceCodeRegistry;
        return this;
    }

    public ReducerConfigurationBuilder withProjectRootPath(Path projectRootPath){
        this.projectRootPath = projectRootPath;
        return this;
    }

    public ReducerConfigurationBuilder withGeneralMavenTestPathPart(MavenTestPathIdentifier generalMavenTestPathPart){
        this.generalMavenTestPathPart = generalMavenTestPathPart;
        return this;
    }

    public ReducerConfiguration build(){
        final ReducerConfiguration configuration = new ReducerConfiguration(pomFile, dependencyRegistry, sourceCodeRegistry, projectRootPath, generalMavenTestPathPart);
        return configuration;
    }
}
