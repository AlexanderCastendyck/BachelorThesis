package castendyck.callgraph.callgraphcreating;

import castendyck.callgraph.functiondata.CouldNotExtractCalledFunctionsOutOfClassException;
import castendyck.functionidentifier.FunctionIdentifier;

import java.util.List;

public interface Direction {

    List<FunctionIdentifier> getNextFunctionsToTraverseAfter(FunctionIdentifier functionIdentifier) throws CouldNotExtractCalledFunctionsOutOfClassException;

}
