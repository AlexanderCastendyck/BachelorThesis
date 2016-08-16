package castendyck.analyzing.processedcveconverting;

import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.callgraph.CallPath;
import castendyck.callgraph.CallPathBuilder;
import castendyck.cve.CVE;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functionidentifier.FunctionIdentifierBuilder;
import castendyck.processedcve.ProcessedCve;
import castendyck.vulnerablepoint.VulnerablePoint;
import castendyck.vulnerablepoint.VulnerablePointBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static castendyck.examplecves.ExampleCve.cveForCVE_2013_22541;
import static castendyck.examplecves.ExampleCve.cveForCVE_2014_0086;
import static castendyck.processedcve.ProcessedCveMatcher.isAProcessedCveFor;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class ControlPathsToProcessedCveConverterTest {

    @Test
    public void process_returnsNoProcessedCveForNoInitialCve() throws Exception {
        final List<ProcessedCve> processedCves = act();

        assertThat(processedCves, hasSize(0));
    }

    @Test
    public void process_findsNoCallPathsForCveWhenNoExist() throws Exception {
        final List<CVE> cves = Collections.singletonList(
                cveForCVE_2013_22541()
        );
        final List<CallPath> callPaths = Collections.emptyList();

        final List<ProcessedCve> processedCves = act_(cves, callPaths);

        assertThat(processedCves, hasSize(1));
        final ProcessedCve processedCve = processedCves.get(0);
        assertThat(processedCve, isAProcessedCveFor(cveForCVE_2013_22541())
                .containingNoCallPaths());
    }

    @Test
    public void process_findsAllCallPathsForCve() throws Exception {
        final List<CVE> cves = Collections.singletonList(
                cveForCVE_2013_22541()
        );
        final List<CallPath> callPaths = Arrays.asList(
                aCallPathFor(cveForCVE_2013_22541()),
                aCallPathFor(cveForCVE_2013_22541())
        );
        final List<ProcessedCve> processedCves = act_(cves, callPaths);

        assertThat(processedCves, hasSize(1));
        final ProcessedCve processedCve = processedCves.get(0);
        assertThat(processedCve, isAProcessedCveFor(cveForCVE_2013_22541())
                .containingCallPaths(callPaths));
    }

    @Test
    public void process_findsNoVulnerablePointForCveWhenNoExist() throws Exception {
        final List<CVE> cves = Collections.singletonList(
                cveForCVE_2013_22541()
        );

        final List<VulnerablePoint> vulnerablePoints = Collections.emptyList();
        final List<ProcessedCve> processedCves = act(cves, vulnerablePoints);

        assertThat(processedCves, hasSize(1));
        final ProcessedCve processedCve = processedCves.get(0);
        assertThat(processedCve, isAProcessedCveFor(cveForCVE_2013_22541())
                .containingNoVulnerablePoints());
    }

    @Test
    public void process_findsAllVulnerablePointForCve() throws Exception {
        final List<CVE> cves = Collections.singletonList(
                cveForCVE_2014_0086()
        );

        final List<VulnerablePoint> vulnerablePoints = Arrays.asList(
                aVulnerablePointFor(cveForCVE_2014_0086()),
                aVulnerablePointFor(cveForCVE_2014_0086())
        );
        final List<ProcessedCve> processedCves = act(cves, vulnerablePoints);

        assertThat(processedCves, hasSize(1));
        final ProcessedCve processedCve = processedCves.get(0);
        assertThat(processedCve, isAProcessedCveFor(cveForCVE_2014_0086())
                .containingVulnerablePoints(vulnerablePoints));
    }

    @Test
    public void process_findsAllCallPathsAndVulnerablePointsForCve() throws Exception {
        final List<CVE> cves = Collections.singletonList(
                cveForCVE_2013_22541()
        );

        final List<VulnerablePoint> vulnerablePoints = Arrays.asList(
                aVulnerablePointFor(cveForCVE_2013_22541()),
                aVulnerablePointFor(cveForCVE_2013_22541())
        );
        final List<CallPath> callPaths = Arrays.asList(
                aCallPathFor(cveForCVE_2013_22541()),
                aCallPathFor(cveForCVE_2013_22541())
        );
        final List<ProcessedCve> processedCves = act(cves, vulnerablePoints, callPaths);

        assertThat(processedCves, hasSize(1));
        final ProcessedCve processedCve = processedCves.get(0);
        assertThat(processedCve, isAProcessedCveFor(cveForCVE_2013_22541())
                .containingCallPaths(callPaths)
                .containingVulnerablePoints(vulnerablePoints));
    }

    private VulnerablePoint aVulnerablePointFor(CVE cve) {
        final FunctionIdentifier functionIdentifier = FunctionIdentifierBuilder.aFunctionIdentifier()
                .withArtifactIdentifier(ArtifactIdentifierBuilder.buildShort("someGroup", "someArtifact", "1.0"))
                .forClass("class.class")
                .forFunction("function")
                .build();
        return VulnerablePointBuilder.aVulnerablePoint()
                .forCve(cve)
                .withFunctionIdentifier(functionIdentifier)
                .build();
    }

    private CallPath aCallPathFor(CVE cve) {
        final VulnerablePoint vulnerablePoint = aVulnerablePointFor(cve);
        final CallPath callPath = CallPathBuilder.aCallPath()
                .forVulnerablePoint(vulnerablePoint)
                .build();
        return callPath;
    }

    private List<ProcessedCve> act() {
        return ControlPathsToProcessedCveConverter.process(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    private List<ProcessedCve> act(List<CVE> cves, List<VulnerablePoint> vulnerablePoints) {
        return ControlPathsToProcessedCveConverter.process(cves, vulnerablePoints, Collections.emptyList());
    }

    private List<ProcessedCve> act_(List<CVE> cves, List<CallPath> callPaths) {
        return ControlPathsToProcessedCveConverter.process(cves, Collections.emptyList(), callPaths);
    }

    private List<ProcessedCve> act(List<CVE> cves, List<VulnerablePoint> vulnerablePoints, List<CallPath> callPaths) {
        return ControlPathsToProcessedCveConverter.process(cves, vulnerablePoints, callPaths);
    }

}