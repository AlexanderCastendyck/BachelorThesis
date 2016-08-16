package castendyck.callgraph.sourcecoderegistering;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.callgraph.sourcecoderegistering.internal.DependencyGraphToJarArtifactMapMapper;
import castendyck.callgraph.sourcecoderegistering.internal.SourceCodeRegistryImpl;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.repository.LocalRepository;

import java.util.Map;
import java.util.jar.JarFile;

public class SourceCodeRegistryFactory {

    public static SourceCodeRegistry newInstance(DependencyGraph dependencyGraph, LocalRepository localRepository) {
        Map<ArtifactIdentifier, JarFile> map = DependencyGraphToJarArtifactMapMapper.map(dependencyGraph, localRepository);
        final SourceCodeRegistryImpl sourceCodeRegistry = new SourceCodeRegistryImpl(map);
        return sourceCodeRegistry;
    }
}
