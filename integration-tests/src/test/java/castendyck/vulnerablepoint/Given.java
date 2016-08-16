package castendyck.vulnerablepoint;

import castendyck.maven.pomfile.PomFileCreationException;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.resolution.DependencyResolutionException;

import java.io.IOException;

public class Given {
    public static When given(ProjectWithVulnerablePointsBuilder projectWithVulnerablePointsBuilder) throws DependencyCollectionException, DependencyResolutionException, IOException, PomFileCreationException {
        final ProjectWithVulnerablePointsBuilder.ProjectWithVulnerablePoints projectWithVulnerablePoints = projectWithVulnerablePointsBuilder.build();
        return new When(projectWithVulnerablePoints);
    }
}
