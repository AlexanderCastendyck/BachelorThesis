package castendyck.bytecode.dependentartifactcalling.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.bytecode.reversecalllist.ReverseCallListCreator;
import castendyck.functionCalls.FunctionCallTestBuilder;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functionidentifier.FunctionIdentifierAbbreviatedBuilder;
import castendyck.reversecalllist.ReverseCallList;
import castendyck.reversecalllist.ReverseCallListBuilder;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class DependentCallRegisterer {
    private final FunctionIdentifier functionIdentifier;
    private final List<FunctionIdentifier> calls;

    public DependentCallRegisterer(FunctionIdentifier functionIdentifier) {
        this.functionIdentifier = functionIdentifier;
        this.calls = new ArrayList<>();
    }

    public static DependentCallRegisterer letFunction(ArtifactIdentifier artifactIdentifier, String classname, String function) {
        final FunctionIdentifier functionIdentifier = FunctionIdentifierAbbreviatedBuilder.aFunctionIdentifierFor(artifactIdentifier, classname, function);
        return letFunction(functionIdentifier);
    }

    public static DependentCallRegisterer letFunction(FunctionIdentifier functionIdentifier) {
        return new DependentCallRegisterer(functionIdentifier);
    }

    public DependentCallRegisterer beCalledByDependentArtifact(FunctionIdentifier functionIdentifier) {
        calls.add(functionIdentifier);
        return this;
    }

    public ReverseCallListCreator whenUsingThisReverseCallListCreator() {
        final ReverseCallList reverseCallList = ReverseCallListBuilder.aReverseCallList()
                .containingFunction(FunctionCallTestBuilder.aFunction(functionIdentifier)
                        .calledBy(calls))
                .build();

        final ReverseCallListCreator reverseCallListCreator = Mockito.mock(ReverseCallListCreator.class);
        when(reverseCallListCreator.createForArtifact(any())).thenReturn(reverseCallList);
        return reverseCallListCreator;
    }

    public DependentCallRegisterer notBeCalled() {
        calls.clear();
        return this;
    }
}
