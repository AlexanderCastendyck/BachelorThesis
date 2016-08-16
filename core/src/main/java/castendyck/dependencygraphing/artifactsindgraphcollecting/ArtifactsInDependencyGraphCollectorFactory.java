package castendyck.dependencygraphing.artifactsindgraphcollecting;

import castendyck.dependencygraphing.artifactsindgraphcollecting.internal.ArtifactsInDependencyGraphCollectorImpl;

public class ArtifactsInDependencyGraphCollectorFactory {

    public static ArtifactsInDependencyGraphCollector newInstance(){
        return new ArtifactsInDependencyGraphCollectorImpl();
    }
}
