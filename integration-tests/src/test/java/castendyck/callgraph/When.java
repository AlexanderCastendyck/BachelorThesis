package castendyck.callgraph;

import java.util.List;

public class When {
    private final CallGraphCollector callGraphCollector;
    private final CallGraphCollectRequestDto callGraphCollectRequestDto;
    private final List<CallPath> expectedCallPaths;

    public When(CallGraphCollectorConfiguration configuration, CallGraphCollectRequestDto callGraphCollectRequestDto, List<CallPath> expectedCallPaths) {
        this.callGraphCollector = CallGraphCollectorFactory.newInstance(configuration);
        this.callGraphCollectRequestDto = callGraphCollectRequestDto;
        this.expectedCallPaths = expectedCallPaths;
    }

    public Then whenCallGraphIsCollected() throws CallGraphCollectingException {
        final List<CallPath> callPaths = callGraphCollector.collectCallGraph(callGraphCollectRequestDto);
        return new Then(callPaths, expectedCallPaths);
    }
}
