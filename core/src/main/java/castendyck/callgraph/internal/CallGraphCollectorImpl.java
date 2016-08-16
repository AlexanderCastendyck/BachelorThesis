package castendyck.callgraph.internal;

import castendyck.callgraph.CallGraphCollectRequestDto;
import castendyck.callgraph.CallGraphCollectingException;
import castendyck.callgraph.CallGraphCollector;
import castendyck.callgraph.CallGraphCollectorConfiguration;
import castendyck.callgraph.*;
import castendyck.callgraph.callgraphcreating.CallGraphCreator;
import castendyck.callgraph.callgraphcreating.CallGraphCreatorFactory;
import castendyck.callgraph.functiondata.FunctionDataProvider;
import castendyck.callgraph.functiondata.FunctionDataProviderFactory;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistryFactory;
import castendyck.streamfetching.StreamFetcher;
import castendyck.callgraphstrategy.CallGraphStrategy;
import castendyck.callgraphstrategy.StrategyUnexpectedStateException;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistry;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistryBuilder;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.repository.LocalRepository;

import java.util.List;

public class CallGraphCollectorImpl implements CallGraphCollector {
    private final LocalRepository localRepository;
    private final StreamFetcher streamFetcher;

    public CallGraphCollectorImpl(CallGraphCollectorConfiguration callGraphCollectorConfiguration) {
        localRepository = callGraphCollectorConfiguration.getLocalRepository();
        streamFetcher = callGraphCollectorConfiguration.getStreamFetcher();
    }

    @Override
    public List<CallPath> collectCallGraph(CallGraphCollectRequestDto callGraphCollectRequestDto) throws CallGraphCollectingException {
        final CallGraphCreator callGraphCreator = createControlFlowAnalyzer(callGraphCollectRequestDto);
        try {
            final CallGraphStrategy strategy = callGraphCollectRequestDto.getStrategy();
            return callGraphCreator.analyzeControlFlow(strategy);
        } catch (StrategyUnexpectedStateException e) {
            throw new CallGraphCollectingException(e);
        }
    }

    private CallGraphCreator createControlFlowAnalyzer(CallGraphCollectRequestDto callGraphCollectRequestDto) {
        final DependencyGraph dependencyGraph = callGraphCollectRequestDto.getDependencyGraph();
        final SourceCodeRegistry sourceCodeRegistry = SourceCodeRegistryFactory.newInstance(dependencyGraph, localRepository);
        DependencyRegistry dependencyRegistry = DependencyRegistryBuilder.aDependencyRegistry()
                .initializedWithGraph(dependencyGraph)
                .build();
        FunctionDataProvider functionDataProvider = FunctionDataProviderFactory.newInstance(sourceCodeRegistry, dependencyRegistry, streamFetcher);

        final CallGraphCreator callGraphCreator = CallGraphCreatorFactory.newInstanceForReversedCallGraphAnalysis(functionDataProvider);
        return callGraphCreator;
    }
}
