package castendyck.callgraphstrategy;


import castendyck.dependencygraphing.GraphBuilder;

public class Given {
    private final GraphBuilder graphBuilder;

    public Given(GraphBuilder graphBuilder) {
        this.graphBuilder = graphBuilder;
    }

    public static When given(GraphBuilder graphBuilder) {
        return new When(graphBuilder);
    }



}
