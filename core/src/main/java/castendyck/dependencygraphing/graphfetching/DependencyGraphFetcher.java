package castendyck.dependencygraphing.graphfetching;

import castendyck.aetherdependency.AetherRootDependency;
import castendyck.dependencygraph.DependencyGraph;

public interface DependencyGraphFetcher {

    DependencyGraph fetchGraph(AetherRootDependency root) throws GraphFetchingException;
}
