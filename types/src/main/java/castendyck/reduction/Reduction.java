package castendyck.reduction;

import castendyck.callgraph.CallPath;
import de.castendyck.enforcing.NotNullConstraintEnforcer;

import java.util.List;

public class Reduction {
    private final List<CallPath> callPaths;
    private final String reason;

    Reduction(List<CallPath> callPaths, String reason) {
        NotNullConstraintEnforcer.ensureNotNull(callPaths);
        this.callPaths = callPaths;
        //NotNullConstraintEnforcer.ensureNotNull(reason);
        this.reason = reason;
    }

    public List<CallPath> getRemainingCallPaths() {
        return callPaths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reduction reduction = (Reduction) o;

        return callPaths.equals(reduction.callPaths);

    }

    public String getReason() {
        return reason;
    }

    @Override
    public int hashCode() {
        return callPaths.hashCode();
    }
}
