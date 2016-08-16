package castendyck.callgraph;

import castendyck.callgraph.internal.CallGraphCollectorImpl;

public class CallGraphCollectorFactory {

    public static CallGraphCollector newInstance(CallGraphCollectorConfiguration configuration) {
        return new CallGraphCollectorImpl(configuration);
    }
}
