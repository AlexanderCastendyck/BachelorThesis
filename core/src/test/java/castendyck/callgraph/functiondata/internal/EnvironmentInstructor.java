package castendyck.callgraph.functiondata.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.classpath.ClassPath;
import castendyck.classpath.ClassPathFactory;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistry;
import castendyck.dependencygraphing.dependencyregistry.NotRegisteredArtifactException;
import castendyck.inmemoryjar.InMemoryJarFileBuilder;
import castendyck.callgraph.sourcecoderegistering.NoJarForThisArtifactIdentifierFoundException;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import org.mockito.Mockito;

import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

public class EnvironmentInstructor {
    private final Map<ArtifactIdentifier, List<ClassPath>> artifactToContainingClassPathsMap;
    private final SourceCodeRegistry sourceCodeRegistry;
    private final DependencyRegistry dependencyRegistry;
    private ArtifactIdentifier artifactIdentifierOfInterest;
    private List<ClassPath> classPathsOfArtifactOfInterest;

    private EnvironmentInstructor(SourceCodeRegistry sourceCodeRegistry, DependencyRegistry dependencyRegistry) {
        this.dependencyRegistry = dependencyRegistry;
        this.artifactToContainingClassPathsMap = new HashMap<>();
        this.sourceCodeRegistry = sourceCodeRegistry;
    }

    public static EnvironmentInstructor prepare(SourceCodeRegistry sourceCodeRegistry, DependencyRegistry dependencyRegistry) {
        return new EnvironmentInstructor(sourceCodeRegistry, dependencyRegistry);
    }
    public static EnvironmentInstructor prepare(SourceCodeRegistry sourceCodeRegistry) {
        final DependencyRegistry dependencyRegistry = Mockito.mock(DependencyRegistry.class);
        return new EnvironmentInstructor(sourceCodeRegistry, dependencyRegistry);
    }

    public EnvironmentInstructor registerArtifactWithClassPaths(ArtifactIdentifier artifactIdentifier, String... classPaths) {
        List<ClassPath> containedClassPaths = Arrays.stream(classPaths)
                .map(ClassPathFactory::createNew)
                .collect(Collectors.toList());
        artifactToContainingClassPathsMap.put(artifactIdentifier, containedClassPaths);
        return this;
    }

    public EnvironmentInstructor withArtifactIdentifierOfInterest(ArtifactIdentifier artifactIdentifierOfInterest) {
        this.artifactIdentifierOfInterest = artifactIdentifierOfInterest;
        return this;
    }

    public EnvironmentInstructor registerArtifactOfInterestWithClassPaths(String... classPaths) {
        final List<ClassPath> classPathsAsList = Arrays.stream(classPaths)
                .map(ClassPathFactory::createNew)
                .collect(Collectors.toList());
        this.classPathsOfArtifactOfInterest = classPathsAsList;
        return this;
    }

    public void startingNow() throws NoJarForThisArtifactIdentifierFoundException, NotRegisteredArtifactException {
        for (Map.Entry<ArtifactIdentifier, List<ClassPath>> entry : artifactToContainingClassPathsMap.entrySet()) {
            final ArtifactIdentifier artifactIdentifier = entry.getKey();
            final List<ClassPath> classPaths = entry.getValue();
            mockEnvironmentToReturnClassPathsForArtifactIdentifier(artifactIdentifier, classPaths);
        }
        mockEnvironmentToReturnClassPathsForArtifactIdentifier(artifactIdentifierOfInterest, classPathsOfArtifactOfInterest);

        final ArrayList<ArtifactIdentifier> artifactIdentifiersCallingArtifactIdentifierOfInterest = new ArrayList<>(artifactToContainingClassPathsMap.keySet());
        when(dependencyRegistry.getArtifactsThatDependOn(artifactIdentifierOfInterest)).thenReturn(artifactIdentifiersCallingArtifactIdentifierOfInterest);
    }

    private void mockEnvironmentToReturnClassPathsForArtifactIdentifier(ArtifactIdentifier artifactIdentifier, List<ClassPath> classPaths) throws NoJarForThisArtifactIdentifierFoundException {
        final List<String> classPathNames = classPaths.stream()
                .map(ClassPath::asString)
                .collect(Collectors.toList());
        final JarFile jarFile = InMemoryJarFileBuilder.aJarFile()
                .withJarEntriesWithNames(classPathNames)
                .build();

        when(sourceCodeRegistry.getJarFile(artifactIdentifier)).thenReturn(jarFile);
    }
}
