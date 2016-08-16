package castendyck.bytecode.bytecodehandling;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.classfile.ClassFile;
import castendyck.classfile.ClassFileFactory;
import castendyck.classpath.ClassPath;
import castendyck.functionCalls.FunctionCall;
import castendyck.functionCalls.FunctionCalls;
import castendyck.callgraph.functiondata.internal.classpathtoartifactmapstoring.ClassPathsToArtifactIdentifierStorage;
import castendyck.callgraph.functiondata.internal.classpathtoartifactmapstoring.NoArtifactIdentifierRegisteredForThisClassPathException;
import castendyck.functionidentifier.FunctionIdentifier;
import de.castendyck.javaapi.JavaApiClassMatcher;
import org.apache.log4j.Logger;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static castendyck.functionidentifier.FunctionIdentifierBuilder.aFunctionIdentifier;

public class FunctionCallMethodVisitor extends MethodVisitor {
    private final static Logger logger = Logger.getLogger(FunctionCallMethodVisitor.class);
    private final ClassPathsToArtifactIdentifierStorage classPathsToArtifactIdentifierStorage;
    private final FunctionCalls functionCalls;
    private final FunctionIdentifier currentFunction;


    FunctionCallMethodVisitor(ClassPathsToArtifactIdentifierStorage classPathsToArtifactIdentifierStorage, FunctionIdentifier currentFunction) {
        super(Opcodes.ASM4);
        this.classPathsToArtifactIdentifierStorage = classPathsToArtifactIdentifierStorage;
        this.functionCalls = new FunctionCalls();
        this.currentFunction = currentFunction;
    }

    @Override
    public void visitMethodInsn(int i, String owner, String name, String desc, boolean isInterface) {
        try {
            final FunctionIdentifier targetFunctionIdentifier = mapToFunctionIdentifier(owner, name, desc);
            final FunctionCall functionCall = new FunctionCall(currentFunction, targetFunctionIdentifier);
            functionCalls.addFunctionCalls(functionCall);
        } catch (NoArtifactIdentifierRegisteredForThisClassPathException e) {
            logger.error("Error encountered during visiting byte code analysis of invoke instruction");
            logger.error(owner + "." + name + "could not be associated with an known Artifact");

            final String className = currentFunction.getClassIdentifier().getClassName();
            final String functionName = currentFunction.getFunctionName().asString();
            logger.debug("Occurred in " + className + "." + functionName + " for " +owner+"."+name+" "+desc);
        }
    }

    private FunctionIdentifier mapToFunctionIdentifier(String owner, String name, String signature) throws NoArtifactIdentifierRegisteredForThisClassPathException {
        final String className;
        if (JavaApiClassMatcher.isClassFromJavaApi(owner)) {
            className = owner;
        } else {
            className = owner + ".class";
        }
        final ClassFile classFile = ClassFileFactory.createNew(className);
        final ArtifactIdentifier artifactIdentifier = extractArtifactIdentifier(classFile);
        final FunctionIdentifier functionIdentifier = aFunctionIdentifier()
                .withArtifactIdentifier(artifactIdentifier)
                .forClass(classFile)
                .forFunction(name)
                .withSignature(signature)
                .build();
        return functionIdentifier;
    }

    private ArtifactIdentifier extractArtifactIdentifier(ClassFile classFile) throws NoArtifactIdentifierRegisteredForThisClassPathException {
        final ClassPath classPath = classFile.toClassPath();
        final ArtifactIdentifier artifactIdentifier = classPathsToArtifactIdentifierStorage.getArtifactIdentifierFor(classPath);
        return artifactIdentifier;
    }

    public FunctionCalls getFunctionCalls() {
        return functionCalls;
    }
}
