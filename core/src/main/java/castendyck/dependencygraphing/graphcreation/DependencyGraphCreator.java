package castendyck.dependencygraphing.graphcreation;

import castendyck.dependencygraph.DependencyGraph;
import castendyck.maven.pomfile.PomFile;

public interface DependencyGraphCreator {

    DependencyGraph createDependencyGraphFor(PomFile pomFile) throws GraphCreationException;
}
