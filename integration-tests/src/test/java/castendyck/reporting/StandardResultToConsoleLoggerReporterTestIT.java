package castendyck.reporting;

import castendyck.analyzedcve.AnalyzedCveTestBuilder;
import castendyck.certainty.Certainty;
import castendyck.risklevel.RiskLevel;
import org.junit.Test;

import static castendyck.reporting.StandardResultToConsoleLoggerReporterTestExamples.emptyAnalysisResult;
import static castendyck.reporting.StandardResultToConsoleLoggerReporterTestExamples.resultWith;

public class StandardResultToConsoleLoggerReporterTestIT {
    @Test
    public void givenEmptyResult_expectOutputWithZeroAnalyzedCves() throws Exception {
        Given.given(emptyAnalysisResult())
                .whenPrintedToConsole()
                .expectOutputContaining("Analysis finished: 0 CVEs checked.\n");
    }

    @Test
    public void givenResultWithOneAnalyzedCve_expectOutput() throws Exception {
        Given.given(resultWith(
                AnalyzedCveTestBuilder.aAnalyzedCve()
                        .forCve("Cve-1234-2015")
                        .withCertainty(Certainty.CERTAIN)
                        .withRisk(RiskLevel.HIGH)))
                .whenPrintedToConsole()
                .expectOutputContaining("Analysis finished: 1 CVEs checked.\n" +
                        "Cve-1234-2015: risk=high, certainty=certain\n"
                );
    }
}
