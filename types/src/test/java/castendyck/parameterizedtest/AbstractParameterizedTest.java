package castendyck.parameterizedtest;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public abstract class AbstractParameterizedTest<T, R> {
    public static String NO_EXPECTATION_NECESSARY = "Expected Exception to be thrown";


    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    private T input;
    private R expectedResult;
    private Class<? extends Throwable> expectedExceptionClass;

    public AbstractParameterizedTest(T input, R expectedResult, Class<? extends Throwable>  expectedExceptionClass) {
        this.input = input;
        this.expectedResult = expectedResult;
        this.expectedExceptionClass = expectedExceptionClass;
    }

    public abstract R act(T input) throws Exception;

    @Test
    public void test() throws Exception {
        if (expectedExceptionClass != null) {
            expectedException.expect(expectedExceptionClass);
            act(input);
        } else {
            R result = act(input);
            Assert.assertThat(result, equalTo(expectedResult));
        }
    }
}
