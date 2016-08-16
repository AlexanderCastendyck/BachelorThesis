package castendyck.dependencygraphing.graphfetching;

import castendyck.dependencygraphing.graphfetching.internal.DependencyGraphFetcherImpl;
import castendyck.dependencygraphing.repositorysession.RepositorySessionProvider;

public class DependencyGraphFetcherFactory {

    public static DependencyGraphFetcher newInstance(RepositorySessionProvider sessionProvider) {
        return new DependencyGraphFetcherImpl(sessionProvider);
    }
}
