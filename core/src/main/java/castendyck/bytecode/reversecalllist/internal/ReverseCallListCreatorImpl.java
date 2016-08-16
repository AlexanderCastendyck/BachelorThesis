package castendyck.bytecode.reversecalllist.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.reversecalllist.ReverseCallList;
import castendyck.bytecode.reversecalllist.ReverseCallListCreator;
import castendyck.calllist.CallList;
import castendyck.bytecode.calllist.CallListCreator;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistry;
import castendyck.dependencygraphing.dependencyregistry.NotRegisteredArtifactException;
import castendyck.functionidentifier.FunctionIdentifier;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class ReverseCallListCreatorImpl implements ReverseCallListCreator {
    private static final Logger logger = Logger.getLogger(ReverseCallListCreatorImpl.class);
    private final CallListCreator callListCreator;
    private final DependencyRegistry dependencyRegistry;

    public ReverseCallListCreatorImpl(CallListCreator callListCreator, DependencyRegistry dependencyRegistry) {
        this.callListCreator = callListCreator;
        this.dependencyRegistry = dependencyRegistry;
    }

    @Override
    public ReverseCallList createForArtifact(ArtifactIdentifier artifactIdentifier) {
        final ReverseCallList reverseCallList = new ReverseCallList();
        collectCallingFunctionsOfCurrentArtifactIntoCallingMap(artifactIdentifier, reverseCallList);
        final List<ArtifactIdentifier> dependingArtifacts = getArtifactsThatDependOn(artifactIdentifier);
        for (ArtifactIdentifier dependingArtifact : dependingArtifacts) {
            collectCallingFunctionsForDependentArtifactIntoCallingMap(dependingArtifact, reverseCallList, artifactIdentifier);
        }
        return reverseCallList;
    }

    private void collectCallingFunctionsOfCurrentArtifactIntoCallingMap(ArtifactIdentifier artifactIdentifier, ReverseCallList reverseCallLis) {
        final Predicate<FunctionIdentifier> filter = aAllCollectingFilter();
        storeCallingFunctionsMatchingFilterInCallingMap(artifactIdentifier, filter, reverseCallLis);
    }

    private Predicate<FunctionIdentifier> aAllCollectingFilter() {
        return functionIdentifier -> true;
    }

    private void collectCallingFunctionsForDependentArtifactIntoCallingMap(ArtifactIdentifier artifactIdentifier, ReverseCallList reverseCallLis, ArtifactIdentifier currentArtifactIdentifier)  {
        final Predicate<FunctionIdentifier> filter = aFilterThatCollectsOnlyFunctionsTargetingFunctionsInCurrentArtifact(currentArtifactIdentifier);
        storeCallingFunctionsMatchingFilterInCallingMap(artifactIdentifier, filter, reverseCallLis);
    }

    private Predicate<FunctionIdentifier> aFilterThatCollectsOnlyFunctionsTargetingFunctionsInCurrentArtifact(ArtifactIdentifier artifactIdentifier) {
        return functionIdentifier -> {
            final ArtifactIdentifier targetArtifact = functionIdentifier.getArtifactIdentifier();
            return targetArtifact.equals(artifactIdentifier);
        };
    }
    private void storeCallingFunctionsMatchingFilterInCallingMap(ArtifactIdentifier artifactIdentifier, Predicate<FunctionIdentifier> filter, ReverseCallList reverseCallList)  {
        final CallList callList = callListCreator.createForArtifact(artifactIdentifier);
        final Set<FunctionIdentifier> callers = callList.getAllCallers();
        for (FunctionIdentifier caller : callers) {
            final List<FunctionIdentifier> calledFunctions = callList.getCalledFunctionsFor(caller);
            calledFunctions.stream()
                    .filter(filter)
                    .forEach(cf -> reverseCallList.addFunctionBeingCalledBy(cf, caller));
        }
    }

    private List<ArtifactIdentifier> getArtifactsThatDependOn(ArtifactIdentifier artifactIdentifier) {
        try {
            return dependencyRegistry.getArtifactsThatDependOn(artifactIdentifier);
        } catch (NotRegisteredArtifactException e) {
            logger.debug("NotRegisteredArtifactException thrown in ReverseCallListCreatorImpl");
            logger.error("Failed to load depending Artifacts for "+artifactIdentifier.asSimpleString()+".");
            logger.error("This will probably result in false test results for artifacts using this artifact.");
            return new ArrayList<>();
        }
    }
}
