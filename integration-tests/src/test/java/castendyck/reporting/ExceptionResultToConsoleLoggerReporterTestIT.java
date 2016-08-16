package castendyck.reporting;

import org.junit.Test;

public class ExceptionResultToConsoleLoggerReporterTestIT {
    @Test
    public void givenEmptyResult_expectOutputWithZeroAnalyzedCves() throws Exception {
        Given.given(ExceptionResultToConsoleLoggerReporterExamples.anErroneousResult()
                .withErrorMessage("Problem xyz encountered"))
                .whenPrintedToConsole()
                .expectOutputContaining("Analysis failed: Exception class java.lang.Exception was thrown with message Problem xyz encountered\n");
    }
}
