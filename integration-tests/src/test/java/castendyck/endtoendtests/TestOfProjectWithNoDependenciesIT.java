package castendyck.endtoendtests;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

public class TestOfProjectWithNoDependenciesIT {
    File testDir = new File("src/test/resources/TestProjectWithoutDependencies");

    @Ignore
    @Test
    public void testForNotDependencies() throws IOException, VerificationException {
        Verifier verifier  = new Verifier( testDir.getAbsolutePath() );

        verifier.executeGoal( "verify" );

        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
        final File log = new File(testDir + "/log.txt");
        assertThat(log, ConsoleOutputMatcher.contains(
                "[INFO] Analysis finished: 0 CVEs checked.",
                "[INFO] BUILD SUCCESS"
        ));
    }
}
