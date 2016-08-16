package castendyck.dependencygraphing;

import castendyck.dependencygraph.DependencyGraph;
import castendyck.maven.pomfile.PomFile;

public class When {
    private final DependencyGraphProviderConfiguration configuration;
    private final PomFile pomFile;

    public When(DependencyStateBuilder.DependencyState dependencyState) {
        configuration = dependencyState.getConfiguration();
        pomFile = dependencyState.getPomFile();
    }

    public Then graphIsCreated() throws Exception {
        final DependencyGraphProvider dependencyGraphProvider = DependencyGraphProviderFactory.newInstance(configuration);
        final DependencyGraph dependencyGraph = dependencyGraphProvider.provideDependencyGraphFor(pomFile);
        return new Then(dependencyGraph);
    }
}
