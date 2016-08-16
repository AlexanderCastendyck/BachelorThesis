package castendyck.callgraph.functiondata;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.classfile.ClassFile;
import castendyck.classfile.ClassFileFactory;
import de.castendyck.collections.ExpectOneUniqueElementCollector;
import castendyck.inmemoryjar.InMemoryJarFileBuilder;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.streamfetching.StreamFetcher;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistry;
import castendyck.dependencygraphing.dependencyregistry.NotRegisteredArtifactException;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functioninformation.CallGraphFunctionInformation;
import castendyck.inmemorycompiling.ByteCode;
import castendyck.inmemorycompiling.ByteCodeBuilder;
import castendyck.inmemorycompiling.classes.ClassSourceCodeBuilder;
import castendyck.inmemorycompiling.CompilationFinishedWithWarningsExceptions;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

class SourceCodeStateBuilder {
    private final List<ClassSourceCodeBuilder> referencedClasses;
    private final Map<ArtifactIdentifier, List<ClassFile>> registeredArtifacts;
    private ClassStateBuilder targetClassStateBuilder;
    private ArtifactIdentifier defaultArtifact;

    SourceCodeStateBuilder() {
        this.registeredArtifacts = new HashMap<>();
        this.referencedClasses = new ArrayList<>();


        this.defaultArtifact = ArtifactIdentifierBuilder.anArtifactIdentifier()
                .withGroupId("someGroup")
                .withArtifactId("artifact")
                .withVersion("1.0")
                .build();
    }

    public static SourceCodeStateBuilder aClass() {
        return new SourceCodeStateBuilder();
    }


    public ClassStateBuilder named(String className) {
        targetClassStateBuilder = new ClassStateBuilder(className, this, defaultArtifact);
        return targetClassStateBuilder;
    }

    void addReferencedClass(ClassSourceCodeBuilder classSourceCodeBuilder) {
        ensureClassNotAlreadyReferenced(classSourceCodeBuilder);
        referencedClasses.add(classSourceCodeBuilder);

    }

    private void ensureClassNotAlreadyReferenced(ClassSourceCodeBuilder classSourceCodeBuilder) {
        final String name = classSourceCodeBuilder.getName();
        final List<String> classesWithSameName = referencedClasses.stream()
                .map(ClassSourceCodeBuilder::getName)
                .filter(n -> n.equals(name))
                .collect(Collectors.toList());
        if (classesWithSameName.size() > 0) {
            throw new RuntimeException("Class with name " + name + " was already referenced");
        }
    }

    public SourceCodeState build() throws CompilationFinishedWithWarningsExceptions, IOException, NotRegisteredArtifactException {

        final ClassSourceCodeBuilder classSourceCodeBuilder = targetClassStateBuilder.build();
        DependencyRegistry dependencyRegistry = createMockedDependencyRegistry();
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(classSourceCodeBuilder)
                .withClasses(referencedClasses)
                .build();

        SourceCodeState sourceCodeState = new SourceCodeState();

        registerClassInArtifact(classSourceCodeBuilder.getName() + ".class", defaultArtifact);
        final SourceCodeRegistry sourceCodeRegistry = aSourceCodeRegistryFilledWith(byteCodes);


        final MockedStreamFetcher streamFetcher = new MockedStreamFetcher();
        streamFetcher.addByteCodes(byteCodes);
        final FunctionDataProvider functionDataProvider = FunctionDataProviderFactory.newInstance(sourceCodeRegistry, dependencyRegistry, streamFetcher);
        sourceCodeState.setFunctionDataProvider(functionDataProvider);
        FunctionIdentifier functionIdentifier = targetClassStateBuilder.buildFunctionIdentifier();
        sourceCodeState.setFunctionIdentifier(functionIdentifier);

        return sourceCodeState;
    }

