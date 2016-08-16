package castendyck.analyzing.reducing.internal;

import castendyck.analyzing.reducing.Reducer;
import castendyck.processedcve.ProcessedCve;
import castendyck.reduction.Reduction;

public class ReductionSetup {
    private final Reduction previousReduction;
    private final Reducer reducer;
    private final Reduction expectedReduction;
    private ProcessedCve procesedCve;

    public ReductionSetup(Reduction previousReduction, Reducer reducer, Reduction expectedReduction) {
        this.previousReduction = previousReduction;
        this.reducer = reducer;
        this.expectedReduction = expectedReduction;
    }

    public Reduction getPreviousReduction() {
        return previousReduction;
    }

    public Reducer getReducer() {
        return reducer;
    }

    public Reduction getExpectedReduction() {
        return expectedReduction;
    }

    public ProcessedCve getProcesedCve() {
        return procesedCve;
    }
}
