package castendyck.callgraph;

import java.util.List;

public class Given {
    public static When given(CallGraphCollectExamples.CallGraphCollectExample example) {
        final CallGraphCollectorConfiguration configuration = example.getConfiguration();
        final CallGraphCollectRequestDto callGraphCollectRequestDto = example.getCallGraphCollectRequestDto();
        final List<CallPath> expectedCallPaths = example.getExpectedCallPaths();
        return new When(configuration, callGraphCollectRequestDto, expectedCallPaths);
    }
}
