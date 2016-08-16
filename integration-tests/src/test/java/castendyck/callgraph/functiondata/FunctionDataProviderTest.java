package castendyck.callgraph.functiondata;

import org.junit.Ignore;
import org.junit.Test;

import static castendyck.callgraph.functiondata.Given.given;
import static castendyck.callgraph.functiondata.SourceCodeStateBuilder.aClass;

public class FunctionDataProviderTest {

    @Test
    public void provideControlFlowInformation_forNoCall() throws Exception {
        given(aClass().named("someClass")
                .withAFunction("doSomething")
                .notBeingCalledByAnyOne())
                .whenControlFlowDataIsCollected()
                .then().theReceivedControlFlowInformationShouldNotHaveAnyCalls();
    }

    @Test
    public void provideControlFlowInformation_forOneCallWithinSameClass() throws Exception {
        given(aClass().named("someClass")
                .withAFunction("doSomething")
                .beingCalledByFunctionWithinSameClass())
                .whenControlFlowDataIsCollected()
                .then().theReceivedControlFlowInformationShouldHaveThisOneCall();
    }

    @Test
    public void provideControlFlowInformation_forOneCallFromWithinPrivateMethodWithinSameClass() throws Exception {
        given(aClass().named("someClass")
                .withAFunction("doSomething")
                .beingCalledByPrivateFunctionWithinSameClass())
                .whenControlFlowDataIsCollected()
                .then().theReceivedControlFlowInformationShouldHaveThisOneCall();
    }

    @Test
    public void provideControlFlowInformation_forTwoCallWithinSameClass() throws Exception {
        given(aClass().named("someClass")
                .withAFunction("doSomething")
                .beingCalledByTwoFunctionsWithinSameClass())
                .whenControlFlowDataIsCollected()
                .then().theReceivedControlFlowInformationShouldHaveThisTwoCalls();
    }


    @Test
    public void provideControlFlowInformation_forTwoCallFromTwoOtherClasses() throws Exception {
        given(aClass().named("someClass")
                .withAFunction("doSomething")
                .beingCalledByTwoFunctionsFromTwoDifferentClasses())
                .whenControlFlowDataIsCollected()
                .then().theReceivedControlFlowInformationShouldHaveThisTwoCalls();
    }

    @Test
    public void provideControlFlowInformation_forTwoCallFromTwoOtherClassesDifferentArtifacts() throws Exception {
        given(aClass().named("someClass")
                .withAFunction("doSomething")
                .beingCalledByTwoFunctionsFromTwoDifferentClassesArtifacts())
                .whenControlFlowDataIsCollected()
                .then().theReceivedControlFlowInformationShouldHaveThisTwoCalls();
    }

}
