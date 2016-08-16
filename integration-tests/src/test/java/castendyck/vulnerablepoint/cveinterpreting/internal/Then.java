package castendyck.vulnerablepoint.cveinterpreting.internal;

import castendyck.vulnerablepoint.VulnerablePoint;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpretingResult;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class Then {
    private final CveInterpretingResult result;
    private final CveInterpretingResult expectedResult;

    public Then(CveInterpretingResult result, CveInterpretingResult expectedResult) {
        this.result = result;
        this.expectedResult = expectedResult;
    }

    public void allExposedApiCallsShouldBeVulnerablePoints() {
        checkWhetherResultAndExpectedResultAreTheSame();
    }

    public void thisExposedApiCallShouldBeIgnored() {
        checkWhetherResultAndExpectedResultAreTheSame();
    }

    public void allCalledFunctionsShouldBeFound() {
        checkWhetherResultAndExpectedResultAreTheSame();
    }

    public void expectNegativeResult() {
        assertThat(result.gotAResult(), is(false));
    }

    public void expect(VulnerablePoint vulnerablePoint) {
        assertThat(result.gotAResult(), is(true));
        final List<VulnerablePoint> vulnerablePoints = result.getVulnerablePoints();
        assertThat(vulnerablePoints, hasSize(1));
        final VulnerablePoint onlyVulnerablePoint = vulnerablePoints.get(0);
        assertThat(onlyVulnerablePoint, equalTo(vulnerablePoint));
    }

    public void expectPositiveResultWithNoVulnerablePoint() {
        assertThat(result.gotAResult(), is(true));
        assertThat(result.getVulnerablePoints(), hasSize(0));
    }

    public void expectPositiveResultWithOneVulnerablePoint() {
        assertThat(result.gotAResult(), is(true));
        assertThat(result.getVulnerablePoints(), hasSize(1));
        checkWhetherResultAndExpectedResultAreTheSame();
    }

    public void expectPositiveResultWithTwoVulnerablePoints() {
        assertThat(result.gotAResult(), is(true));
        assertThat(result.getVulnerablePoints(), hasSize(2));
        checkWhetherResultAndExpectedResultAreTheSame();
    }

    private void checkWhetherResultAndExpectedResultAreTheSame() {
        assertThat(result.gotAResult(), equalTo(expectedResult.gotAResult()));

        final List<VulnerablePoint> vulnerablePoints = result.getVulnerablePoints();
        final List<VulnerablePoint> expectedVulnerablePoints = expectedResult.getVulnerablePoints();
        assertThat(vulnerablePoints, hasSize(expectedVulnerablePoints.size()));

        for (VulnerablePoint expectedVulnerablePoint : expectedVulnerablePoints) {
            assertThat(vulnerablePoints, hasItem(expectedVulnerablePoint));
        }
    }
}
