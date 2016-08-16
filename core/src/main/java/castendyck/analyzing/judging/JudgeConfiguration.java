package castendyck.analyzing.judging;

import castendyck.dependencygraph.DependencyGraph;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.repository.LocalRepository;

public class JudgeConfiguration {
    private final DependencyGraph dependencyGraph;
    private final LocalRepository localRepository;

    JudgeConfiguration(DependencyGraph dependencyGraph, LocalRepository localRepository) {
        NotNullConstraintEnforcer.ensureNotNull(dependencyGraph);
        this.dependencyGraph = dependencyGraph;
        NotNullConstraintEnforcer.ensureNotNull(localRepository);
        this.localRepository = localRepository;
    }

    public DependencyGraph getDependencyGraph() {
        return dependencyGraph;
    }

    public LocalRepository getLocalRepository() {
        return localRepository;
    }
}
