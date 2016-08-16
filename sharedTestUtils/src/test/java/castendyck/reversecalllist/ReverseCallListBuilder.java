package castendyck.reversecalllist;

import castendyck.functionCalls.FunctionCall;
import castendyck.functionCalls.FunctionCalls;
import castendyck.functionCalls.FunctionCallTestBuilder;
import castendyck.functionidentifier.FunctionIdentifier;

import java.util.ArrayList;
import java.util.List;

public class ReverseCallListBuilder {
    private final List<FunctionCallTestBuilder> functionCallTestBuilders;

    public ReverseCallListBuilder() {
        this.functionCallTestBuilders = new ArrayList<>();
    }

    public static ReverseCallListBuilder aReverseCallList() {
        return new ReverseCallListBuilder();
    }

    public ReverseCallListBuilder containingFunction(FunctionCallTestBuilder functionCallTestBuilder) {
        functionCallTestBuilders.add(functionCallTestBuilder);
        return this;
    }

    public ReverseCallList build() {
        final ReverseCallList reverseCallList = new ReverseCallList();
        for (FunctionCallTestBuilder functionCallTestBuilder : functionCallTestBuilders) {
            final FunctionCalls functionCalls = functionCallTestBuilder.build();
            for (FunctionCall functionCall : functionCalls) {
                final FunctionIdentifier targetFunction = functionCall.getTargetFunction();
                final FunctionIdentifier callingFunction = functionCall.getCallingFunction();
                reverseCallList.addFunctionBeingCalledBy(targetFunction, callingFunction);
            }
        }
        return reverseCallList;
    }
}
