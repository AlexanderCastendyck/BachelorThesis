package castendyck.dependencygraphing;

import castendyck.dependencygraphing.internal.DependencyGraphProviderImpl;

public class DependencyGraphProviderFactory {
    public static DependencyGraphProvider newInstance(DependencyGraphProviderConfiguration configuration){
        return new DependencyGraphProviderImpl(configuration);
    }
}
