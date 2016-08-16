package castendyck.dependencygraphing.graphfetching.internal;

import castendyck.aetherdependency.AetherLeafDependency;
import castendyck.aetherdependency.AetherRootDependency;
import castendyck.dependencygraphing.graphfetching.DependencyGraphFetcher;
import castendyck.dependencygraphing.graphfetching.GraphFetchingException;
import castendyck.dependencygraphing.repositorysession.DependencyCollectionException;
import castendyck.dependencygraphing.repositorysession.RepositorySession;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraphing.repositorysession.RepositorySessionProvider;
import de.castendyck.collections.ObjectListToStringCollector;
import org.apache.log4j.Logger;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.repository.RemoteRepository;

import java.util.List;
import java.util.stream.Collectors;

import static de.castendyck.collections.ObjectListToStringCollector.collectToString;

public class DependencyGraphFetcherImpl implements DependencyGraphFetcher {
    private final Logger logger = Logger.getLogger(DependencyGraphFetcherImpl.class);

    private final RepositorySessionProvider sessionProvider;

    public DependencyGraphFetcherImpl(RepositorySessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    @Override
    public DependencyGraph fetchGraph(AetherRootDependency root) throws GraphFetchingException {
        final RepositorySession session = sessionProvider.provideSession();
        final CollectRequest request = mapToRequest(root, session);
        final DependencyGraph dependencyGraph = executeRequest(session, request);
        return dependencyGraph;
    }

    private DependencyGraph executeRequest(RepositorySession session, CollectRequest request) throws GraphFetchingException {
        try {
            final CollectResult result = session.fetchResult(request);
            final DependencyNode rootNode = result.getRoot();
            final DependencyGraphTraverser dependencyGraphTraverser = new DependencyGraphTraverser();
            final DependencyGraph dependencyGraph = dependencyGraphTraverser.getResultingGraph(rootNode);
            return dependencyGraph;
        } catch (DependencyCollectionException e) {
            throw new GraphFetchingException(e);
        }
    }

    private CollectRequest mapToRequest(AetherRootDependency root, RepositorySession session) {
        final CollectRequest collectRequest = new CollectRequest();
        final org.eclipse.aether.graph.Dependency rootDependency = root.getRealAetherDependency();
        collectRequest.setRoot(rootDependency);

        final List<AetherLeafDependency> aetherDependencies = root.getAetherLeafDependencies();
        List<org.eclipse.aether.graph.Dependency> realAetherDependencies = aetherDependencies.stream()
                .map(AetherLeafDependency::getRealAetherDependency)
                .collect(Collectors.toList());
        collectRequest.setDependencies(realAetherDependencies);

        final List<RemoteRepository> associatedRepositories = session.getAssociatedRepositories();
        collectRequest.setRepositories(associatedRepositories);

        return collectRequest;
    }
}
