package castendyck.vulnerablepoint.cveinterpreting.internal;

import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistryFactory;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraphing.DependencyGraphTestBuilder;
import castendyck.localrepository.LocalRepositoryBuilder;
import castendyck.repository.LocalRepository;

import java.io.IOException;

public class SourceCodeRegistryBuilder {
    private LocalRepository localRepository;
    private DependencyGraph dependencyGraph;

    public static SourceCodeRegistryBuilder aSourceCodeRegistry() {
        return new SourceCodeRegistryBuilder();
    }

    public SourceCodeRegistryBuilder with(LocalRepositoryBuilder localRepositoryBuilder) throws IOException {
        this.localRepository= localRepositoryBuilder.build();
        return this;
    }
    public SourceCodeRegistryBuilder with(LocalRepository localRepository) throws IOException {
        this.localRepository= localRepository;
        return this;
    }

    public SourceCodeRegistryBuilder with(DependencyGraphTestBuilder dependencyGraphTestBuilder) {
        this.dependencyGraph = dependencyGraphTestBuilder.build();
        return this;
    }
    public SourceCodeRegistryBuilder with(DependencyGraph dependencyGraph) {
        this.dependencyGraph = dependencyGraph;
        return this;
    }

    public SourceCodeRegistry build() throws IOException {
        final SourceCodeRegistry sourceCodeRegistry = SourceCodeRegistryFactory.newInstance(dependencyGraph, localRepository);
        return sourceCodeRegistry;
    }
}
