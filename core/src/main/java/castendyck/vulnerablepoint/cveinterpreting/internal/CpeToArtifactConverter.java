package castendyck.vulnerablepoint.cveinterpreting.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.cpe.CPE;
import castendyck.cpe.CpeNameParsingException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class CpeToArtifactConverter {
    private final static Logger logger = Logger.getLogger(CpeToArtifactConverter.class);
    public static List<ArtifactIdentifier> convertToArtifactsWhileSkippingUnmappableOnes(List<CPE> cpes) {
        final List<ArtifactIdentifier> artifactIdentifiers = new ArrayList<>();
        for (CPE cpe : cpes) {
            try {
                final ArtifactIdentifier artifactIdentifier = convertToArtifact(cpe);
                artifactIdentifiers.add(artifactIdentifier);
            } catch (CpeNameParsingException e) {
                logger.debug("CpeNameParsingException was thrown at CpeToArtifactConverter ");
                logger.error("Skipping cpe, because "+e.getMessage());
            }
        }
        return artifactIdentifiers;
    }

    public static ArtifactIdentifier convertToArtifact(CPE cpe) throws CpeNameParsingException {
        final String product = cpe.getProduct();
        final String vendor = cpe.getVendor();
        final String version = cpe.getVersion();
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.anArtifactIdentifier()
                .withGroupId(vendor)
                .withArtifactId(product)
                .withVersion(version)
                .build();
        return artifactIdentifier;
    }
}
