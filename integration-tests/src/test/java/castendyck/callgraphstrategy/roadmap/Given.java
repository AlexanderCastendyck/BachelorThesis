package castendyck.callgraphstrategy.roadmap;

import castendyck.dependencygraphing.GraphBuilder;

public class Given {
    private final GraphBuilder graphBuilder;

    public Given(GraphBuilder graphBuilder) {
        this.graphBuilder = graphBuilder;
    }

    public static Given given(GraphBuilder graphBuilder) {
        return new Given(graphBuilder);
    }


    public When whenRoadMapIsCreated() {
        final GraphBuilder.RoadMapPackage roadMapPackage = graphBuilder.build();
        return new When(roadMapPackage);
    }
}
