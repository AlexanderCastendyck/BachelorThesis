package castendyck.dependencygraphing.graphcreation;

import castendyck.dependencygraphing.graphfetching.DependencyGraphFetcher;
import castendyck.dependencygraphing.graphcreation.internal.DependencyGraphCreatorImpl;
import castendyck.maven.pomfile.PomFile;

public class DependencyGraphCreatorFactory {

    public static DependencyGraphCreator newInstance(DependencyGraphFetcher dependencyGraphFetcher, PomFile parentPom){
        return new DependencyGraphCreatorImpl(dependencyGraphFetcher, parentPom);
    }
}
