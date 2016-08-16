package castendyck.vulnerablepoint;

import castendyck.vulnerablepoint.cveimporting.MavenProjectBuilder;
import org.apache.maven.project.MavenProject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleWithSubModuleBuilder {
    private MavenProjectBuilder rootProjectBuilder;
    private final List<MavenProjectBuilder> subModules = new ArrayList<>();

    public static ModuleWithSubModuleBuilder aMultiModuleProject(){
        return new ModuleWithSubModuleBuilder();
    }

    public ModuleWithSubModuleBuilder withRootProject(MavenProjectBuilder mavenProjectBuilder){
        this.rootProjectBuilder = mavenProjectBuilder;
        return this;
    }


    public ModuleWithSubModuleBuilder withASubModule(MavenProjectBuilder submodule) {
        this.subModules.add(submodule);
        return this;
    }
    public MultiModule build(){
        final MavenProject rootProject = rootProjectBuilder.build();
        final List<MavenProject> reactorProjects = subModules.stream()
                .map(MavenProjectBuilder::build)
                .collect(Collectors.toList());
        return new MultiModule(rootProject, reactorProjects);
    }


    public class MultiModule {
        public final MavenProject rootProject;
        public final List<MavenProject> reactorProjects;

        public MultiModule(MavenProject rootProject, List<MavenProject> reactorProjects) {
            this.rootProject = rootProject;
            this.reactorProjects = reactorProjects;
        }
    }
}
