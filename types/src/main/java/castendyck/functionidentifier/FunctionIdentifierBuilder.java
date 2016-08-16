package castendyck.functionidentifier;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.classfile.ClassFile;
import castendyck.classidentifier.ClassIdentifier;
import castendyck.functionname.FunctionName;
import castendyck.functionname.FunctionNameFactory;
import castendyck.functionsignature.FunctionSignature;
import castendyck.functionsignature.FunctionSignatureFactory;

public class FunctionIdentifierBuilder {
    private ArtifactIdentifier artifactIdentifier;
    private ClassIdentifier classIdentifier;
    private FunctionName functionName;
    private FunctionSignature functionSignature;

    private FunctionIdentifierBuilder() {
        functionSignature = FunctionSignatureFactory.createNew("()V");
    }

    public static FunctionIdentifierBuilder aFunctionIdentifier() {
        return new FunctionIdentifierBuilder();
    }

    public FunctionIdentifierBuilder withArtifactIdentifier(ArtifactIdentifier artifactIdentifier) {
        this.artifactIdentifier = artifactIdentifier;
        return this;
    }

    public FunctionIdentifierBuilder forClass(String className) {
        final ClassIdentifier classIdentifier = new ClassIdentifier(className);
        return forClass(classIdentifier);
    }

    public FunctionIdentifierBuilder forClass(ClassFile classFile) {
        final ClassIdentifier classIdentifier = new ClassIdentifier(classFile);
        return forClass(classIdentifier);
    }

    public FunctionIdentifierBuilder forClass(ClassIdentifier classIdentifier) {
        this.classIdentifier = classIdentifier;
        return this;
    }

    public FunctionIdentifierBuilder forFunction(String functionName) {
        this.functionName = FunctionNameFactory.createNew(functionName);
        return this;
    }
    public FunctionIdentifierBuilder withSignature(String signature) {
        this.functionSignature = FunctionSignatureFactory.createNew(signature);
        return this;
    }

    public FunctionIdentifier build() {
        FunctionIdentifier functionIdentifier = new FunctionIdentifier(artifactIdentifier, classIdentifier, functionName, functionSignature);
        return functionIdentifier;
    }
}
