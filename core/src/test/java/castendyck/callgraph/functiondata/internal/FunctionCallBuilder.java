package castendyck.callgraph.functiondata.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.classidentifier.ClassIdentifier;
import castendyck.functionCalls.FunctionCall;
import castendyck.functionCalls.FunctionCalls;
import castendyck.functionidentifier.FunctionIdentifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static castendyck.functionidentifier.FunctionIdentifierBuilder.aFunctionIdentifier;

public class FunctionCallBuilder {
    private List<FunctionCall> functionCallList;
    private FunctionIdentifier functionIdentifierCalled;

    public FunctionCallBuilder(FunctionIdentifier functionIdentifierCalled) {
        this.functionCallList = new ArrayList<>();
        this.functionIdentifierCalled = functionIdentifierCalled;
    }

    public static FunctionCallBuilder CalledFunctionsFor(FunctionIdentifier functionIdentifier) {
        return new FunctionCallBuilder(functionIdentifier);
    }

    public FunctionCallBuilder calledBy(List<FunctionIdentifier> calledFunctionIdentifiers) {
        for (FunctionIdentifier calledFunctionIdentifier : calledFunctionIdentifiers) {
            calledBy(calledFunctionIdentifier);
        }
        return this;
    }

    public FunctionCallBuilder calledBy(String className, String functionName) {
        final ArtifactIdentifier artifactIdentifier = functionIdentifierCalled.getArtifactIdentifier();
        final ClassIdentifier classIdentifier = new ClassIdentifier(className);
        final FunctionIdentifier calledFunctionIdentifier = aFunctionIdentifier()
                .withArtifactIdentifier(artifactIdentifier)
                .forClass(classIdentifier)
                .forFunction(functionName)
                .build();
        return calledBy(calledFunctionIdentifier);
    }

    public FunctionCallBuilder calledBy(FunctionIdentifier calledFunctionIdentifier) {
        final FunctionCall functionCall = new FunctionCall(calledFunctionIdentifier, functionIdentifierCalled);
        this.functionCallList.add(functionCall);
        return this;
    }

    public FunctionCallBuilder calling(FunctionIdentifier ...calledFunctions ){
        final List<FunctionIdentifier> list = Arrays.asList(calledFunctions);
        return calling(list);
    }

    public FunctionCallBuilder calling(List<FunctionIdentifier> calledFunctionIdentifiers) {
        calledFunctionIdentifiers.forEach(this::calling);
        return this;
    }

    public FunctionCallBuilder calling(FunctionIdentifier calledFunctionIdentifier) {
        final FunctionCall functionCall = new FunctionCall(functionIdentifierCalled, calledFunctionIdentifier);
        this.functionCallList.add(functionCall);
        return this;
    }

    public FunctionCallBuilder callingNoFunction() {
        this.functionCallList.clear();
        return this;
    }

    public FunctionCalls build() {
        final FunctionCalls functionCalls = new FunctionCalls();
        functionCallList.forEach(functionCalls::addFunctionCalls);
        return functionCalls;
    }
}
