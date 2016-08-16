package castendyck.bytecode.calllist;

import castendyck.bytecode.bytecodehandling.ByteCodeHandler;
import castendyck.bytecode.calllist.internal.CallListCreatorImpl;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;

public class CallListCreatorFactory {
    public static CallListCreator newInstance(SourceCodeRegistry sourceCodeRegistry, ByteCodeHandler byteCodeHandler){
        return new CallListCreatorImpl(sourceCodeRegistry, byteCodeHandler);
    }
}
