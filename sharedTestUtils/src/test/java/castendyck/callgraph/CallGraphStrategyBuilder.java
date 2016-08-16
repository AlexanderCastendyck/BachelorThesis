package castendyck.callgraph;

import castendyck.callgraphstrategy.CallGraphStrategy;
import castendyck.callgraphstrategy.internal.RoadMapGuidedCallGraphStrategyImpl;
import castendyck.roadmap.RoadMap;
import castendyck.roadmap.RoadMapBuilder;
import castendyck.roadmap.RoadSectionBuilder;

import java.util.ArrayList;
import java.util.List;

public class CallGraphStrategyBuilder {
    private final List<RoadSectionBuilder> roadSectionBuilders = new ArrayList<>();

    public static CallGraphStrategyBuilder aCallGraphStrategy(){
        return new CallGraphStrategyBuilder();
    }

    public static RoadSectionBuilder aLevel(){
        return new RoadSectionBuilder();
    }
    public CallGraphStrategyBuilder having(RoadSectionBuilder roadSectionBuilder){
        this.roadSectionBuilders.add(roadSectionBuilder);
        return this;
    }

    public CallGraphStrategy build(){
        final RoadMapBuilder roadMapBuilder = RoadMapBuilder.aRoadMap();
        roadSectionBuilders.stream()
                .forEach(roadMapBuilder::withARoadSection);

        final RoadMap roadMap = roadMapBuilder.build();
        final RoadMapGuidedCallGraphStrategyImpl strategy = new RoadMapGuidedCallGraphStrategyImpl(roadMap);
        return strategy;
    }

}
