package castendyck.callgraph.functiondata;

import castendyck.bytecode.calllist.CallListCreator;
import castendyck.bytecode.calllist.CallListCreatorFactory;
import castendyck.bytecode.reversecalllist.ReverseCallListCreator;
import castendyck.bytecode.reversecalllist.ReverseCallListCreatorFactory;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistry;
import castendyck.bytecode.bytecodehandling.ByteCodeHandler;
import castendyck.callgraph.functiondata.internal.FunctionDataProviderImpl;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.streamfetching.StreamFetcher;

public class FunctionDataProviderFactory {
    public static FunctionDataProvider newInstance(SourceCodeRegistry sourceCodeRegistry, DependencyRegistry dependencyRegistry) {
        final StreamFetcher streamFetcher = new StreamFetcher();
        return newInstance(sourceCodeRegistry, dependencyRegistry);
    }
    public static FunctionDataProvider newInstance(SourceCodeRegistry sourceCodeRegistry, DependencyRegistry dependencyRegistry, StreamFetcher streamFetcher) {
        final ByteCodeHandler byteCodeHandler = new ByteCodeHandler(sourceCodeRegistry, streamFetcher);
        final CallListCreator callListCreator = CallListCreatorFactory.newInstance(sourceCodeRegistry, byteCodeHandler);
        final ReverseCallListCreator reverseCallListCreator = ReverseCallListCreatorFactory.newInstance(dependencyRegistry, sourceCodeRegistry, streamFetcher);
        return new FunctionDataProviderImpl(callListCreator, reverseCallListCreator);
    }
}
