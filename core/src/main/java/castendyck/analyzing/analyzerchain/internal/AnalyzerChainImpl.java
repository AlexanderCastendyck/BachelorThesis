package castendyck.analyzing.analyzerchain.internal;

import castendyck.analyzing.analyzerchain.AnalyzerChain;
import castendyck.analyzing.judging.Judge;
import castendyck.analyzing.reducing.Reducer;
import castendyck.callgraph.CallPath;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.judgement.Judgement;
import castendyck.processedcve.ProcessedCve;
import castendyck.reduction.Reduction;
import castendyck.reduction.ReductionBuilder;

import java.util.List;

public class AnalyzerChainImpl implements AnalyzerChain {
    private final List<Reducer> reducerList;
    private final Judge judge;

    public AnalyzerChainImpl(List<Reducer> reducerList, Judge judge) {
        NotNullConstraintEnforcer.ensureNotNull(reducerList);
        this.reducerList = reducerList;
        NotNullConstraintEnforcer.ensureNotNull(judge);
        this.judge = judge;
    }

    @Override
    public Judgement traverse(ProcessedCve processedCve) {
        final Reduction initialReduction = ReductionBuilder.aReduction()
                .containing(processedCve.getCallPaths())
                .build();

        Reduction lastReduction = initialReduction;
        for (Reducer reducer : reducerList) {
            final Reduction newReduction = reducer.reduceFurther(lastReduction, processedCve);
            lastReduction = newReduction;
            if(noMoreReductionNeeded(newReduction)){
                break;
            }
        }

        final Judgement judgement = judge.makeJudgement(lastReduction, processedCve);
        return judgement;
    }

    private boolean noMoreReductionNeeded(Reduction reduction) {
        final List<CallPath> remainingCallPaths = reduction.getRemainingCallPaths();
        final boolean noeMoreReductionNeeded = remainingCallPaths.isEmpty();
        return noeMoreReductionNeeded;
    }
}
