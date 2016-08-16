package castendyck.analyzing.reducing.internal;

import castendyck.analyzing.reducing.Reducer;
import castendyck.processedcve.ProcessedCve;
import castendyck.reduction.Reduction;

public class When {
    private final ReductionSetup setup;

    public When(ReductionSetup setup) {
        this.setup = setup;
    }

    public Then whenReduced() {
        final Reduction previousReduction = setup.getPreviousReduction();
        final Reducer reducer = setup.getReducer();
        final ProcessedCve processedCve = setup.getProcesedCve();
        final Reduction reduction = reducer.reduceFurther(previousReduction, processedCve);

        final Reduction expectedReduction = setup.getExpectedReduction();
        return new Then(reduction, expectedReduction);
    }
}
