package castendyck.analyzing.reducing;

import castendyck.processedcve.ProcessedCve;
import castendyck.reduction.Reduction;

public interface Reducer {

    Reduction reduceFurther(Reduction previousReduction, ProcessedCve processedCve);

    String getReason();
}
