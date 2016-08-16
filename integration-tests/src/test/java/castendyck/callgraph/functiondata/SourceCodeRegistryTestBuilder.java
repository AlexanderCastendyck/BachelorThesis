package castendyck.callgraph.functiondata;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.callgraph.sourcecoderegistering.internal.SourceCodeRegistryImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;

public class SourceCodeRegistryTestBuilder {
    private final Map<ArtifactIdentifier, JarFile> map;

    public SourceCodeRegistryTestBuilder() {
        map = new HashMap<>();
    }

    public static SourceCodeRegistryTestBuilder aSourceCodeRegistry(){
        return new SourceCodeRegistryTestBuilder();
    }

    public SourceCodeRegistryTestBuilder withJarFileRegisteredFor(JarFile jarFile, ArtifactIdentifier artifactIdentifier){
        map.put(artifactIdentifier, jarFile);
        return this;
    }

    public SourceCodeRegistry build(){
        return new SourceCodeRegistryImpl(map);
    }
}
