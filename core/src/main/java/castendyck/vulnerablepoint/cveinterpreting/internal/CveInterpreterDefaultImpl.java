package castendyck.vulnerablepoint.cveinterpreting.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.bytecode.dependentartifactcalling.CallsOfDependentArtifactsFinder;
import castendyck.callgraph.CallPath;
import castendyck.callgraph.callgraphcreating.CallGraphCreator;
import castendyck.callgraph.callgraphcreating.CallGraphCreatorFactory;
import castendyck.callgraph.functiondata.FunctionDataProvider;
import castendyck.callgraphstrategy.CallGraphStrategy;
import castendyck.callgraphstrategy.StrategyUnexpectedStateException;
import castendyck.cpe.CPE;
import castendyck.cve.CVE;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.maven.pomfile.PomFile;
import castendyck.vulnerablepoint.VulnerablePoint;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpreter;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpretingResult;
import castendyck.vulnerablepoint.usedcpefinding.UsedCpeFinder;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static castendyck.callgraph.mapping.CallPathsToVulnerablePointsMapper.mapToVulnerablePoints;
import static castendyck.vulnerablepoint.cveinterpreting.internal.CpeToArtifactConverter.convertToArtifactsWhileSkippingUnmappableOnes;

public class CveInterpreterDefaultImpl implements CveInterpreter {
    private final static Logger logger = Logger.getLogger(CveInterpreterFinalBackupImpl.class);

    private final UsedCpeFinder usedCpeFinder;
    private final CallsOfDependentArtifactsFinder callsOfDependentArtifactsFinder;
    private final CallGraphCreator callGraphCreator;

    public CveInterpreterDefaultImpl(UsedCpeFinder usedCpeFinder, CallsOfDependentArtifactsFinder callsOfDependentArtifactsFinder, FunctionDataProvider functinDataProvider) {
        this.usedCpeFinder = usedCpeFinder;
        this.callsOfDependentArtifactsFinder = callsOfDependentArtifactsFinder;
        this.callGraphCreator = CallGraphCreatorFactory.newInstanceForCallGraphAnalysis(functinDataProvider);
    }

    @Override
    public CveInterpretingResult interpret(CVE cve, PomFile pomFile) {
        final List<CPE> associatedCpes = cve.getCpes();
        final List<CPE> usedCpes = usedCpeFinder.findUsedCpesIn(pomFile, associatedCpes);
        final List<ArtifactIdentifier> potentialVulnerableArtifacts = convertToArtifactsWhileSkippingUnmappableOnes(usedCpes);

        final Set<VulnerablePoint> allVulnerablePoints = new HashSet<>();
        for (ArtifactIdentifier artifact : potentialVulnerableArtifacts) {
            final List<FunctionIdentifier> dependentCalls = callsOfDependentArtifactsFinder.findCallsOfDependentArtifactsCalling(artifact);
            final CallGraphStrategy callGraphStrategy = new AllCallFlowsWithinArtifactCallGraphStrategy(dependentCalls, artifact);
            final List<CallPath> callPaths = getCallPaths(callGraphStrategy, artifact);
            final List<CallPath> filteredCallPaths = filterOutControlFlowsNotEndingInThisArtifact(callPaths, artifact);

            final List<VulnerablePoint> vulnerablePoints = mapToVulnerablePoints(filteredCallPaths, cve);
            allVulnerablePoints.addAll(vulnerablePoints);
        }
        return new CveInterpretingResult(CveInterpretingResult.RESULT.HAS_RESULT, allVulnerablePoints);
    }

    private List<CallPath> getCallPaths(CallGraphStrategy callGraphStrategy, ArtifactIdentifier artifact) {
        try {
            return callGraphCreator.analyzeControlFlow(callGraphStrategy);
        } catch (StrategyUnexpectedStateException e) {
            logger.debug("StrategyUnexpectedStateException thrown in CveInterpreterDefaultImpl");
            logger.error("Was not able to locate possible vulnerable points in " + artifact.asSimpleString());
            logger.error("Analysis for this artifact could be false");
            return new ArrayList<>();
        }
    }

    private List<CallPath> filterOutControlFlowsNotEndingInThisArtifact(List<CallPath> callPaths, ArtifactIdentifier artifact) {
        final List<CallPath> controlFlowsEndingInArtifact = callPaths.stream()
                .filter(cf -> cf.getCurrentFunctionIdentifier().getArtifactIdentifier().equals(artifact))
                .collect(Collectors.toList());
        return controlFlowsEndingInArtifact;
    }


}
