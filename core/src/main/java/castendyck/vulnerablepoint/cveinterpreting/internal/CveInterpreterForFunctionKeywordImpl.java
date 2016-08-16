package castendyck.vulnerablepoint.cveinterpreting.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.cpe.CPE;
import castendyck.cpe.CpeNameParsingException;
import castendyck.cve.CVE;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functionidentifier.FunctionIdentifierBuilder;
import castendyck.maven.pomfile.PomFile;
import de.castendyck.utils.ThisShouldNeverHappenException;
import castendyck.vulnerablepoint.VulnerablePoint;
import castendyck.vulnerablepoint.VulnerablePointBuilder;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpreter;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpretingResult;
import castendyck.vulnerablepoint.usedcpefinding.UsedCpeFinder;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CveInterpreterForFunctionKeywordImpl implements CveInterpreter {
    private final static Logger logger = Logger.getLogger(CveInterpreterForFunctionKeywordImpl.class);
    private final static Pattern SEARCH_PATTERN = Pattern.compile("(in )?the (?<functionName>[\\w,._()/\\-]+) function( in (?<className>[\\w,._()/\\-]+))? in (?<artifactName>[\\w,._()/\\-]+)", Pattern.CASE_INSENSITIVE);
    private final UsedCpeFinder usedCpeFinder;

    public CveInterpreterForFunctionKeywordImpl(UsedCpeFinder usedCpeFinder) {
        this.usedCpeFinder = usedCpeFinder;
    }

    @Override
    public CveInterpretingResult interpret(CVE cve, PomFile pomFile) {
        final String description = cve.getDescription();
        final Matcher matcher = SEARCH_PATTERN.matcher(description);
        if (matcher.find()) {
            if (!isInformationPresentFor(matcher, "functionName", "className", "artifactName")) {
                return negativeResult();
            }
            final String functionName = obtainInformation("functionName", matcher);
            final String className = obtainInformation("className", matcher);
            final String artifactName = obtainInformation("artifactName", matcher);

            final List<CPE> cpes = cve.getCpes();
            if (artifactNameMatchesAssociatedCpe(artifactName, cpes)) {
                final List<VulnerablePoint> vulnerablePoints = mapToVulnerablePoints(functionName, className, cve, pomFile);
                return positiveResultWith(vulnerablePoints);
            }
        }
        return negativeResult();
    }

    private boolean isInformationPresentFor(Matcher matcher, String... informationTypes) {
        boolean isPresent = true;
        for (String informationType : informationTypes) {
            isPresent &= isInformationPresent(matcher, informationType);
        }
        return isPresent;
    }

    private boolean isInformationPresent(Matcher matcher, String informationType) {
        return matcher.group(informationType) != null;
    }

    private String obtainInformation(String informationType, Matcher matcher) {
        final String information = matcher.group(informationType);
        if (information == null) {
            throw new ThisShouldNeverHappenException();
        } else {
            return information;
        }
    }

    private boolean artifactNameMatchesAssociatedCpe(String artifactName, List<CPE> cpes) {
        for (CPE cpe : cpes) {
            if (isCpeIdenticallyWithArtifact(artifactName, cpe)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCpeIdenticallyWithArtifact(String artifactName, CPE cpe) {
        try {
            final String vendor = cpe.getVendor();
            if (vendor.equalsIgnoreCase(artifactName)) {
                return true;
            }
            return true;
        } catch (CpeNameParsingException e) {
            final String message = "CveInterpreterForFunctionKeywordImpl was not able to determine, if cpe is matching, " +
                    "because cpe " + cpe.getValue() + " threw Exception when being asked for vendor.";
            logger.debug(message);
            return false;
        }
    }

    private List<VulnerablePoint> mapToVulnerablePoints(String functionName, String className, CVE cve, PomFile pomFile) {
        final List<CPE> cpes = cve.getCpes();
        final List<CPE> usedCpes = usedCpeFinder.findUsedCpesIn(pomFile, cpes);

        final List<VulnerablePoint> vulnerablePoints = new ArrayList<>();
        for (CPE cpe : usedCpes) {
            try {
                final ArtifactIdentifier artifactIdentifier = CpeToArtifactConverter.convertToArtifact(cpe);

                final String escapedClassName = escapeClassName(className);
                final FunctionIdentifier functionIdentifier = FunctionIdentifierBuilder.aFunctionIdentifier()
                        .forFunction(functionName)
                        .forClass(escapedClassName)
                        .withArtifactIdentifier(artifactIdentifier)
                        .build();
                final VulnerablePoint vulnerablePoint = VulnerablePointBuilder.aVulnerablePoint()
                        .forCve(cve)
                        .withFunctionIdentifier(functionIdentifier)
                        .build();

                vulnerablePoints.add(vulnerablePoint);
            } catch (CpeNameParsingException e) {
                final String message = "CveInterpreterForFunctionKeywordImpl was not able to map cpe " + cpe.getValue() + " to an ArtifactIdentifier, " +
                        "because CpeNameParsingException was thrown by CpeToArtifactConverter with message " + e.getMessage();
                logger.debug(message);
            }
        }
        return vulnerablePoints;
    }

    private String escapeClassName(String className) {
        if(className.endsWith(".java")){
            return className.replace(".java", ".class");
        }
        if(!className.endsWith(".class")){
            return className+".class";
        }
        return className;
    }

    private CveInterpretingResult positiveResultWith(List<VulnerablePoint> vulnerablePoints) {
        return new CveInterpretingResult(CveInterpretingResult.RESULT.HAS_RESULT, vulnerablePoints);
    }

    private CveInterpretingResult negativeResult() {
        final List<VulnerablePoint> vulnerablePoints = Collections.emptyList();
        return new CveInterpretingResult(CveInterpretingResult.RESULT.NO_USABLE_RESULT, vulnerablePoints);
    }
}
