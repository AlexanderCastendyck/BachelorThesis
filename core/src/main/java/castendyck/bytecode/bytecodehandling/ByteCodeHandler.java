package castendyck.bytecode.bytecodehandling;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.classfile.ClassFile;
import castendyck.classidentifier.ClassIdentifier;
import castendyck.callgraph.functiondata.CouldNotExtractCalledFunctionsOutOfClassException;
import castendyck.functionCalls.FunctionCalls;
import castendyck.callgraph.functiondata.internal.classpathtoartifactmapstoring.ClassPathsToArtifactIdentifierStorage;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.streamfetching.StreamFetcher;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;

public class ByteCodeHandler {
    private final ClassPathsToArtifactIdentifierStorage classPathsToArtifactIdentifierStorage;
    private final StreamFetcher streamFetcher;

    public ByteCodeHandler(SourceCodeRegistry sourceCodeRegistry, StreamFetcher streamFetcher) {
        this.classPathsToArtifactIdentifierStorage = ClassPathsToArtifactIdentifierStorage.createFromSourceCodeRegistry(sourceCodeRegistry);
        this.streamFetcher = streamFetcher;
    }

    public FunctionCalls findFunctionsCallsOfClass(ClassIdentifier classIdentifier, ArtifactIdentifier artifactIdentifierOfClass) throws CouldNotExtractCalledFunctionsOutOfClassException {
        final ClassFile classPath = classIdentifier.getClassFile();
        final InputStream inputStream = streamFetcher.getClassFileAsStream(classPath);
        try {
            final ClassReader classReader = new ClassReader(inputStream);
            final CalledFunctionsClassVisitor classVisitor = new CalledFunctionsClassVisitor(classPathsToArtifactIdentifierStorage, artifactIdentifierOfClass, classIdentifier);
            classReader.accept(classVisitor, 0);

            final FunctionCalls functionCallsByThisClass = classVisitor.getCalledFunctionsByThisClass();
            return functionCallsByThisClass;
        } catch (IOException e) {
            final String message = e.getMessage();
            throw new CouldNotExtractCalledFunctionsOutOfClassException(message);
        }
    }
}
