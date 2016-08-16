package castendyck.vulnerablepoint.cveinterpreting.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.bytecode.bytecodehandling.ByteCodeHandler;
import castendyck.callgraph.sourcecoderegistering.NoJarForThisArtifactIdentifierFoundException;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.streamfetching.StreamFetcher;
import castendyck.inmemorycompiling.ByteCode;
import org.mockito.Mockito;

import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static de.castendyck.collections.ExpectOneUniqueElementCollector.expectOneUniqueElement;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class ByteCodeHandlerWithoutLoadingClassesBuilder {
    private SourceCodeRegistry sourcecodeRegistry;
    private List<ByteCode> byteCodes;

    public static ByteCodeHandlerWithoutLoadingClassesBuilder aByteCodeHandler(){
        return new ByteCodeHandlerWithoutLoadingClassesBuilder();
    }

    public ByteCodeHandlerWithoutLoadingClassesBuilder with(SourceCodeRegistry sourcecodeRegistry) {
        this.sourcecodeRegistry = sourcecodeRegistry;
        return this;
    }

    public ByteCodeHandlerWithoutLoadingClassesBuilder with(List<ByteCode> byteCodes) {
        this.byteCodes = byteCodes;
        return this;
    }

    public ByteCodeHandler build() throws NoJarForThisArtifactIdentifierFoundException {
        final StreamFetcher mockStreamFetcher = mockStreamFetcher();

        return new ByteCodeHandler(sourcecodeRegistry, mockStreamFetcher);
    }
    private StreamFetcher mockStreamFetcher() throws NoJarForThisArtifactIdentifierFoundException {
        final StreamFetcher streamFetcher = Mockito.mock(StreamFetcher.class);
        when(streamFetcher.getClassFileAsStream(any())).thenCallRealMethod();

        for (ArtifactIdentifier artifactIdentifier : sourcecodeRegistry.getAllStoredArtifactIdentifier()) {
            final JarFile jarFile = sourcecodeRegistry.getJarFile(artifactIdentifier);
            mockForJarFile(streamFetcher, jarFile);
        }
        return streamFetcher;
    }

    private void mockForJarFile(StreamFetcher streamFetcher, JarFile jarFile) {
        final Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            final JarEntry jarEntry = entries.nextElement();
            final String jarEntryName = jarEntry.getName();
            if (jarEntryName.endsWith(".class")) {
                final String nameWithOutClass = jarEntryName.substring(0, jarEntryName.length() - 6);
                final ByteCode byteCode = getByteCodeFor(nameWithOutClass, byteCodes);
                when(streamFetcher.getRessourceAsStream(jarEntryName)).thenReturn(byteCode.getInputStream());
            }
        }
    }

    private static ByteCode getByteCodeFor(String name, List<ByteCode> byteCodes) {
        final ByteCode byteCode = byteCodes.stream()
                .filter(b -> b.getRelatedClassName().equals(name))
                .collect(expectOneUniqueElement());
        return byteCode;
    }
}
