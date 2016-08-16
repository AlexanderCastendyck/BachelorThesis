package castendyck.analyzing.reducing.internal;

import castendyck.callgraph.CallPath;
import castendyck.reduction.Reduction;
import castendyck.reduction.ReductionBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReductionTestBuilder {
    private final List<CallPath> callPaths = new ArrayList<>();

    public static ReductionTestBuilder aReduction(){
        return new ReductionTestBuilder();
    }
    public ReductionTestBuilder containing(CallPath ... callPaths){
        Arrays.stream(callPaths)
                .forEach(this.callPaths::add);
        return this;
    }

    public ReductionTestBuilder containingNoCallPaths() {
        callPaths.clear();
        return this;
    }

    public Reduction build() {
        final Reduction reduction = ReductionBuilder.aReduction()
                .containing(callPaths)
                .build();
        return reduction;
    }
}
