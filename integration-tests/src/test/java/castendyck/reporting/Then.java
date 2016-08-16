package castendyck.reporting;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class Then {
    private final String output;

    public Then(String output) {
        this.output = output;
    }

    public void expectOutputContaining(String expectedOutput) {
        assertThat(output, containsString(expectedOutput));
    }
}
