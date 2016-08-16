package castendyck.functionCalls;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.classidentifier.ClassIdentifier;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functionidentifier.FunctionIdentifierBuilder;

import java.util.ArrayList;
import java.util.List;

import static castendyck.functionidentifier.FunctionIdentifierBuilder.aFunctionIdentifier;

public class FunctionCallTestBuilder {
    private List<FunctionCall> functionCallList;
    private FunctionIdentifier functionIdentifierCalled;

    public FunctionCallTestBuilder(FunctionIdentifier functionIdentifierCalled) {
        this.functionCallList = new ArrayList<>();
        this.functionIdentifierCalled = functionIdentifierCalled;
    }

    public static FunctionCallTestBuilder aFunction(ArtifactIdentifier artifact, String className, String function) {
        final FunctionIdentifier functionIdentifier = FunctionIdentifierBuilder.aFunctionIdentifier()
                .withArtifactIdentifier(artifact)
                .forClass(className)
                .forFunction(function)
                .build();
        return aFunction(functionIdentifier);
    }

    public static FunctionCallTestBuilder aFunction(FunctionIdentifier functionIdentifier) {
        return new FunctionCallTestBuilder(functionIdentifier);
    }

    public FunctionCallTestBuilder calledBy(List<FunctionIdentifier> calledFunctionIdentifiers) {
        for (FunctionIdentifier calledFunctionIdentifier : calledFunctionIdentifiers) {
            calledBy(calledFunctionIdentifier);
        }
        return this;
    }

    public FunctionCallTestBuilder calledBy(String className, String functionName) {
        final ArtifactIdentifier artifactIdentifier = functionIdentifierCalled.getArtifactIdentifier();
        final ClassIdentifier classIdentifier = new ClassIdentifier(className);
        final FunctionIdentifier calledFunctionIdentifier = aFunctionIdentifier()
                .withArtifactIdentifier(artifactIdentifier)
                .forClass(classIdentifier)
                .forFunction(functionName)
                .build();
        return calledBy(calledFunctionIdentifier);
    }

    public FunctionCallTestBuilder calledBy(ArtifactIdentifier artifact, String className, String function) {
        final FunctionIdentifier functionIdentifier = FunctionIdentifierBuilder.aFunctionIdentifier()
                .withArtifactIdentifier(artifact)
                .forClass(className)
                .forFunction(function)
                .build();
        return calledBy(functionIdentifier);
    }

    public FunctionCallTestBuilder calledBy(FunctionIdentifier calledFunctionIdentifier) {
        final FunctionCall functionCall = new FunctionCall(calledFunctionIdentifier, functionIdentifierCalled);
        this.functionCallList.add(functionCall);
        return this;
    }

    public FunctionCallTestBuilder notCalled() {
        this.functionCallList.clear();
        return this;
    }

    public FunctionCalls build() {
        final FunctionCalls functionCalls = new FunctionCalls();
        functionCallList.forEach(functionCalls::addFunctionCalls);
        return functionCalls;
    }
}
