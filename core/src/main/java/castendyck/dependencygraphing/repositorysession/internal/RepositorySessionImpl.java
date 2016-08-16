package castendyck.dependencygraphing.repositorysession.internal;

import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.dependencygraphing.repositorysession.DependencyCollectionException;
import castendyck.dependencygraphing.repositorysession.RepositorySession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.repository.RemoteRepository;

import java.util.List;

public class RepositorySessionImpl implements RepositorySession {
    private final RepositorySystem repositorySystem;
    private final RepositorySystemSession session;
    private final List<RemoteRepository> repositories;

    RepositorySessionImpl(RepositorySystem repositorySystem, RepositorySystemSession session, List<RemoteRepository> repositories) {
        NotNullConstraintEnforcer.ensureNotNull(repositorySystem);
        this.repositorySystem = repositorySystem;
        NotNullConstraintEnforcer.ensureNotNull(session);
        this.session = session;
        NotNullConstraintEnforcer.ensureNotNull(repositories);
        this.repositories = repositories;
    }

    @Override
    public CollectResult fetchResult(CollectRequest request) throws DependencyCollectionException {
        try {
            return repositorySystem.collectDependencies(session, request);
        } catch (org.eclipse.aether.collection.DependencyCollectionException e) {
            throw new DependencyCollectionException(e);
        }
    }

    @Override
    public List<RemoteRepository> getAssociatedRepositories() {
        return repositories;
    }
}
