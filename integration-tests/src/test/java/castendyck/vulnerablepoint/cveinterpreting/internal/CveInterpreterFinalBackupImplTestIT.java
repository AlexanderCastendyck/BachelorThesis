package castendyck.vulnerablepoint.cveinterpreting.internal;

import org.junit.Test;

import static castendyck.vulnerablepoint.cveinterpreting.internal.CveInterpreterFinalBackupExamples.*;

public class CveInterpreterFinalBackupImplTestIT {

    @Test
    public void givenOneExposedApiCallInOneClass_findsAllExposedApiCalls() throws Exception {
        Given.given(exampleWithOnlyOneApiCallInOneClass())
                .whenCveIsInterpreted()
                .allExposedApiCallsShouldBeVulnerablePoints();
    }

    @Test
    public void givenThreeExposedApiCallsInOneClass_findsAllExposedApiCalls() throws Exception {
        Given.given(exampleWithThreeExposedApiCallsInOneClass())
                .whenCveIsInterpreted()
                .allExposedApiCallsShouldBeVulnerablePoints();
    }
    @Test
    public void givenTwoExposedApiCallsInTwoClasses_findsAllExposedApiCallsInBothClasses() throws Exception {
        Given.given(exampleWithTwoExposedApiCallsInTwoClasses())
                .whenCveIsInterpreted()
                .allExposedApiCallsShouldBeVulnerablePoints();
    }
    @Test
    public void givenExposedCallThatDoesNotCallAnything_callIsIgnored() throws Exception {
        Given.given(exampleWithAnExposedCallThatIsNotCallingAnything())
                .whenCveIsInterpreted()
                .thisExposedApiCallShouldBeIgnored();
    }
}
