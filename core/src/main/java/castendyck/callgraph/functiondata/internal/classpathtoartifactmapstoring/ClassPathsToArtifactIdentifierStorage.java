package castendyck.callgraph.functiondata.internal.classpathtoartifactmapstoring;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierForJavaApi;
import castendyck.classpath.ClassPath;
import castendyck.classpath.ClassPathFactory;
import castendyck.callgraph.sourcecoderegistering.NoJarForThisArtifactIdentifierFoundException;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import de.castendyck.javaapi.JavaApiClassMatcher;
import de.castendyck.utils.ThisShouldNeverHappenException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class ClassPathsToArtifactIdentifierStorage {
    private final Map<ClassPath, ArtifactIdentifier> storedMappingInformation;

    public ClassPathsToArtifactIdentifierStorage() {
        storedMappingInformation = new HashMap<>();
    }

    public static ClassPathsToArtifactIdentifierStorage createFromSourceCodeRegistry(SourceCodeRegistry sourceCodeRegistry) {
        final Set<ArtifactIdentifier> artifactIdentifiers = sourceCodeRegistry.getAllStoredArtifactIdentifier();
        ClassPathsToArtifactIdentifierStorage storage = new ClassPathsToArtifactIdentifierStorage();

        for (ArtifactIdentifier artifactIdentifier : artifactIdentifiers) {
            final JarFile jarFile = getJarFileFromSourceCodeRegistry(sourceCodeRegistry, artifactIdentifier);
            final List<ClassPath> classPaths = extractAllClassPathsFromJarFIle(jarFile);
            storage.storeInformationForArtifactIdentifier(classPaths, artifactIdentifier);
        }
        return storage;
    }

    private static JarFile getJarFileFromSourceCodeRegistry(SourceCodeRegistry sourceCodeRegistry, ArtifactIdentifier artifactIdentifier)  {
        try {
            return sourceCodeRegistry.getJarFile(artifactIdentifier);
        } catch (NoJarForThisArtifactIdentifierFoundException e) {
            throw new ThisShouldNeverHappenException(e);
        }
    }

    private static List<ClassPath> extractAllClassPathsFromJarFIle(JarFile jarFile) {
        return jarFile.stream()
                .map(JarEntry::getName)
                .map(ClassPathFactory::createNew)
                .collect(Collectors.toList());
    }

    public ArtifactIdentifier getArtifactIdentifierFor(ClassPath classPath) throws NoArtifactIdentifierRegisteredForThisClassPathException {
        final String packagePart = classPath.getPackagePart();
        if (JavaApiClassMatcher.isClassFromJavaApi(packagePart)) {
            return ArtifactIdentifierForJavaApi.createNew();
        }
        if (storedMappingInformation.containsKey(classPath)) {
            final ArtifactIdentifier artifactIdentifier = storedMappingInformation.get(classPath);
            return artifactIdentifier;
        } else {
            throw new NoArtifactIdentifierRegisteredForThisClassPathException("No ArtifactIdentifier was registered for classpath " + classPath);
        }
    }

    void storeInformationForArtifactIdentifier(List<ClassPath> classPaths, ArtifactIdentifier artifactIdentifier) {
        for (ClassPath classPath : classPaths) {
            storedMappingInformation.put(classPath, artifactIdentifier);
        }
    }
}
