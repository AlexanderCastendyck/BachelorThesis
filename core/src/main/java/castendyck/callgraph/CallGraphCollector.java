package castendyck.callgraph;

import java.util.List;

public interface CallGraphCollector {

    List<CallPath> collectCallGraph(CallGraphCollectRequestDto callGraphCollectRequestDto) throws CallGraphCollectingException;
}
