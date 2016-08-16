package castendyck.callgraph.mapping;

import castendyck.callgraph.CallPath;
import castendyck.cve.CVE;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.vulnerablepoint.VulnerablePoint;

import java.util.List;
import java.util.stream.Collectors;

import static castendyck.vulnerablepoint.VulnerablePointBuilder.aVulnerablePoint;

public class CallPathsToVulnerablePointsMapper {

    public static List<VulnerablePoint> mapToVulnerablePoints(List<CallPath> callPaths, CVE cve) {
        final List<VulnerablePoint> vulnerablePoints = callPaths.stream()
                .map(CallPath::getCurrentFunctionIdentifier)
                .map(fid -> CallPathsToVulnerablePointsMapper.mapToVulnerablePoint(fid, cve))
                .collect(Collectors.toList());
        return vulnerablePoints;
    }

    public static VulnerablePoint mapToVulnerablePoint(FunctionIdentifier functionIdentifier, CVE cve) {
        final VulnerablePoint vulnerablePoint = aVulnerablePoint()
                .withFunctionIdentifier(functionIdentifier)
                .forCve(cve)
                .build();
        return vulnerablePoint;
    }
}
