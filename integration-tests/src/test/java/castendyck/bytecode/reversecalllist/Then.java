package castendyck.bytecode.reversecalllist;

import castendyck.reversecalllist.ReverseCallList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class Then {
    private final ReverseCallList reverseCallList;
    private final ReverseCallList expectedReverseCallList;

    public Then(ReverseCallList reverseCallList, ReverseCallList expectedReverseCallList) {
        this.reverseCallList = reverseCallList;
        this.expectedReverseCallList = expectedReverseCallList;
    }

    public void thenExpectEmptyReverseCallList() {
        checkIfResultMatchesExpected();
    }

    public void thenExpectReverseCallListWithAFunctionCalledOnce() {
        checkIfResultMatchesExpected();
    }

    public void thenExpectReverseCallListWithOneFunctionCalledTwice() {
        checkIfResultMatchesExpected();
    }

    public void thenExpectReverseCallListWithTwoFunctionsCalledOnceEach() {
        checkIfResultMatchesExpected();
    }

    private void checkIfResultMatchesExpected(){
        assertThat(reverseCallList, equalTo(expectedReverseCallList));
    }
}
