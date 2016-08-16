package castendyck.endtoendtests;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIfUnusedVulnerabilitiesAreFilteredOutIT {

    File testDir = new File("src/test/resources/ProjectWithOneUnusedDependency");

    @Ignore
    @Test
    public void testUnusedVulnerableDependenciesAreFilteredOut() throws IOException, VerificationException {
        Verifier verifier  = new Verifier( testDir.getAbsolutePath() );

        verifier.executeGoal( "verify" );

        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
        final File log = new File(testDir + "/log.txt");
        assertThat(log, ConsoleOutputMatcher.contains(
                "[INFO] Analysis finished: 3 CVEs checked.",
                "CVE-2016-0956: risk=none, certainty=certain",
                "CVE-2013-4390: risk=none, certainty=certain",
                "CVE-2013-2254: risk=none, certainty=certain",
                "[INFO] BUILD SUCCESS"
                ));
    }

}
