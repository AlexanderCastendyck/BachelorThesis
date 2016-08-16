package castendyck.reversecalllist;

import castendyck.functionidentifier.FunctionIdentifier;

import java.util.*;

public class ReverseCallList {
    private final Map<FunctionIdentifier, List<FunctionIdentifier>> calledFunctions;

    public ReverseCallList() {
        calledFunctions = new HashMap<>();
    }

    public void addFunctionBeingCalledBy(FunctionIdentifier targetFunction, FunctionIdentifier callingFunction) {
        if (!calledFunctions.containsKey(targetFunction)) {
            final ArrayList<FunctionIdentifier> emptyList = new ArrayList<>();
            calledFunctions.put(targetFunction, emptyList);
        }
        final List<FunctionIdentifier> listOfCalledFunctions = calledFunctions.get(targetFunction);
        listOfCalledFunctions.add(callingFunction);
    }

    public List<FunctionIdentifier> getFunctionsCalling(FunctionIdentifier functionIdentifier) {
        final List<FunctionIdentifier> functionsCalled = calledFunctions.get(functionIdentifier);
        if(functionsCalled != null) {
            return functionsCalled;
        }else {
            return new ArrayList<>();
        }
    }

    public Set<FunctionIdentifier> getAllCalledFunctions(){
        final Set<FunctionIdentifier> calledFunctions = this.calledFunctions.keySet();
        return calledFunctions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReverseCallList that = (ReverseCallList) o;

        return calledFunctions.equals(that.calledFunctions);

    }

    @Override
    public int hashCode() {
        return calledFunctions.hashCode();
    }
}
