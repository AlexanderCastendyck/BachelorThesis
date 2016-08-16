package castendyck.callgraphstrategy.roadmap;

import castendyck.callgraphstrategy.roadmap.internal.RoadMapCreatorImpl;

public class RoadMapCreatorFactory {

    public static RoadMapCreator newInstance() {
        return new RoadMapCreatorImpl();
    }
}
