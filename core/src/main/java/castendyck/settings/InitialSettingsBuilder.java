package castendyck.settings;

import castendyck.maventestpathidentifier.MavenTestPathIdentifier;
import castendyck.maven.pomfile.PomFile;
import castendyck.repository.LocalRepository;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;

import java.nio.file.Path;
import java.util.List;

public class InitialSettingsBuilder {
    private MavenProject project;
    private List<MavenProject> reactorProjects;
    private RepositorySystemSession repoSession;
    private List<RemoteRepository> projectRepos;
    private RepositorySystem repoSystem;
    private PomFile pomFile;
    private LocalRepository localRepository;
    private Path projectRootPath;
    private MavenTestPathIdentifier generalMavenTestPathIdentifier;
    private Path suppressionFile;
    private boolean owaspSkipTestScope = false;

    public static InitialSettingsBuilder Settings(){
        return new InitialSettingsBuilder();
    }

    public InitialSettingsBuilder forMavenProject(MavenProject project){
        this.project = project;
        return this;
    }
    public InitialSettingsBuilder withReactorProjects(List<MavenProject> reactorProjects) {
        this.reactorProjects = reactorProjects;
        return this;
    }
    public InitialSettingsBuilder withRepositorySystemSession(RepositorySystemSession repoSession){
        this.repoSession = repoSession;
        return this;
    }
    public InitialSettingsBuilder withRepositories(List<RemoteRepository> projectRepos){
        this.projectRepos = projectRepos;
        return this;
    }
    public InitialSettingsBuilder withRepositorySystem(RepositorySystem repoSystem) {
        this.repoSystem = repoSystem;
        return this;
    }
    public InitialSettingsBuilder withPomFile(PomFile pomFile){
        this.pomFile = pomFile;
        return this;
    }
    public InitialSettingsBuilder withLocalRepository(LocalRepository localRepository){
        this.localRepository = localRepository;
        return this;
    }

    public InitialSettingsBuilder withSuppressionFile(Path suppressionFile) {
        this.suppressionFile = suppressionFile;
        return this;
    }

    public InitialSettingsBuilder withOwaspSkipTestScope(boolean owaspSkipTestScope) {
        this.owaspSkipTestScope = owaspSkipTestScope;
        return this;
    }

    public InitialSettingsBuilder withProjectRootPath(Path projectRootPath){
        this.projectRootPath = projectRootPath;
        return this;
    }

    public InitialSettingsBuilder withMavenTestPathIdentifier(MavenTestPathIdentifier generalMavenTestPathIdentifier){
        this.generalMavenTestPathIdentifier = generalMavenTestPathIdentifier;
        return this;
    }

    public Settings build(){
        final Settings settings = new Settings(project, reactorProjects,  repoSession, projectRepos, repoSystem, localRepository,
                pomFile, projectRootPath, generalMavenTestPathIdentifier, suppressionFile, owaspSkipTestScope);
        return settings;
    }
}
