package castendyck.analyzing.processedcveconverting;

import castendyck.callgraph.CallPath;
import castendyck.cve.CVE;
import castendyck.processedcve.ProcessedCve;
import castendyck.vulnerablepoint.VulnerablePoint;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static castendyck.processedcve.ProcessedCveBuilder.aProcessedCve;

public class ControlPathsToProcessedCveConverter {
    private final static Logger logger = Logger.getLogger(ControlPathsToProcessedCveConverter.class);

    public static List<ProcessedCve> process(List<CVE> cves, List<VulnerablePoint> vulnerablePoints, List<CallPath> callPaths) {
        final List<ProcessedCve> processedCves = new ArrayList<>();
        for (CVE cve : cves) {
            final List<CallPath> callPathsForCve = findAllCallPathsForCve(cve, callPaths);
            final List<VulnerablePoint> vulnerablePointsForCve = findVulnerablePointsForCve(cve, vulnerablePoints);

            final ProcessedCve processedCve = aProcessedCve()
                    .forCve(cve)
                    .containingCallPaths(callPathsForCve)
                    .containingVulnerablePoints(vulnerablePointsForCve)
                    .build();
            processedCves.add(processedCve);
        }

        return processedCves;
    }

    private static List<VulnerablePoint> findVulnerablePointsForCve(CVE cve, List<VulnerablePoint> vulnerablePoints) {
        final List<VulnerablePoint> vulnerablePointsForCve = vulnerablePoints.stream()
                .filter(vulnerablePoint -> vulnerablePoint.getAssociatedCve().equals(cve))
                .collect(Collectors.toList());
        return vulnerablePointsForCve;
    }

    private static List<CallPath> findAllCallPathsForCve(CVE cve, List<CallPath> callPaths) {
        final List<CallPath> callPathsOfCve = callPaths.stream()
                .filter(callPath -> isCallPathForCve(cve, callPath))
                .collect(Collectors.toList());
        return callPathsOfCve;
    }

    private static boolean isCallPathForCve(CVE cve, CallPath callPath) {
        final CVE associatedCve = callPath.getAssociatedVulnerablePoint().getAssociatedCve();
        if (associatedCve == null) {
            logger.debug("CallPath without Cve found in ControlPathsToProcessedCveConverter. Should definitely not occur here");
            return false;
        } else {
            return associatedCve.equals(cve);
        }
    }
}
