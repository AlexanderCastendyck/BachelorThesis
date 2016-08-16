package castendyck.roadmap.internal;

import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.roadmap.RoadMap;
import castendyck.roadmap.RoadSection;

import java.util.Iterator;
import java.util.List;

public class RoadMapImpl implements RoadMap {
    private final List<RoadSection> roadSections;
    private final Iterator<RoadSection> iterator;

    public RoadMapImpl(List<RoadSection> roadSections) {
        NotNullConstraintEnforcer.ensureNotNull(roadSections);
        this.roadSections = roadSections;
        this.iterator = roadSections.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public RoadSection getNext() {
        return iterator.next();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoadMapImpl roadMap = (RoadMapImpl) o;

        return roadSections.equals(roadMap.roadSections);
    }

    @Override
    public int hashCode() {
        return roadSections.hashCode();
    }
}
