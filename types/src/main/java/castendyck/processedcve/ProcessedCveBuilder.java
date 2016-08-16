package castendyck.processedcve;

import castendyck.callgraph.CallPath;
import castendyck.cve.CVE;
import castendyck.vulnerablepoint.VulnerablePoint;

import java.util.ArrayList;
import java.util.List;

public class ProcessedCveBuilder {
    private final List<CallPath> callPaths = new ArrayList<>();
    private final List<VulnerablePoint> vulnerablePoints = new ArrayList<>();
    private CVE cve;

    public static ProcessedCveBuilder aProcessedCve(){
        return new ProcessedCveBuilder();
    }

    public ProcessedCveBuilder forCve(CVE cve){
        this.cve = cve;
        return this;
    }
    public ProcessedCveBuilder containingCallPaths(List<CallPath> callPaths){
        this.callPaths.addAll(callPaths);
        return this;
    }

    public ProcessedCveBuilder containingVulnerablePoints(List<VulnerablePoint> vulnerablePoints) {
        this.vulnerablePoints.addAll(vulnerablePoints);
        return this;
    }

    public ProcessedCve build(){
        return new ProcessedCve(cve, callPaths, vulnerablePoints);
    }
}
