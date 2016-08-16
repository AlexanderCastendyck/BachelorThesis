package castendyck.callgraph.functiondata;

public class Given {
    private final SourceCodeStateBuilder sourceCodeStateBuilder;

    public Given(SourceCodeStateBuilder sourceCodeStateBuilder) {
        this.sourceCodeStateBuilder = sourceCodeStateBuilder;
    }

    public static Given given(SourceCodeStateBuilder sourceCodeStateBuilder){
        return new Given(sourceCodeStateBuilder);
    }

    public When whenControlFlowDataIsCollected(){
        return new When(sourceCodeStateBuilder);
    }
}
