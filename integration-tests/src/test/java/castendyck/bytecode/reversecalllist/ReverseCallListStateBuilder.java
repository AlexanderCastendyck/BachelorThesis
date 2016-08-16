package castendyck.bytecode.reversecalllist;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraphing.DependencyGraphTestBuilder;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistry;
import castendyck.inmemorycompiling.ByteCode;
import castendyck.inmemorycompiling.ByteCodeBuilder;
import castendyck.localrepository.LocalRepositoryBuilder;
import castendyck.repository.LocalRepository;
import castendyck.reversecalllist.ReverseCallList;
import castendyck.streamfetching.MockedStreamFetcher;
import castendyck.streamfetching.StreamFetcher;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static castendyck.reversecalllist.ReverseCallListBuilder.aReverseCallList;
import static castendyck.dependencygraphing.GraphNodeTestBuilder.aGraphNode;
import static castendyck.dependencygraphing.dependencyregistry.DependencyRegistryBuilder.aDependencyRegistry;
import static castendyck.functionCalls.FunctionCallTestBuilder.aFunction;
import static castendyck.inmemorycompiling.classes.NormalClassSourceCodeBuilder.sourceCodeForAClass;
import static castendyck.inmemorycompiling.methods.NormalMethodSourceCodeBuilder.sourceCodeForAMethod;
import static castendyck.vulnerablepoint.cveinterpreting.internal.SourceCodeRegistryBuilder.aSourceCodeRegistry;

public class ReverseCallListStateBuilder {
    private static final Path repository_root = Paths.get("target", "ReverseCallListStateBuilderTests");
    private final ArtifactIdentifier artifact;
    private final DependencyGraph dependencyGraph;
    private final LocalRepository localRepository;
    private final ReverseCallList expectedReverseCallList;
    private final StreamFetcher streamFetcher;

    public ReverseCallListStateBuilder(ArtifactIdentifier artifact, DependencyGraph dependencyGraph, LocalRepository localRepository, ReverseCallList expectedReverseCallList, StreamFetcher streamFetcher) {
        this.artifact = artifact;
        this.dependencyGraph = dependencyGraph;
        this.localRepository = localRepository;
        this.expectedReverseCallList = expectedReverseCallList;
        this.streamFetcher = streamFetcher;
    }

    public static ReverseCallListStateBuilder aFunctionNotCalled() throws Exception {
        final ArtifactIdentifier artifact = ArtifactIdentifierBuilder.buildShort("local", "name", "1.0");
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("Main")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function")))
                .build();

        final Path repositoryRoot = repository_root.resolve("TestWitAFunctionNotCalled");
        final LocalRepository localRepository = LocalRepositoryBuilder.aLocalRepository()
                .containing(byteCodes)
                .withClassStoredInJarFor("Main", artifact)
                .locatedAt(repositoryRoot)
                .build();

        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact(artifact))
                .build();

        final ReverseCallList expectedReverseCallList = aReverseCallList()
                .containingFunction(aFunction(artifact, "Main.class", "function")
                        .notCalled())
                .build();

        final MockedStreamFetcher mockedStreamFetcher = MockedStreamFetcher.forByteCodes(byteCodes);

