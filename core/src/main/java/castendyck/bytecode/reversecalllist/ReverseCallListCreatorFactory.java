package castendyck.bytecode.reversecalllist;

import castendyck.bytecode.bytecodehandling.ByteCodeHandler;
import castendyck.bytecode.calllist.CallListCreator;
import castendyck.bytecode.calllist.CallListCreatorFactory;
import castendyck.bytecode.reversecalllist.internal.ReverseCallListCreatorImpl;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistry;
import castendyck.streamfetching.StreamFetcher;

public class ReverseCallListCreatorFactory {

    public static ReverseCallListCreator newInstance(DependencyRegistry dependencyRegistry, SourceCodeRegistry sourceCodeRegistry){
        final StreamFetcher streamFetcher = new StreamFetcher();
        return newInstance(dependencyRegistry, sourceCodeRegistry, streamFetcher);
    }

    public static ReverseCallListCreator newInstance(DependencyRegistry dependencyRegistry, SourceCodeRegistry sourceCodeRegistry, StreamFetcher streamFetcher){
        final ByteCodeHandler byteCodeHandler = new ByteCodeHandler(sourceCodeRegistry, streamFetcher);
        return newInstance(dependencyRegistry, sourceCodeRegistry, byteCodeHandler);
    }
    public static ReverseCallListCreator newInstance(DependencyRegistry dependencyRegistry, SourceCodeRegistry sourceCodeRegistry, ByteCodeHandler byteCodeHandler){
        final CallListCreator callListCreator = CallListCreatorFactory.newInstance(sourceCodeRegistry, byteCodeHandler);
        return new ReverseCallListCreatorImpl(callListCreator, dependencyRegistry);
    }
}
