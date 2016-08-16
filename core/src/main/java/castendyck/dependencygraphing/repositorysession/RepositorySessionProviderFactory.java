package castendyck.dependencygraphing.repositorysession;

import castendyck.dependencygraphing.repositorysession.internal.RepositorySessionProviderImpl;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;

import java.util.List;

public class RepositorySessionProviderFactory {
    public static RepositorySessionProvider newInstance(List<RemoteRepository> repositories, RepositorySystem repositorySystem, RepositorySystemSession session){
        return new RepositorySessionProviderImpl(repositories, repositorySystem, session);
    }
}
