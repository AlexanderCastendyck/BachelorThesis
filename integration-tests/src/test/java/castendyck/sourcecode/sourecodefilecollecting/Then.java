package castendyck.sourcecode.sourecodefilecollecting;

import castendyck.javasourcefile.JavaSourceFile;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class Then {
    private final List<JavaSourceFile> foundTestClasses;
    private final List<JavaSourceFile> expectedTestClasses;

    public Then(List<JavaSourceFile> foundTestClasses, List<JavaSourceFile> expectedTestClasses) {
        this.foundTestClasses = foundTestClasses;
        this.expectedTestClasses = expectedTestClasses;
    }

    public void thenNoFilesShouldBeCollected() {
        assertThat(foundTestClasses, hasSize(0));
    }

    public void thenOneFileShouldBeCollected() {
        assertThat(foundTestClasses, hasSize(1));
        assertThat(foundTestClasses, equalTo(expectedTestClasses));
    }

    public void thenAllTestFilesShouldBeCollected() {
        assertThat(foundTestClasses, equalTo(expectedTestClasses));
    }
}
