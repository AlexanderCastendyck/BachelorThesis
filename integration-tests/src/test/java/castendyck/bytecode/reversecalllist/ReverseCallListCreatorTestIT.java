package castendyck.bytecode.reversecalllist;

import org.junit.Test;

import static castendyck.bytecode.reversecalllist.ReverseCallListStateBuilder.aFunctionNotCalled;

public class ReverseCallListCreatorTestIT {


    @Test
    public void createForArtifact_withNoCallingFunction() throws Exception {
        Given.given(aFunctionNotCalled())
                .whenReverseCallListIsCreated()
                .thenExpectEmptyReverseCallList();
    }

    @Test
    public void createForArtifact_withOneCalledFunction() throws Exception {
        Given.given(ReverseCallListStateBuilder.aFunctionCalledOnce())
                .whenReverseCallListIsCreated()
                .thenExpectReverseCallListWithAFunctionCalledOnce();
    }

    @Test
    public void createForArtifact_withAFunctionCalledTwice() throws Exception {
        Given.given(ReverseCallListStateBuilder.aFunctionCalledTwice())
                .whenReverseCallListIsCreated()
                .thenExpectReverseCallListWithOneFunctionCalledTwice();
    }

    @Test
    public void createForArtifact_withAFunctionCalledFromAnotherArtifact() throws Exception {
        Given.given(ReverseCallListStateBuilder.aFunctionCalledFromAnotherArtifact())
                .whenReverseCallListIsCreated()
                .thenExpectReverseCallListWithAFunctionCalledOnce();
    }

    @Test
    public void createForArtifact_withAFunctionCalledFromAFunctionThatIsAlsoCalled() throws Exception {
        Given.given(ReverseCallListStateBuilder.aFunctionCalledFromAFunctionThatIsAlsoCalled())
                .whenReverseCallListIsCreated()
                .thenExpectReverseCallListWithTwoFunctionsCalledOnceEach();
    }
}
