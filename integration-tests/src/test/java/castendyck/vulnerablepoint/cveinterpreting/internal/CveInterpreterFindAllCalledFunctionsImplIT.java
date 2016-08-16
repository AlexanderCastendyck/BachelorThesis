package castendyck.vulnerablepoint.cveinterpreting.internal;

import org.junit.Test;

import static castendyck.vulnerablepoint.cveinterpreting.internal.CveInterpreterFindAllCalledFunctionsExamples.exampleForNoCallingFunction;
import static castendyck.vulnerablepoint.cveinterpreting.internal.CveInterpreterFindAllCalledFunctionsExamples.exampleForOneCallingFunctionInDependingArtifact;
import static castendyck.vulnerablepoint.cveinterpreting.internal.CveInterpreterFindAllCalledFunctionsExamples.exampleForTwoCallingFunctionInTwoDependingArtifact;

public class CveInterpreterFindAllCalledFunctionsImplIT {

    @Test
    public void interpret_returnsNoResultWhenNoCallingFunctionsExists() throws Exception {
        Given.given(exampleForNoCallingFunction())
                .whenCveIsInterpreted()
                .expectNegativeResult();

    }

    @Test
    public void interpret_findsOneCallingFunctionInDependingArtifact() throws Exception {
        Given.given(exampleForOneCallingFunctionInDependingArtifact())
                .whenCveIsInterpreted()
                .allCalledFunctionsShouldBeFound();

    }
    @Test
    public void interpret_findsTwoCallingFunctionInTwoDependingArtifact() throws Exception {
        Given.given(exampleForTwoCallingFunctionInTwoDependingArtifact())
                .whenCveIsInterpreted()
                .allCalledFunctionsShouldBeFound();

    }
}
