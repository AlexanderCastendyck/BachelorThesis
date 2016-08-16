package castendyck.calllist;

import castendyck.functionCalls.FunctionCall;
import castendyck.functionCalls.FunctionCalls;
import castendyck.functionidentifier.FunctionIdentifier;

import java.util.*;

public class CallList {
    private final Map<FunctionIdentifier, List<FunctionIdentifier>> calledFunctions;

    public CallList() {
        calledFunctions = new HashMap<>();
    }

    public void addFunctionCalls(FunctionCalls functionCalls) {
        for (FunctionCall functionCall : functionCalls) {
            addFunctionCall(functionCall);
        }
    }

    public void addFunctionCall(FunctionCall functionCall) {
        final FunctionIdentifier callingFunction = functionCall.getCallingFunction();
        if (!calledFunctions.containsKey(callingFunction)) {
            final ArrayList<FunctionIdentifier> emptyList = new ArrayList<>();
            calledFunctions.put(callingFunction, emptyList);
        }
        final List<FunctionIdentifier> listOfCalledFunctions = calledFunctions.get(callingFunction);
        final FunctionIdentifier targetFunction = functionCall.getTargetFunction();
        listOfCalledFunctions.add(targetFunction);
    }

    public List<FunctionIdentifier> getCalledFunctionsFor(FunctionIdentifier functionIdentifier) {
        final List<FunctionIdentifier> functionsCalled = calledFunctions.get(functionIdentifier);
        if(functionsCalled != null) {
            return functionsCalled;
        }else {
            return new ArrayList<>();
        }
    }

    public Set<FunctionIdentifier> getAllCallers(){
        return calledFunctions.keySet();
    }
}
