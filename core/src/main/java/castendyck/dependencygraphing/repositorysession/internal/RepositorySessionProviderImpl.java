package castendyck.dependencygraphing.repositorysession.internal;

import castendyck.dependencygraphing.repositorysession.RepositorySession;
import castendyck.dependencygraphing.repositorysession.RepositorySessionProvider;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;

import java.util.List;

public class RepositorySessionProviderImpl implements RepositorySessionProvider {
    private final List<RemoteRepository> repositories;
    private final RepositorySystem repositorySystem;
    private final RepositorySystemSession session;


    public RepositorySessionProviderImpl(List<RemoteRepository> repositories, RepositorySystem repositorySystem, RepositorySystemSession session) {
        this.repositories = repositories;
        this.repositorySystem = repositorySystem;
        this.session = session;
    }


    @Override
    public RepositorySession provideSession() {
        return new RepositorySessionImpl(repositorySystem, session, repositories);
    }
}
