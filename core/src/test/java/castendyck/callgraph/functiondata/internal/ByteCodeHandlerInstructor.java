package castendyck.callgraph.functiondata.internal;

import castendyck.classidentifier.ClassIdentifier;
import castendyck.callgraph.functiondata.CouldNotExtractCalledFunctionsOutOfClassException;
import castendyck.bytecode.bytecodehandling.ByteCodeHandler;
import castendyck.functionCalls.FunctionCalls;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class ByteCodeHandlerInstructor {
    private final ByteCodeHandler byteCodeHandler;
    private final Map<ClassIdentifier, FunctionCallBuilder> mockingCommands;

    private ByteCodeHandlerInstructor(ByteCodeHandler byteCodeHandler) {
        this.byteCodeHandler = byteCodeHandler;
        this.mockingCommands = new HashMap<>();
    }

    public static ByteCodeHandlerInstructor instruct(ByteCodeHandler byteCodeHandler) {
        return new ByteCodeHandlerInstructor(byteCodeHandler);
    }

    public ByteCodeHandlerInstructor havingClass(String className, FunctionCallBuilder functionCallBuilder) {
        final ClassIdentifier classIdentifier = new ClassIdentifier(className);
        return havingClass(classIdentifier, functionCallBuilder);
    }

    public ByteCodeHandlerInstructor havingClass(ClassIdentifier classIdentifier, FunctionCallBuilder functionCallBuilder) {
        mockingCommands.put(classIdentifier, functionCallBuilder);
        return this;
    }

    public void startingNow() throws CouldNotExtractCalledFunctionsOutOfClassException {
        for (Map.Entry<ClassIdentifier, FunctionCallBuilder> entry : mockingCommands.entrySet()) {
            final FunctionCallBuilder functionCallBuilder = entry.getValue();
            final FunctionCalls functionCalls = functionCallBuilder.build();

            final ClassIdentifier classIdentifier = entry.getKey();
            when(byteCodeHandler.findFunctionsCallsOfClass(eq(classIdentifier), any())).thenReturn(functionCalls);
        }
    }
}
