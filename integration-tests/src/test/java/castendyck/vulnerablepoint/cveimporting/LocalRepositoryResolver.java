package castendyck.vulnerablepoint.cveimporting;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.repository.TestRepositories;
import castendyck.repository.TestRepositorySession;
import castendyck.repository.TestRepositorySystem;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;

import java.nio.file.Path;
import java.util.List;

public class LocalRepositoryResolver {
    private final RepositorySystem repositorySystem;
    private final RepositorySystemSession session;
    private final List<RemoteRepository> remoteRepositories;


    public LocalRepositoryResolver(Path repositoryRoot) {
        this.repositorySystem = TestRepositorySystem.getRepositorySystem();
        this.session = TestRepositorySession.getSessionForLocalRepo(repositorySystem, repositoryRoot);
        this.remoteRepositories = TestRepositories.getRepositories();
    }

    public void resolve(List<ArtifactIdentifier> artifactIdentifiers) throws DependencyCollectionException, DependencyResolutionException {
        for (ArtifactIdentifier artifactIdentifier : artifactIdentifiers) {
            resolve(artifactIdentifier);
        }
    }

    public void resolve(ArtifactIdentifier artifactIdentifier) throws DependencyCollectionException, DependencyResolutionException {
        CollectRequest collectRequest = new CollectRequest();
        final Dependency dependency = mapToDependency(artifactIdentifier);
        collectRequest.setRoot(dependency);
        collectRequest.setRepositories(remoteRepositories);

        final CollectResult collectResult = repositorySystem.collectDependencies(session, collectRequest);

        DependencyRequest dependencyRequest = new DependencyRequest();
        DependencyNode node = collectResult.getRoot();
        dependencyRequest.setRoot(node);

        repositorySystem.resolveDependencies(session, dependencyRequest);
    }

    private Dependency mapToDependency(ArtifactIdentifier artifactIdentifier) {
        final String groupId = artifactIdentifier.getGroupId();
        final String artifactId = artifactIdentifier.getArtifactId();
        final String version = artifactIdentifier.getVersion();
        final String coords = groupId + ":" + artifactId + ":" + version;
        final DefaultArtifact artifact = new DefaultArtifact(coords);

        Dependency dependency = new Dependency(artifact, "compile");
        return dependency;
    }
}
