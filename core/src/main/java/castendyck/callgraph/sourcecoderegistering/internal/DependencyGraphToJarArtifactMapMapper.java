package castendyck.callgraph.sourcecoderegistering.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.callgraph.sourcecoderegistering.NoJarForThisArtifactIdentifierFoundException;
import castendyck.dependency.Dependency;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraph.GraphNode;
import castendyck.repository.LocalRepository;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

public final class DependencyGraphToJarArtifactMapMapper {
    private final static Logger logger = Logger.getLogger(DependencyGraphToJarArtifactMapMapper.class);
    private final LocalRepository localRepository;
    private final Map<ArtifactIdentifier, JarFile> map;

    public DependencyGraphToJarArtifactMapMapper(LocalRepository localRepository) {
        this.localRepository = localRepository;
        this.map = new HashMap<>();
    }

    public static Map<ArtifactIdentifier, JarFile> map(DependencyGraph dependencyGraph, LocalRepository localRepository) {
        final DependencyGraphToJarArtifactMapMapper mapper = new DependencyGraphToJarArtifactMapMapper(localRepository);
        Map<ArtifactIdentifier, JarFile> map = mapper.map(dependencyGraph);
        return map;
    }

    private Map<ArtifactIdentifier, JarFile> map(DependencyGraph dependencyGraph) {
        final GraphNode rootNode = dependencyGraph.getRootNode();
        fillNodeDataIntoMap(rootNode);
        return map;
    }

    private void fillNodeDataIntoMap(GraphNode node) {
        final Dependency dependency = node.getRelatedDependency();
        final ArtifactIdentifier artifactIdentifier = dependency.getArtifactIdentifier();
        storeJarInMapFor(artifactIdentifier);

        final List<GraphNode> children = node.getChildren();
        for (GraphNode childNode : children) {
            fillNodeDataIntoMap(childNode);
        }
    }

    private void storeJarInMapFor(ArtifactIdentifier artifactIdentifier) {
        try {
            final JarFile jarFile = locateJarFileInLocalRepo(artifactIdentifier);
            map.put(artifactIdentifier, jarFile);
        } catch (NoJarForThisArtifactIdentifierFoundException e) {
            logger.error("Could not load artifact " + artifactIdentifier.asSimpleString() + ", because the jar file was not found.");
        }
    }

    private JarFile locateJarFileInLocalRepo(ArtifactIdentifier artifactIdentifier) throws NoJarForThisArtifactIdentifierFoundException {
        final Path jarPath = localRepository.potentialJarFileLocationFor(artifactIdentifier);

        final JarFile jarFile = findJarFileFor(jarPath);
        return jarFile;
    }

    private JarFile findJarFileFor(Path jarPath) throws NoJarForThisArtifactIdentifierFoundException {
        final File file = jarPath.toFile();
        final JarFile jarFile;
        try {
            jarFile = new JarFile(file);
        } catch (IOException e) {
            throw new NoJarForThisArtifactIdentifierFoundException(e);
        }
        return jarFile;
    }
}
