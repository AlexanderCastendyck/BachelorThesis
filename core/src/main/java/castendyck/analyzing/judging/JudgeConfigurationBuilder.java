package castendyck.analyzing.judging;

import castendyck.dependencygraph.DependencyGraph;
import castendyck.repository.LocalRepository;

public class JudgeConfigurationBuilder {
    private DependencyGraph dependencyGraph;
    private LocalRepository localRepository;

    public static JudgeConfigurationBuilder aJudgeConfiguration() {
        return new JudgeConfigurationBuilder();
    }

    public JudgeConfigurationBuilder withDependencyGraph(DependencyGraph dependencyGraph) {
        this.dependencyGraph = dependencyGraph;
        return this;
    }

    public JudgeConfigurationBuilder withLocalRepository(LocalRepository localRepository) {
        this.localRepository = localRepository;
        return this;
    }

    public JudgeConfiguration build() {
        return new JudgeConfiguration(dependencyGraph, localRepository);
    }
}
