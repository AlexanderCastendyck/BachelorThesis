package castendyck.dependencygraphing.internal;

import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraphing.DependencyGraphProvider;
import castendyck.dependencygraphing.DependencyGraphProviderConfiguration;
import castendyck.dependencygraphing.DependencyGraphProvidingException;
import castendyck.dependencygraphing.graphcreation.DependencyGraphCreator;
import castendyck.dependencygraphing.graphcreation.DependencyGraphCreatorFactory;
import castendyck.dependencygraphing.graphcreation.GraphCreationException;
import castendyck.dependencygraphing.graphfetching.DependencyGraphFetcher;
import castendyck.dependencygraphing.graphfetching.DependencyGraphFetcherFactory;
import castendyck.dependencygraphing.repositorysession.RepositorySessionProvider;
import castendyck.dependencygraphing.repositorysession.RepositorySessionProviderFactory;
import castendyck.maven.pomfile.PomFile;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;

import java.util.List;

public class DependencyGraphProviderImpl implements DependencyGraphProvider {
    private final DependencyGraphCreator dependencyGraphCreator;

    public DependencyGraphProviderImpl(DependencyGraphProviderConfiguration configuration) {
        final List<RemoteRepository> repositories = configuration.getRepositories();
        final RepositorySystem repositorySystem = configuration.getRepositorySystem();
        final RepositorySystemSession session = configuration.getSession();
        final RepositorySessionProvider sessionProvider = RepositorySessionProviderFactory.newInstance(repositories, repositorySystem, session);
        final DependencyGraphFetcher dependencyGraphFetcher = DependencyGraphFetcherFactory.newInstance(sessionProvider);
        final PomFile parentPom = configuration.getParentPom();

        dependencyGraphCreator = DependencyGraphCreatorFactory.newInstance(dependencyGraphFetcher, parentPom);
    }

    @Override
    public DependencyGraph provideDependencyGraphFor(PomFile pomFile) throws DependencyGraphProvidingException {
        try {
            final DependencyGraph dependencyGraphFor = dependencyGraphCreator.createDependencyGraphFor(pomFile);
            return dependencyGraphFor;
        } catch (GraphCreationException e) {
            throw new DependencyGraphProvidingException(e);
        }
    }
}
