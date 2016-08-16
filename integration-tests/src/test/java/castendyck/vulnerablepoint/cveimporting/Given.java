package castendyck.vulnerablepoint.cveimporting;

import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.resolution.DependencyResolutionException;

public class Given {

    public static When given(VulnerableMavenProjectBuilder vulnerableMavenProjectBuilder) throws DependencyCollectionException, DependencyResolutionException {
        final VulnerableMavenProjectState projectState = vulnerableMavenProjectBuilder.build();

        return new When(projectState);
    }
}
