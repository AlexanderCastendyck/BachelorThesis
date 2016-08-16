package castendyck.reduction;

import castendyck.callgraph.CallPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReductionBuilder {
    private final List<CallPath> callPaths;
    private  String reason;

    public ReductionBuilder() {
        this.callPaths = new ArrayList<>();
        this.reason = "";
    }

    public static ReductionBuilder aReduction(){
        return new ReductionBuilder();
    }

    public ReductionBuilder containing(List<CallPath> callPaths){
        this.callPaths.addAll(callPaths);
        return this;
    }
    public ReductionBuilder containing(CallPath ... callPaths){
        Arrays.stream(callPaths)
                .forEach(this.callPaths::add);
        return this;
    }
    public ReductionBuilder withReason(String reason){
        this.reason = reason;
        return this;
    }

    public Reduction build(){
        return new Reduction(callPaths, reason);
    }
}
