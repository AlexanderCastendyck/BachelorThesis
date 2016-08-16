package castendyck.functionCalls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class FunctionCalls implements Iterable<FunctionCall> {
    private final List<FunctionCall> functionCalls;

    public static FunctionCalls noFunctionCalls(){
        return new FunctionCalls();
    }

    public FunctionCalls() {
        functionCalls = new ArrayList<>();
    }

    public void addFunctionCalls(FunctionCall functionCall){
        functionCalls.add(functionCall);
    }

    public void addFunctionCalls(FunctionCalls calledFunction){
        final List<FunctionCall> functionsToAdd = calledFunction.functionCalls;
        this.functionCalls.addAll(functionsToAdd);
    }

    @Override
    public Iterator<FunctionCall> iterator() {
        return functionCalls.iterator();
    }

    @Override
    public void forEach(Consumer<? super FunctionCall> consumer) {
        functionCalls.forEach(consumer);
    }

    @Override
    public Spliterator<FunctionCall> spliterator() {
        return functionCalls.spliterator();
    }

    public Stream<FunctionCall> stream(){
        return functionCalls.stream();
    }
}
