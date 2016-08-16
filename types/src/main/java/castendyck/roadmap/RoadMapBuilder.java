package castendyck.roadmap;

import castendyck.roadmap.internal.RoadMapImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoadMapBuilder {
    private final List<RoadSectionBuilder> roadSectionBuilders = new ArrayList<>();

    public static RoadMapBuilder aRoadMap(){
        return new RoadMapBuilder();
    }

    public RoadMapBuilder withARoadSection(RoadSectionBuilder roadSectionBuilder){
        roadSectionBuilders.add(roadSectionBuilder);
        return this;
    }

    public RoadMap build(){
        final List<RoadSection> roadSections = roadSectionBuilders.stream()
                .map(RoadSectionBuilder::build)
                .collect(Collectors.toList());

        final RoadMapImpl roadMap = new RoadMapImpl(roadSections);
        return roadMap;
    }
}
