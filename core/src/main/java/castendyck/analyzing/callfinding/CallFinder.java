package castendyck.analyzing.callfinding;

import castendyck.callgraph.CallPath;
import castendyck.functionidentifier.FunctionIdentifier;

import java.util.ArrayList;
import java.util.List;

public class CallFinder {

    public static List<FunctionIdentifier> findCallsThatMatch(CallPath callPath, CallMatcher callMatcher){
        final List<FunctionIdentifier> matchingCalls = new ArrayList<>();
        for (FunctionIdentifier functionIdentifier : callPath.getFunctions()) {
            if(callMatcher.matches(functionIdentifier)){
                matchingCalls.add(functionIdentifier);
            }
        }
        return matchingCalls;
    }
}
