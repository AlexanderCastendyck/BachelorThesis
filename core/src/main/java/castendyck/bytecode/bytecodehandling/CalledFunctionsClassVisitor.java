package castendyck.bytecode.bytecodehandling;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.classidentifier.ClassIdentifier;
import castendyck.functionCalls.FunctionCalls;
import castendyck.callgraph.functiondata.internal.classpathtoartifactmapstoring.ClassPathsToArtifactIdentifierStorage;
import castendyck.functionidentifier.FunctionIdentifier;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.List;

import static castendyck.functionidentifier.FunctionIdentifierBuilder.aFunctionIdentifier;
import static org.objectweb.asm.Opcodes.ASM4;

public class CalledFunctionsClassVisitor extends ClassVisitor {
    private final ClassPathsToArtifactIdentifierStorage classPathsToArtifactIdentifierStorage;
    private final List<FunctionCallMethodVisitor> usedMethodVisitors;
    private final ArtifactIdentifier artifactIdentifier;
    private final ClassIdentifier classIdentifier;

    CalledFunctionsClassVisitor(ClassPathsToArtifactIdentifierStorage classPathsToArtifactIdentifierStorage, ArtifactIdentifier artifactIdentifier, ClassIdentifier classIdentifier) {
        super(ASM4);
        this.classPathsToArtifactIdentifierStorage = classPathsToArtifactIdentifierStorage;
        this.usedMethodVisitors = new ArrayList<>();
        this.artifactIdentifier = artifactIdentifier;
        this.classIdentifier = classIdentifier;
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        FunctionIdentifier currentFunctionIdentifier = aFunctionIdentifier()
                .withArtifactIdentifier(artifactIdentifier)
                .forClass(classIdentifier)
                .forFunction(name)
                .withSignature(desc)
                .build();

        final FunctionCallMethodVisitor methodVisitor = new FunctionCallMethodVisitor(classPathsToArtifactIdentifierStorage, currentFunctionIdentifier);
        usedMethodVisitors.add(methodVisitor);
        return methodVisitor;
    }

    public FunctionCalls getCalledFunctionsByThisClass() {
        FunctionCalls functionCalls = new FunctionCalls();

        for (FunctionCallMethodVisitor methodVisitor : usedMethodVisitors) {
            final FunctionCalls functionsCalledInsideMethod = methodVisitor.getFunctionCalls();
            functionCalls.addFunctionCalls(functionsCalledInsideMethod);
        }
        return functionCalls;
    }
}
