package castendyck.cpes.cpetoartifactmatching;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.cpe.CPE;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class CpeToArtifactMatcherParsingTest {
    private final CPE cpe;
    private final ArtifactIdentifier artifactIdentifier;
    private final boolean expectedMatched;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"cpe:/a:apache:commons:1.2.3", "apache:commons:1.2.3", true},
                {"cpe:/a:org.apache:commons:1.2.3", "apache:commons:1.2.3", true},
                {"cpe:/a:apache:commons:1.2.3", "org.apache:commons:1.2.3", true},
                {"cpe:/a:org.apache:commons:1.2.3", "org.apache:commons:1.2.3", true},
                {"cpe:/a:org.apache:org.commons:1.2.3", "apache:commons:1.2.3", true},
                {"cpe:/a:apache:org.apache.sling.servlets.post:2.3.0", "org.apache.sling:org.apache.sling.servlets.post:2.3.0", true},
                {"cpe:/a:apache:sling", "org.apache.sling:org.apache.sling.servlets.post:2.3.0", true},
                {"cpe:/a:one.two.three.google:one.two.three.chrome:1.0", "google:chrome:1.0", true},
                {"cpe:/a:apache:mailApi:1.0", "google:mailApi:1.0", false},
                {"cpe:/a:apache:mailApi:1.0", "apache:commons:1.0", false},
                {"cpe:/a:apache:mailApi:1.0", "apache:mailApi:1337.0", false},
                {"notACve", "apache:mailApi:1337.0", false},
        });
    }




    public CpeToArtifactMatcherParsingTest(String cpe, String artifactIdentifier, boolean expectedMatched) {
        this.cpe = CPE.createNew(cpe);
        this.artifactIdentifier = toArtifactIdentifier(artifactIdentifier);
        this.expectedMatched = expectedMatched;
    }

    private ArtifactIdentifier toArtifactIdentifier(String artifactIdentifierString) {
        final String[] split = artifactIdentifierString.split(":");
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.anArtifactIdentifier()
                .withGroupId(split[0])
                .withArtifactId(split[1])
                .withVersion(split[2])
                .build();
        return artifactIdentifier;
    }


    @Test
    public void matches() throws Exception {
        final boolean matches = CpeToArtifactMatcher.matches(cpe, artifactIdentifier);
        if(matches != expectedMatched){
            throw new AssertionError("Expected "+expectedMatched+", but got "+matches);
        }
    }
}