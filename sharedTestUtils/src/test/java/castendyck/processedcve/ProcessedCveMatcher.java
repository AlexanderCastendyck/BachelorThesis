package castendyck.processedcve;

import castendyck.callgraph.CallPath;
import de.castendyck.collections.ObjectListToStringCollector;
import castendyck.cve.CVE;
import castendyck.vulnerablepoint.VulnerablePoint;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.ArrayList;
import java.util.List;

public class ProcessedCveMatcher extends TypeSafeDiagnosingMatcher<ProcessedCve> {
    private final CVE cve;
    private final List<CallPath> callPaths;
    private final List<VulnerablePoint> vulnerablePoints;

    public ProcessedCveMatcher(CVE cve) {
        this.cve = cve;
        this.callPaths = new ArrayList<>();
        this.vulnerablePoints = new ArrayList<>();
    }

    public static ProcessedCveMatcher isAProcessedCveFor(CVE cve){
        return new ProcessedCveMatcher(cve);
    }

    public ProcessedCveMatcher containingCallPaths(List<CallPath> callPaths){
        this.callPaths.addAll(callPaths);
        return this;
    }
    public ProcessedCveMatcher containingNoCallPaths(){
        this.callPaths.clear();
        return this;
    }
    public ProcessedCveMatcher containingVulnerablePoints(List<VulnerablePoint> vulnerablePoints){
        this.vulnerablePoints.addAll(vulnerablePoints);
        return this;
    }
    public ProcessedCveMatcher containingNoVulnerablePoints(){
        this.vulnerablePoints.clear();
        return this;
    }

    @Override
    protected boolean matchesSafely(ProcessedCve processedCve, Description description) {
        matchCallPaths(processedCve.getCallPaths());
        matchVulnerablePoints(processedCve.getVulnerablePoints());

        final CVE actualCve = processedCve.getCve();
        if(this.cve.equals(actualCve)){
            return true;
        }else{
            throw new AssertionError("Found "+actualCve.getName()+" but found "+cve.getName());
        }
    }

    private void matchCallPaths(List<CallPath> callPaths) {
        List<CallPath> remainingCallPaths = new ArrayList<>(this.callPaths);
        for (CallPath callPath : callPaths) {
            if(remainingCallPaths.contains(callPath)){
                remainingCallPaths.remove(callPath);
            }
        }

        if(!remainingCallPaths.isEmpty()){
            final String remainingCallPathsAsString = remainingCallPaths.stream().collect(ObjectListToStringCollector.collectToString());
            throw new AssertionError("Could not match CallPaths: "+ remainingCallPathsAsString);
        }
    }

    private void matchVulnerablePoints(List<VulnerablePoint> vulnerablePoints) {
        List<VulnerablePoint> remainingVulnerablePoints = new ArrayList<>(this.vulnerablePoints);
        for (VulnerablePoint vulnerablePoint : vulnerablePoints) {
            if(remainingVulnerablePoints.contains(vulnerablePoint)){
                remainingVulnerablePoints.remove(vulnerablePoint);
            }
        }

        if(!remainingVulnerablePoints.isEmpty()){
            final String remainingVulnerablePointsAsString = remainingVulnerablePoints.stream().collect(ObjectListToStringCollector.collectToString());
            throw new AssertionError("Could not match VulnerablePoints "+ remainingVulnerablePointsAsString);
        }
    }

    @Override
    public void describeTo(Description description) {

    }
}
