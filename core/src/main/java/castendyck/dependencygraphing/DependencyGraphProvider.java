package castendyck.dependencygraphing;

import castendyck.dependencygraph.DependencyGraph;
import castendyck.maven.pomfile.PomFile;

public interface DependencyGraphProvider {

    DependencyGraph provideDependencyGraphFor(PomFile pomFile) throws DependencyGraphProvidingException;

}
