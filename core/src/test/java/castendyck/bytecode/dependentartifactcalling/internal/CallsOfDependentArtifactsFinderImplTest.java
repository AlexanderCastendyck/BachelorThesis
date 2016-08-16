package castendyck.bytecode.dependentartifactcalling.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.bytecode.reversecalllist.ReverseCallListCreator;
import castendyck.functionCalls.FunctionCallTestBuilder;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functionidentifier.FunctionIdentifierAbbreviatedBuilder;
import castendyck.functionidentifier.FunctionIdentifierBuilder;
import castendyck.reversecalllist.ReverseCallListBuilder;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static castendyck.functionidentifier.FunctionIdentifierAbbreviatedBuilder.aFunctionIdentifierFor;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class CallsOfDependentArtifactsFinderImplTest {

    @Test
    public void findCallsOfDependentArtifactsCalling_forNoDependentCall() throws Exception {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final ReverseCallListCreator reverseCallListCreator = DependentCallRegisterer.letFunction(artifactIdentifier, "Main.class", "function")
                .notBeCalled()
                .whenUsingThisReverseCallListCreator();
        final CallsOfDependentArtifactsFinderImpl callsOfDependentArtifactsFinder = new CallsOfDependentArtifactsFinderImpl(reverseCallListCreator);

        final List<FunctionIdentifier> dependentCalls = callsOfDependentArtifactsFinder.findCallsOfDependentArtifactsCalling(artifactIdentifier);

        assertThat(dependentCalls, hasSize(0));
    }

    @Test
    public void findCallsOfDependentArtifactsCalling_forOneDependentCall() throws Exception {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final ArtifactIdentifier dependentArtifact = ArtifactIdentifierBuilder.buildShort("local", "dependentArtifact", "1.0");
        final FunctionIdentifier callingFunctionIdentifier = aFunctionIdentifierFor(dependentArtifact, "callingClass.class", "callingFunction");

        final ReverseCallListCreator reverseCallListCreator = DependentCallRegisterer.letFunction(artifactIdentifier, "Main.class", "function")
                .beCalledByDependentArtifact(callingFunctionIdentifier)
                .whenUsingThisReverseCallListCreator();

        final CallsOfDependentArtifactsFinderImpl callsOfDependentArtifactsFinder = new CallsOfDependentArtifactsFinderImpl(reverseCallListCreator);

        final List<FunctionIdentifier> dependentCalls = callsOfDependentArtifactsFinder.findCallsOfDependentArtifactsCalling(artifactIdentifier);

        assertThat(dependentCalls, contains(callingFunctionIdentifier));
    }

    @Test
    public void findCallsOfDependentArtifactsCalling_forTwoDependentCallWithinSameDependentArtifact() throws Exception {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final ArtifactIdentifier dependentArtifact = ArtifactIdentifierBuilder.buildShort("local", "dependentArtifact", "1.0");
        final FunctionIdentifier callingFunctionIdentifier1 = aFunctionIdentifierFor(dependentArtifact, "callingClass.class", "callingFunction1");
        final FunctionIdentifier callingFunctionIdentifier2 = aFunctionIdentifierFor(dependentArtifact, "callingClass.class", "callingFunction2");

        final ReverseCallListCreator reverseCallListCreator = DependentCallRegisterer.letFunction(artifactIdentifier, "Main.class", "function")
                .beCalledByDependentArtifact(callingFunctionIdentifier1)
                .beCalledByDependentArtifact(callingFunctionIdentifier2)
                .whenUsingThisReverseCallListCreator();

        final CallsOfDependentArtifactsFinderImpl callsOfDependentArtifactsFinder = new CallsOfDependentArtifactsFinderImpl(reverseCallListCreator);

        final List<FunctionIdentifier> dependentCalls = callsOfDependentArtifactsFinder.findCallsOfDependentArtifactsCalling(artifactIdentifier);

        assertThat(dependentCalls, contains(callingFunctionIdentifier1, callingFunctionIdentifier2));
    }

    @Test
    public void findCallsOfDependentArtifactsCalling_forTwoDependentCallOfTwoDependentArtifact() throws Exception {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");

        final ArtifactIdentifier dependentArtifact1 = ArtifactIdentifierBuilder.buildShort("local", "dependentArtifact1", "1.0");
        final FunctionIdentifier callingFunctionIdentifier1 = aFunctionIdentifierFor(dependentArtifact1, "callingClass.class", "callingFunction1");

        final ArtifactIdentifier dependentArtifact2 = ArtifactIdentifierBuilder.buildShort("local", "dependentArtifact2", "1.0");
        final FunctionIdentifier callingFunctionIdentifier2 = aFunctionIdentifierFor(dependentArtifact2, "callingClass.class", "callingFunction2");

        final ReverseCallListCreator reverseCallListCreator = DependentCallRegisterer.letFunction(artifactIdentifier, "Main.class", "function")
                .beCalledByDependentArtifact(callingFunctionIdentifier1)
                .beCalledByDependentArtifact(callingFunctionIdentifier2)
                .whenUsingThisReverseCallListCreator();

        final CallsOfDependentArtifactsFinderImpl callsOfDependentArtifactsFinder = new CallsOfDependentArtifactsFinderImpl(reverseCallListCreator);

        final List<FunctionIdentifier> dependentCalls = callsOfDependentArtifactsFinder.findCallsOfDependentArtifactsCalling(artifactIdentifier);

        assertThat(dependentCalls, contains(callingFunctionIdentifier1, callingFunctionIdentifier2));
    }
}