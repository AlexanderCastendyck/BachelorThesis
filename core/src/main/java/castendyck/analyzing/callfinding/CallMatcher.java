package castendyck.analyzing.callfinding;

import castendyck.functionidentifier.FunctionIdentifier;

public interface CallMatcher {

    boolean matches(FunctionIdentifier functionIdentifier);
}