    private DependencyRegistry createMockedDependencyRegistry() throws NotRegisteredArtifactException {
        DependencyRegistry dependencyRegistry = Mockito.mock(DependencyRegistry.class);

        final Set<ArtifactIdentifier> otherArtifacts = registeredArtifacts.keySet();
        final ArrayList<ArtifactIdentifier> dependingArtifact = new ArrayList<>(otherArtifacts);
        when(dependencyRegistry.getArtifactsThatDependOn(defaultArtifact)).thenReturn(dependingArtifact);

        final List<ArtifactIdentifier> emptyList = Collections.emptyList();
        for (ArtifactIdentifier otherArtifact : otherArtifacts) {
            when(dependencyRegistry.getArtifactsThatDependOn(otherArtifact)).thenReturn(emptyList);
        }
        return dependencyRegistry;
    }

    void registerClassDefaultArtifact(String className) {
        registerClassInArtifact(className, defaultArtifact);
    }

    void registerClassInArtifact(String className, ArtifactIdentifier artifactIdentifier) {
        if (!registeredArtifacts.containsKey(artifactIdentifier)) {
            final ArrayList<ClassFile> list = new ArrayList<>();
            registeredArtifacts.put(artifactIdentifier, list);
        }
        final List<ClassFile> classFiles = registeredArtifacts.get(artifactIdentifier);
        final ClassFile classFile = ClassFileFactory.createNew(className);
        classFiles.add(classFile);
    }

    private SourceCodeRegistry aSourceCodeRegistryFilledWith(List<ByteCode> byteCodes) {
        final SourceCodeRegistryTestBuilder sourceCodeRegistryTestBuilder = SourceCodeRegistryTestBuilder.aSourceCodeRegistry();
        for (Map.Entry<ArtifactIdentifier, List<ClassFile>> entry : registeredArtifacts.entrySet()) {
            final ArtifactIdentifier artifactIdentifier = entry.getKey();
            final List<ClassFile> classFiles = entry.getValue();

            final InMemoryJarFileBuilder jarFileBuilder = InMemoryJarFileBuilder.aJarFile();
            classFiles.stream()
                    .forEach(c -> jarFileBuilder.withAJarEntryWithName(c.getClassName()));
            final JarFile jarFile = jarFileBuilder.build();
            sourceCodeRegistryTestBuilder.withJarFileRegisteredFor(jarFile, artifactIdentifier);
        }
        final SourceCodeRegistry sourceCodeRegistry = sourceCodeRegistryTestBuilder.build();
        return sourceCodeRegistry;
    }

    public List<FunctionIdentifier> getExpectedCalls() {
        return targetClassStateBuilder.getExpectedCalls();
    }

    private class MockedStreamFetcher extends StreamFetcher {
        private final List<ByteCode> byteCodesToProvide = new ArrayList<>();

        public void addByteCodes(List<ByteCode> byteCodes) {
            byteCodesToProvide.addAll(byteCodes);
        }

        @Override
        public InputStream getRessourceAsStream(String ressource) {
            final ByteCode byteCode = byteCodesToProvide.stream()
                    .filter(b -> {
                        final String className = b.getRelatedClassName() + ".class";
                        return className.equals(ressource);
                    })
                    .collect(ExpectOneUniqueElementCollector.expectOneUniqueElement());
            return byteCode.getInputStream();
        }
    }

    class SourceCodeState {
        private FunctionDataProvider functionDataProvider;
        private FunctionIdentifier functionIdentifier;

        public void setFunctionDataProvider(FunctionDataProvider functionDataProvider) {
            this.functionDataProvider = functionDataProvider;
        }

        public void setFunctionIdentifier(FunctionIdentifier functionIdentifier) {
            this.functionIdentifier = functionIdentifier;
        }

        public CallGraphFunctionInformation getControlFlowInformation() throws CouldNotExtractCalledFunctionsOutOfClassException {
            final CallGraphFunctionInformation information = functionDataProvider.provideControlFlowInformation(functionIdentifier);
            return information;
        }
    }
}
