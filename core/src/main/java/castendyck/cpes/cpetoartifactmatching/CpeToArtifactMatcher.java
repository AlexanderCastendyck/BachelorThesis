package castendyck.cpes.cpetoartifactmatching;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.cpe.CPE;
import castendyck.cpe.CpeNameParsingException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CpeToArtifactMatcher {
    private final static List<String> ignoredQualifier;

    static {
        ignoredQualifier = Arrays.asList(
                "com",
                "org",
                "de",
                "uk",
                "net",
                "sample",
                "example",
                "info",
                "ch"
        );
    }

    public static boolean matches(CPE cpe, ArtifactIdentifier artifactIdentifier) {
        try {

            MATCHING_CERTAINTY versionCertainty = matchVersions(cpe, artifactIdentifier);
            if (versionCertainty != MATCHING_CERTAINTY.HIGH) {
                return false;
            }
            MATCHING_CERTAINTY artifactCertainty = matchArtifactIdAgainstProduct(cpe, artifactIdentifier);
            MATCHING_CERTAINTY groupCertainty = matchGroupIdAgainstVendor(cpe, artifactIdentifier);

            final boolean matches = isSimilarEnough(groupCertainty, artifactCertainty);
            return matches;

        }catch (CpeNameParsingException e){
            return false;
        }
    }

    private static boolean isSimilarEnough(MATCHING_CERTAINTY groupCertainty, MATCHING_CERTAINTY artifactCertainty) {
        return groupCertainty != MATCHING_CERTAINTY.NONE && artifactCertainty.compareTo(MATCHING_CERTAINTY.LOW) >= 1;
    }


    private static MATCHING_CERTAINTY matchVersions(CPE cpe, ArtifactIdentifier artifactIdentifier) throws CpeNameParsingException {
        if(!cpe.hasVersionSet()){
            return MATCHING_CERTAINTY.HIGH;
        }

        final String cpeVersion = cpe.getVersion();
        final String artifactVersion = artifactIdentifier.getVersion();
        if(cpeVersion.equals(artifactVersion)){
            return MATCHING_CERTAINTY.HIGH;
        }else{
            return MATCHING_CERTAINTY.NONE;
        }
    }

    private static MATCHING_CERTAINTY matchGroupIdAgainstVendor(CPE cpe, ArtifactIdentifier artifactIdentifier) throws CpeNameParsingException {
        final String vendor = cpe.getVendor();
        final String groupId = artifactIdentifier.getGroupId();
        if (vendor.equals(groupId)) {
            return MATCHING_CERTAINTY.HIGH;
        }

        final List<String> vendorParts = getMatchablePartsOf(vendor);
        final List<String> groupIdParts = getMatchablePartsOf(groupId);
        final List<String> partsContainedInBoth = findPartsContainedInBoth(vendorParts, groupIdParts);
        if(partsContainedInBoth.isEmpty()){
            return MATCHING_CERTAINTY.NONE;
        }

        final double percentageOfMatchedElements = groupIdParts.size() / partsContainedInBoth.size();

        if (percentageOfMatchedElements >= 0.5) {
            return MATCHING_CERTAINTY.HIGH;
        } else if (percentageOfMatchedElements >= 0.25) {
            return MATCHING_CERTAINTY.MEDIUM;
        } else{
            return MATCHING_CERTAINTY.LOW;
        }
    }

    private static MATCHING_CERTAINTY matchArtifactIdAgainstProduct(CPE cpe, ArtifactIdentifier artifactIdentifier) throws CpeNameParsingException {
        final String product = cpe.getProduct();
        final String artifactId = artifactIdentifier.getArtifactId();
        if (product.equals(artifactId)) {
            return MATCHING_CERTAINTY.HIGH;
        }

        final List<String> productParts = getMatchablePartsOf(product);
        final List<String> artifactIdParts = getMatchablePartsOf(artifactId);

        final List<String> partsContainedInBoth =  findPartsContainedInBoth(productParts, artifactIdParts);
        if(partsContainedInBoth.isEmpty()){
            return MATCHING_CERTAINTY.NONE;
        }

        final double percentageOfMatchedElements = artifactIdParts.size() / partsContainedInBoth.size();
        if (percentageOfMatchedElements >= 0.5) {
            return MATCHING_CERTAINTY.HIGH;
        } else if (percentageOfMatchedElements >= 0.25) {
            return MATCHING_CERTAINTY.MEDIUM;
        } else{
            return MATCHING_CERTAINTY.LOW;
        }
    }

    private static List<String> getMatchablePartsOf(String value) {
        final String[] split = value.split("\\.");
        final List<String> matchableParts = Arrays.stream(split)
                .map(String::trim)
                .filter(part -> !part.isEmpty())
                .filter(part -> !ignoredQualifier.contains(part))
                .collect(Collectors.toList());
        return matchableParts;
    }

    private static List<String> findPartsContainedInBoth(List<String> parts1, List<String> parts2) {
        final List<String> containedParts = parts1.stream()
                .filter(parts2::contains)
                .collect(Collectors.toList());
        return containedParts;
    }

    private enum MATCHING_CERTAINTY {NONE, LOW, MEDIUM, HIGH}
}
