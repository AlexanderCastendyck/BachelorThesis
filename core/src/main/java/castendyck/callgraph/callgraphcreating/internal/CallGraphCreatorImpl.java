package castendyck.callgraph.callgraphcreating.internal;

import castendyck.callgraph.CallPath;
import castendyck.callgraph.callgraphcreating.CallGraphCreator;
import castendyck.callgraph.callgraphcreating.Direction;
import castendyck.callgraph.functiondata.CouldNotExtractCalledFunctionsOutOfClassException;
import castendyck.callgraphstrategy.CallGraphStrategy;
import castendyck.callgraphstrategy.StrategyUnexpectedStateException;
import castendyck.classidentifier.ClassIdentifier;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functionname.FunctionName;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class CallGraphCreatorImpl implements CallGraphCreator {
    private final static Logger logger = Logger.getLogger(CallGraphCreatorImpl.class);
    private final Direction direction;
    private List<CallPath> callPaths;
    private List<CallPath> finishedCallPaths;

    public CallGraphCreatorImpl(Direction direction) {
        this.direction = direction;
    }


    @Override
    public List<CallPath> analyzeControlFlow(CallGraphStrategy strategy) throws StrategyUnexpectedStateException {
        callPaths = new ArrayList<>();
        finishedCallPaths = new ArrayList<>();

        CallPath currentCallPath = null;

        while (!strategy.isFinished(callPaths)) {
            currentCallPath = strategy.determineNextCallPath(currentCallPath, callPaths);
            processControlFlow(currentCallPath);
        }
        return finishedCallPaths;
    }

    private void processControlFlow(CallPath currentCallPath) {
        try {
            final FunctionIdentifier currentFunctionIdentifier = currentCallPath.getCurrentFunctionIdentifier();
            final List<FunctionIdentifier> nextFunctions = direction.getNextFunctionsToTraverseAfter(currentFunctionIdentifier);

            if (functionIsNotCalledByAnyone(nextFunctions)) {
                markFunctionAsFinished(currentCallPath);
            } else {
                final List<CallPath> newCallPaths = createControlFlowsFromCurrentControlFlow(currentCallPath, nextFunctions);

                //Special case, that occurs, if a recursive Function is not called by anyone
                if (newCallPaths.size() == 0) {
                    finishedCallPaths.add(currentCallPath);
                }
                callPaths.addAll(newCallPaths);
                callPaths.remove(currentCallPath);
            }
        } catch (CouldNotExtractCalledFunctionsOutOfClassException e) {
            FunctionIdentifier currentFunctionIdentifier = currentCallPath.getCurrentFunctionIdentifier();
            ClassIdentifier classIdentifier = currentFunctionIdentifier.getClassIdentifier();
            String className = classIdentifier.getClassName();
            FunctionName functionName = currentFunctionIdentifier.getFunctionName();
            logger.warn("Could not extract functions calling " + className + ":" + functionName.asString() + ". Reason: " + e.getMessage());
        }
    }

    private void markFunctionAsFinished(CallPath currentCallPath) {
        callPaths.remove(currentCallPath);
        finishedCallPaths.add(currentCallPath);
    }

    private List<CallPath> createControlFlowsFromCurrentControlFlow(CallPath currentCallPath, List<FunctionIdentifier> nextFunctions) {
        final List<CallPath> newCallPaths = new ArrayList<>();
        for (FunctionIdentifier callingFunction : nextFunctions) {
            if (!currentCallPath.containsFunctionIdentifier(callingFunction)) {
                final CallPath newCallPath = currentCallPath.cloneCallPath();
                newCallPath.addFunctionIdentifier(callingFunction);
                newCallPaths.add(newCallPath);
            }
        }
        return newCallPaths;
    }

    private boolean functionIsNotCalledByAnyone(List<FunctionIdentifier> functions) {
        return functions.isEmpty();
    }
}
