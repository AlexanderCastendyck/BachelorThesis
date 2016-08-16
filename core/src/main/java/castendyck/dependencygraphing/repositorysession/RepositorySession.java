package castendyck.dependencygraphing.repositorysession;

import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.repository.RemoteRepository;

import java.util.List;

public interface RepositorySession {
    CollectResult fetchResult(CollectRequest request) throws DependencyCollectionException;

    List<RemoteRepository> getAssociatedRepositories();
}
