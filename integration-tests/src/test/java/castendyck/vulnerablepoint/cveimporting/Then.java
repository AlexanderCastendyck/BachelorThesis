package castendyck.vulnerablepoint.cveimporting;

import castendyck.cve.CVE;

import java.util.List;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class Then {
    private final List<CVE> importedCves;
    private final List<CVE> expectedCves;
    private final Exception thrownException;

    public Then(List<CVE> importedCves, List<CVE> expectedCves) {
        this.importedCves = importedCves;
        this.expectedCves = expectedCves;
        this.thrownException = null;
    }

    public Then(Exception exception) {
        this.importedCves = null;
        this.expectedCves = null;
        this.thrownException = exception;
    }

    public void thenNoneShouldBeFound() {
        ensureNoExceptionWasThrown();

        assertThat(importedCves, hasSize(0));
    }

    public void thenOneCveShouldBeFound() {
        ensureNoExceptionWasThrown();

        assertThat(importedCves, hasSize(1));
        assertThat(importedCves, equalTo(expectedCves));
    }

    public void thenTwoShouldBeFound() {
        ensureNoExceptionWasThrown();

        assertThat(importedCves, hasSize(2));
        assertThat(importedCves, equalTo(expectedCves));
    }

    public void thenAllShouldBeFound() {
        ensureNoExceptionWasThrown();

        assertThat(importedCves, equalTo(expectedCves));
    }


    private void ensureNoExceptionWasThrown() {
        if (thrownException != null) {
            thrownException.printStackTrace();
            fail("Unexpected exception was thrown: " + thrownException + "\n StackTrace:");
        }
    }
}
