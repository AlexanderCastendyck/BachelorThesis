package castendyck.localrepository;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.callgraph.JarFileFromByteCodeBuilder;
import castendyck.inmemorycompiling.ByteCode;
import castendyck.repository.LocalRepository;
import castendyck.repository.LocalRepositoryFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static de.castendyck.collections.ExpectOneUniqueElementCollector.expectOneUniqueElement;

public class LocalRepositoryBuilder {
    private final Map<ArtifactIdentifier, List<String>> map = new HashMap<>();
    private List<ByteCode> byteCodes;
    private Path repositoryRoot;

    public static LocalRepositoryBuilder aLocalRepository() {
        return new LocalRepositoryBuilder();
    }

    public LocalRepositoryBuilder containing(List<ByteCode> byteCodes) {
        this.byteCodes = byteCodes;
        return this;
    }

    public LocalRepositoryBuilder withClassStoredInJarFor(String className, ArtifactIdentifier artifact) {
        if (!map.containsKey(artifact)) {
            final ArrayList<String> classNames = new ArrayList<>();
            map.put(artifact, classNames);
        }
        final List<String> classesOfArtifact = map.get(artifact);
        classesOfArtifact.add(className);
        return this;
    }

    public LocalRepositoryBuilder withEmptyJarFor(ArtifactIdentifier artifact) {
        final ArrayList<String> classNames = new ArrayList<>();
        map.put(artifact, classNames);
        return this;
    }

    public LocalRepositoryBuilder locatedAt(String repositoryRoot) {
        final Path repositoryRootPath = Paths.get(repositoryRoot);
        return locatedAt(repositoryRootPath);
    }

    public LocalRepositoryBuilder locatedAt(Path repositoryRoot) {
        this.repositoryRoot = repositoryRoot;
        return this;
    }

    public Set<String> getStoredClasses(){
        final Set<String> containedClasses = map.keySet().stream()
                .map(map::get)
                .flatMap(List::stream)
                .collect(Collectors.toSet());
        return containedClasses;
    }

    public LocalRepository build() throws IOException {
        final LocalRepository localRepository = LocalRepositoryFactory.createNewInPath(repositoryRoot);
        for (Map.Entry<ArtifactIdentifier, List<String>> entry : map.entrySet()) {
            final ArtifactIdentifier artifactIdentifier = entry.getKey();
            final List<String> classesInArtifact = entry.getValue();
            final List<ByteCode> byteCodes = classesInArtifact.stream()
                    .map(this::findByteCodeFor)
                    .collect(Collectors.toList());

            final Path jarPath = localRepository.potentialJarFileLocationFor(artifactIdentifier);

            JarFileFromByteCodeBuilder.aJarFile()
                    .withByteCodesForAClass(byteCodes)
                    .storedAs(jarPath)
                    .build();
        }
        return localRepository;
    }

    private ByteCode findByteCodeFor(String className) {
        final castendyck.inmemorycompiling.ByteCode byteCode = byteCodes.stream()
                .filter(b -> b.getRelatedClassName().equals(className))
                .collect(expectOneUniqueElement());
        return byteCode;
    }
}
