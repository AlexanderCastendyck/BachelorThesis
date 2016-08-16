package castendyck.vulnerablepoint.cveinterpreting.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.cpe.CPE;
import castendyck.cpe.CpeNameParsingException;
import castendyck.cve.CVE;
import castendyck.cve.CveTestBuilder;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functionidentifier.FunctionIdentifierBuilder;
import castendyck.vulnerablepoint.VulnerablePoint;
import castendyck.vulnerablepoint.VulnerablePointBuilder;
import org.junit.Test;

import java.util.List;

public class CveInterpreterForFunctionKeywordTestIT {


    @Test
    public void interpretReturnsOneVulnerablePoint_whenAllInformationIsPresentInDescription() throws Exception {
        String description = "Cross-site scripting (XSS) vulnerability in the wp_get_attachment_link function in" +
                " post_template.class in WordPress before 4.5.3 allows remote attackers to inject arbitrary...";

        final CveTestBuilder cve = CveTestBuilder.aCve()
                .witCpe(CPE.createNew("cpe:/a:wordpress:wordpress:4.5.2"))
                .withDescription(description);

        Given.given(CveStateBuilder.aCve(cve))
                .whenCveIsInterpreted()
                .expect(aVulnerablePointIn("wp_get_attachment_link", "post_template.class", "wordpress", cve));
    }

    @Test
    public void interpretReturnsOneVulnerablePoint_whenAllInformationIsPresentInDescription_example2() throws Exception {
        String description = "The mov_read_dref function in libavformat_mov.class in Libav before 11.7 and FFmpeg before 0.11...";

        final CveTestBuilder cve = CveTestBuilder.aCve()
                .witCpe(CPE.createNew("cpe:/a:libav:libav:11.6"))
                .withDescription(description);

        Given.given(CveStateBuilder.aCve(cve))
                .whenCveIsInterpreted()
                .expect(aVulnerablePointIn("mov_read_dref", "libavformat_mov.class", "libav", cve));
    }

    @Test
    public void interpretFails_whenInformationAboutClassIsNotPresent() throws Exception {
        String description = "The parse_chunk_header function in libtorrent before 1.1.1 allows remote attackers to " +
                "cause a denial of service (crash) via a crafted (1) HTTP response or possibly a (2) UPnP broadcas...";

        final CveTestBuilder cve = CveTestBuilder.aCve()
                .witCpe(CPE.createNew("cpe:/a:arvidn:libtorrent:1.1"))
                .withDescription(description);

        Given.given(CveStateBuilder.aCve(cve))
                .whenCveIsInterpreted()
                .expectNegativeResult();
    }

    private VulnerablePoint aVulnerablePointIn(String funtionName, String clazz, String artifactName, CveTestBuilder cveTestBuilder) throws CpeNameParsingException {
        final CVE cve = cveTestBuilder.build();
        final List<CPE> cpes = cve.getCpes();
        final CPE firstCpe = cpes.get(0);
        final String groupId = firstCpe.getVendor();
        final String version = firstCpe.getVersion();
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort(groupId, artifactName, version);

        final FunctionIdentifier functionIdentifier = FunctionIdentifierBuilder.aFunctionIdentifier()
                .forFunction(funtionName)
                .forClass(clazz)
                .withArtifactIdentifier(artifactIdentifier)
                .build();
        final VulnerablePoint vulnerablePoint = VulnerablePointBuilder.aVulnerablePoint()
                .forCve(cve)
                .withFunctionIdentifier(functionIdentifier)
                .build();
        return vulnerablePoint;
    }
}
