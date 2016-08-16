package castendyck.callgraphstrategy;

import castendyck.dependencygraph.DependencyGraph;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.vulnerablepoint.VulnerablePoint;

import java.util.List;

public class CallGraphStrategyRequestDto {
    private final DependencyGraph dependencyGraph;
    private final List<VulnerablePoint> vulnerablePoints;

    public CallGraphStrategyRequestDto(DependencyGraph dependencyGraph, List<VulnerablePoint> vulnerablePoints) {
        NotNullConstraintEnforcer.ensureNotNull(dependencyGraph);
        this.dependencyGraph = dependencyGraph;
        NotNullConstraintEnforcer.ensureNotNull(vulnerablePoints);
        this.vulnerablePoints = vulnerablePoints;
    }

    public DependencyGraph getDependencyGraph() {
        return dependencyGraph;
    }

    public List<VulnerablePoint> getVulnerablePoints() {
        return vulnerablePoints;
    }
}
