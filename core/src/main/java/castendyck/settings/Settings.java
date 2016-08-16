package castendyck.settings;

import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.maventestpathidentifier.MavenTestPathIdentifier;
import castendyck.maven.pomfile.PomFile;
import castendyck.repository.LocalRepository;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;

import java.nio.file.Path;
import java.util.List;

public class Settings {
    private final MavenProject project;
    private final List<MavenProject> reactorProjects;
    private final RepositorySystemSession repositorySystemSession;
    private final List<RemoteRepository> remoteRepositories;
    private final RepositorySystem repositorySystem;
    private final LocalRepository localRepository;
    private final PomFile parentPom;
    private final Path projectRootPath;
    private final MavenTestPathIdentifier generalMavenTestPathIdentifier;
    private final Path suppressionFile;
    private final boolean owaspSkipTestScope;

    public Settings(MavenProject project, List<MavenProject> reactorProjects, RepositorySystemSession repositorySystemSession, List<RemoteRepository> remoteRepositories,
                    RepositorySystem repoSystem, LocalRepository localRepository, PomFile parentPom, Path projectRootPath, MavenTestPathIdentifier generalMavenTestPathIdentifier, Path suppressionFile, boolean owaspSkipTestScope) {
        NotNullConstraintEnforcer.ensureNotNull(project);
        this.project = project;
        NotNullConstraintEnforcer.ensureNotNull(reactorProjects);
        this.reactorProjects = reactorProjects;
        NotNullConstraintEnforcer.ensureNotNull(repositorySystemSession);
        this.repositorySystemSession = repositorySystemSession;
        NotNullConstraintEnforcer.ensureNotNull(remoteRepositories);
        this.remoteRepositories = remoteRepositories;
        NotNullConstraintEnforcer.ensureNotNull(repoSystem);
        this.repositorySystem = repoSystem;
        NotNullConstraintEnforcer.ensureNotNull(localRepository);
        this.localRepository = localRepository;
        NotNullConstraintEnforcer.ensureNotNull(parentPom);
        this.parentPom = parentPom;
        NotNullConstraintEnforcer.ensureNotNull(projectRootPath);
        this.projectRootPath = projectRootPath;
        NotNullConstraintEnforcer.ensureNotNull(generalMavenTestPathIdentifier);
        this.generalMavenTestPathIdentifier = generalMavenTestPathIdentifier;
        NotNullConstraintEnforcer.ensureNotNull(suppressionFile);
        this.suppressionFile = suppressionFile;
        NotNullConstraintEnforcer.ensureNotNull(owaspSkipTestScope);
        this.owaspSkipTestScope = owaspSkipTestScope;
    }

    public MavenProject getProject() {
        return project;
    }

    public List<MavenProject> getReactorProjects() {
        return reactorProjects;
    }

    public PomFile getParentPom() {
        return parentPom;
    }

    public RepositorySystemSession getRepositorySystemSession() {
        return repositorySystemSession;
    }

    public List<RemoteRepository> getRemoteRepositories() {
        return remoteRepositories;
    }

    public RepositorySystem getRepositorySystem() {
        return repositorySystem;
    }

    public LocalRepository getLocalRepository() {
        return localRepository;
    }

    public Path getProjectRootPath() {
        return projectRootPath;
    }

    public MavenTestPathIdentifier getGeneralMavenTestPathIdentifier() {
        return generalMavenTestPathIdentifier;
    }

    public Path getSuppressionFile() {
        return suppressionFile;
    }

    public boolean getOwaspSkipTestScope() {
        return owaspSkipTestScope;
    }
}
