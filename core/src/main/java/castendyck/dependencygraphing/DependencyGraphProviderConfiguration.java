package castendyck.dependencygraphing;

import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.maven.pomfile.PomFile;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;

import java.util.List;

public class DependencyGraphProviderConfiguration {
    private final List<RemoteRepository> repositories;
    private final RepositorySystem repositorySystem;
    private final RepositorySystemSession session;
    private final PomFile parentPom;

    public DependencyGraphProviderConfiguration(List<RemoteRepository> repositories, RepositorySystem repositorySystem, RepositorySystemSession session, PomFile parentPom) {
        NotNullConstraintEnforcer.ensureNotNull(repositories);
        this.repositories = repositories;
        NotNullConstraintEnforcer.ensureNotNull(repositorySystem);
        this.repositorySystem = repositorySystem;
        NotNullConstraintEnforcer.ensureNotNull(session);
        this.session = session;
        NotNullConstraintEnforcer.ensureNotNull(parentPom);
        this.parentPom = parentPom;
    }

    public List<RemoteRepository> getRepositories() {
        return repositories;
    }

    public RepositorySystem getRepositorySystem() {
        return repositorySystem;
    }

    public RepositorySystemSession getSession() {
        return session;
    }

    public PomFile getParentPom() {
        return parentPom;
    }
}