        return new ReverseCallListStateBuilder(artifact, dependencyGraph, localRepository, expectedReverseCallList, mockedStreamFetcher);
    }

    public static ReverseCallListStateBuilder aFunctionCalledOnce() throws Exception {
        final ArtifactIdentifier artifact = ArtifactIdentifierBuilder.buildShort("local", "name", "1.0");
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("Main")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function")))
                .forAClass(sourceCodeForAClass()
                        .named("CallingClass")
                        .withAMethod(sourceCodeForAMethod()
                                .named("callingFunction")
                                .callingUsingTempObject("Main", "function")))
                .build();

        final Path repositoryRoot = repository_root.resolve("TestWitAFunctionCalledOnce");
        final LocalRepository localRepository = LocalRepositoryBuilder.aLocalRepository()
                .containing(byteCodes)
                .withClassStoredInJarFor("Main", artifact)
                .withClassStoredInJarFor("CallingClass", artifact)
                .locatedAt(repositoryRoot)
                .build();

        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact(artifact))
                .build();

        final ReverseCallList expectedReverseCallList = aReverseCallList()
                .containingFunction(aFunction(artifact, "Main.class", "function")
                        .calledBy("CallingClass.class", "callingFunction"))
                .build();

        final MockedStreamFetcher mockedStreamFetcher = MockedStreamFetcher.forByteCodes(byteCodes);

        return new ReverseCallListStateBuilder(artifact, dependencyGraph, localRepository, expectedReverseCallList, mockedStreamFetcher);
    }

    public static ReverseCallListStateBuilder aFunctionCalledTwice() throws Exception {
        final ArtifactIdentifier artifact = ArtifactIdentifierBuilder.buildShort("local", "name", "1.0");
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("Main")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function")))
                .forAClass(sourceCodeForAClass()
                        .named("CallingClass1")
                        .withAMethod(sourceCodeForAMethod()
                                .named("callingFunction1")
                                .callingUsingTempObject("Main", "function")))
                .forAClass(sourceCodeForAClass()
                        .named("CallingClass2")
                        .withAMethod(sourceCodeForAMethod()
                                .named("callingFunction2")
                                .callingUsingTempObject("Main", "function")))
                .build();

        final Path repositoryRoot = repository_root.resolve("TestWitAFunctionCalledTwice");
        final LocalRepository localRepository = LocalRepositoryBuilder.aLocalRepository()
                .containing(byteCodes)
                .withClassStoredInJarFor("Main", artifact)
                .withClassStoredInJarFor("CallingClass1", artifact)
                .withClassStoredInJarFor("CallingClass2", artifact)
                .locatedAt(repositoryRoot)
                .build();

        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact(artifact))
                .build();

        final ReverseCallList expectedReverseCallList = aReverseCallList()
                .containingFunction(aFunction(artifact, "Main.class", "function")
                        .calledBy("CallingClass1.class", "callingFunction1")
                        .calledBy("CallingClass2.class", "callingFunction2"))
                .build();

        final MockedStreamFetcher mockedStreamFetcher = MockedStreamFetcher.forByteCodes(byteCodes);

        return new ReverseCallListStateBuilder(artifact, dependencyGraph, localRepository, expectedReverseCallList, mockedStreamFetcher);
    }

    public static ReverseCallListStateBuilder aFunctionCalledFromAnotherArtifact() throws Exception {
        final ArtifactIdentifier artifact = ArtifactIdentifierBuilder.buildShort("local", "name", "1.0");
        final ArtifactIdentifier dependingArtifact = ArtifactIdentifierBuilder.buildShort("local", "depending", "1.0");
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("Main")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function")))
                .forAClass(sourceCodeForAClass()
                        .named("CallingClass")
                        .withAMethod(sourceCodeForAMethod()
                                .named("callingFunction")
                                .callingUsingTempObject("Main", "function")))
                .build();

        final Path repositoryRoot = repository_root.resolve("TestWitAFunctionCalledFromAnotherArtifact");
        final LocalRepository localRepository = LocalRepositoryBuilder.aLocalRepository()
                .containing(byteCodes)
                .withClassStoredInJarFor("Main", artifact)
                .withClassStoredInJarFor("CallingClass", dependingArtifact)
                .locatedAt(repositoryRoot)
                .build();

        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact(dependingArtifact)
                        .withChildren(aGraphNode()
                                .forArtifact(artifact)))
                .build();

        final ReverseCallList expectedReverseCallList = aReverseCallList()
                .containingFunction(aFunction(artifact, "Main.class", "function")
                        .calledBy(dependingArtifact, "CallingClass.class", "callingFunction"))
                .build();

        final MockedStreamFetcher mockedStreamFetcher = MockedStreamFetcher.forByteCodes(byteCodes);

        return new ReverseCallListStateBuilder(artifact, dependencyGraph, localRepository, expectedReverseCallList, mockedStreamFetcher);
    }

    public static ReverseCallListStateBuilder aFunctionCalledFromAFunctionThatIsAlsoCalled() throws Exception {
        final ArtifactIdentifier artifact = ArtifactIdentifierBuilder.buildShort("local", "name", "1.0");
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("Main")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function")))
                .forAClass(sourceCodeForAClass()
                        .named("MidClass")
                        .withAMethod(sourceCodeForAMethod()
                                .named("midFunction")
                                .callingUsingTempObject("Main", "function")))
                .forAClass(sourceCodeForAClass()
                        .named("TopClass")
                        .withAMethod(sourceCodeForAMethod()
                                .named("topFunction")
                                .callingUsingTempObject("MidClass", "midFunction")))
                .build();

        final Path repositoryRoot = repository_root.resolve("TestWitAFunctionCalledFromAFunctionThatIsAlsoCalled");
        final LocalRepository localRepository = LocalRepositoryBuilder.aLocalRepository()
                .containing(byteCodes)
                .withClassStoredInJarFor("Main", artifact)
                .withClassStoredInJarFor("MidClass", artifact)
                .withClassStoredInJarFor("TopClass", artifact)
                .locatedAt(repositoryRoot)
                .build();

        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact(artifact))
                .build();

        final ReverseCallList expectedReverseCallList = aReverseCallList()
                .containingFunction(aFunction(artifact, "Main.class", "function")
                        .calledBy("MidClass.class", "midFunction"))
                .containingFunction(aFunction(artifact, "MidClass.class", "midFunction")
                        .calledBy("TopClass.class", "topFunction"))
                .build();

        final MockedStreamFetcher mockedStreamFetcher = MockedStreamFetcher.forByteCodes(byteCodes);

        return new ReverseCallListStateBuilder(artifact, dependencyGraph, localRepository, expectedReverseCallList, mockedStreamFetcher);
    }

    public State build() throws IOException {
        final DependencyRegistry dependencyRegistry = aDependencyRegistry()
                .initializedWithGraph(dependencyGraph)
                .build();
        final SourceCodeRegistry sourceCodeRegistry = aSourceCodeRegistry()
                .with(dependencyGraph)
                .with(localRepository)
                .build();
        return new State(dependencyRegistry, artifact, sourceCodeRegistry, expectedReverseCallList, streamFetcher);
    }

    public class State {
        private final DependencyRegistry dependencyRegistry;
        private final ArtifactIdentifier artifact;
        private final SourceCodeRegistry sourceCodeRegistry;
        private final ReverseCallList expectedReverseCallList;
        private final StreamFetcher streamFetcher;

        public State(DependencyRegistry dependencyRegistry, ArtifactIdentifier artifact, SourceCodeRegistry sourceCodeRegistry, ReverseCallList expectedReverseCallList, StreamFetcher streamFetcher) {
            this.dependencyRegistry = dependencyRegistry;
            this.artifact = artifact;
            this.sourceCodeRegistry = sourceCodeRegistry;
            this.expectedReverseCallList = expectedReverseCallList;
            this.streamFetcher = streamFetcher;
        }

        public DependencyRegistry getDependencyRegistry() {
            return dependencyRegistry;
        }

        public ArtifactIdentifier getArtifact() {
            return artifact;
        }

        public SourceCodeRegistry getSourceCodeRegistry() {
            return sourceCodeRegistry;
        }

        public ReverseCallList getExpectedReverseCallList() {
            return expectedReverseCallList;
        }

        public StreamFetcher getStreamFetcher() {
            return streamFetcher;
        }
    }
}
