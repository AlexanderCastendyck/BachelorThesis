package castendyck.callgraph.functiondata.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.bytecode.calllist.CallListCreator;
import castendyck.callgraph.functiondata.FunctionDataProvider;
import castendyck.calllist.CallList;
import castendyck.reversecalllist.ReverseCallList;
import castendyck.bytecode.reversecalllist.ReverseCallListCreator;
import castendyck.callgraph.functiondata.CouldNotExtractCalledFunctionsOutOfClassException;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functioninformation.CallGraphFunctionInformation;
import castendyck.functioninformation.DataFlowFunctionInformation;
import castendyck.functioninformation.GeneralFunctionInformation;
import org.apache.log4j.Logger;

import java.util.List;

public class FunctionDataProviderImpl implements FunctionDataProvider {
    private final static Logger logger = Logger.getLogger(FunctionDataProviderImpl.class);
    private final CallListCreator callListCreator;
    private final ReverseCallListCreator reverseCallListCreator;

    public FunctionDataProviderImpl(CallListCreator callListCreator, ReverseCallListCreator reverseCallListCreator) {
        this.callListCreator = callListCreator;
        this.reverseCallListCreator = reverseCallListCreator;
    }


    public GeneralFunctionInformation provideFunctionInformation(FunctionIdentifier functionIdentifier) {
        return null;
    }

    public CallGraphFunctionInformation provideControlFlowInformation(FunctionIdentifier functionIdentifier) throws CouldNotExtractCalledFunctionsOutOfClassException {
        final List<FunctionIdentifier> calledFunctions = findCalledFunctions(functionIdentifier);
        final List<FunctionIdentifier> functionsCalling = findCallingFunctions(functionIdentifier);
        final CallGraphFunctionInformation callGraphFunctionInformation = new CallGraphFunctionInformation(functionIdentifier, calledFunctions, functionsCalling);
        return callGraphFunctionInformation;
    }

    private List<FunctionIdentifier> findCalledFunctions(FunctionIdentifier functionIdentifier) {
        final CallList callList = callListCreator.createForArtifact(functionIdentifier.getArtifactIdentifier());
        final List<FunctionIdentifier> calledFunctions = callList.getCalledFunctionsFor(functionIdentifier);
        return calledFunctions;
    }

    private List<FunctionIdentifier> findCallingFunctions(FunctionIdentifier functionIdentifier) {
        final ArtifactIdentifier artifactIdentifier = functionIdentifier.getArtifactIdentifier();
        final ReverseCallList reverseCallList = reverseCallListCreator.createForArtifact(artifactIdentifier);
        return reverseCallList.getFunctionsCalling(functionIdentifier);
    }


    public DataFlowFunctionInformation provideDataFlowInformation(FunctionIdentifier functionIdentifier) {
        throw new UnsupportedOperationException();
    }
}
