package castendyck.callgraph.controlflowanalyzer.internal;

import castendyck.functioninformation.CallGraphFunctionInformation;
import castendyck.callgraph.functiondata.FunctionDataProvider;
import castendyck.callgraph.functiondata.CouldNotExtractCalledFunctionsOutOfClassException;
import castendyck.functionidentifier.FunctionIdentifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public class CallRegisterer {
    private final FunctionIdentifier functionIdentifier;
    private List<FunctionIdentifier> callingFunctions;
    private FunctionDataProvider functionDataProvider;

    private CallRegisterer(FunctionIdentifier functionIdentifier, FunctionDataProvider functionDataProvider) {
        this.functionIdentifier = functionIdentifier;
        this.functionDataProvider = functionDataProvider;
        this.callingFunctions = new ArrayList<>();
    }

    static CallRegisterer letFunctionAtProvider(FunctionIdentifier functionIdentifier, FunctionDataProvider functionDataProvider) {
        return new CallRegisterer(functionIdentifier, functionDataProvider);
    }

    public void notBeCalled() throws CouldNotExtractCalledFunctionsOutOfClassException {
        callingFunctions = new ArrayList<>();
        instructMockedControlFlowAnalyzer();
    }

    private void instructMockedControlFlowAnalyzer() throws CouldNotExtractCalledFunctionsOutOfClassException {
        final List<FunctionIdentifier> calledFunctions = new ArrayList<>();
        final CallGraphFunctionInformation information = new CallGraphFunctionInformation(functionIdentifier, calledFunctions, callingFunctions);
        when(functionDataProvider.provideControlFlowInformation(functionIdentifier)).thenReturn(information);
    }

    public void beCalledBy(FunctionIdentifier... functionIdentifiers) throws CouldNotExtractCalledFunctionsOutOfClassException {
        Arrays.stream(functionIdentifiers)
                .forEach(this::registerCall);
        instructMockedControlFlowAnalyzer();
    }

    public void beCalledBy(FunctionIdentifier functionIdentifier) throws CouldNotExtractCalledFunctionsOutOfClassException {
        registerCall(functionIdentifier);
        instructMockedControlFlowAnalyzer();
    }

    private boolean registerCall(FunctionIdentifier functionIdentifier) {
        return callingFunctions.add(functionIdentifier);
    }
}
