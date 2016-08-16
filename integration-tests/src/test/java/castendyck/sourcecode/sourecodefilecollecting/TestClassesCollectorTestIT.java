package castendyck.sourcecode.sourecodefilecollecting;

import org.junit.Test;

import static castendyck.sourcecode.sourecodefilecollecting.TestClassesCollectorTestExamples.*;
import static castendyck.sourcecode.sourecodefilecollecting.TestClassesCollectorTestExamples.directoryWithTestAndNoneJavaFiles;

public class TestClassesCollectorTestIT {

    @Test
    public void givenDirectoryTreeWithNoFiles_noFilesShouldBeCollected() throws Exception {
        Given.given(anEmptyDirectory())
                .whenTestFilesAreCollected()
                .thenNoFilesShouldBeCollected();
    }

    @Test
    public void givenDirectoryTreeWithOneFileInTestDirectory_oneFileShouldBeCollected() throws Exception {
        Given.given(directoryTreeWithOneFileInTestDirectory())
                .whenTestFilesAreCollected()
                .thenOneFileShouldBeCollected();
    }

    @Test
    public void givenDirectoryTreeWithNoFilesInTestDirectoryButSeveralFilesInSourceDirectory_noFilesShouldBeCollected() throws Exception {
        Given.given(directoryTreeWithNoFilesInTestDirectoryButSeveralFilesInSourceDirectory())
                .whenTestFilesAreCollected()
                .thenNoFilesShouldBeCollected();
    }

    @Test
    public void givenDirectoryTreeSeveralTestFilesInSeveralSubDirectories_allTestFilesShouldBeCollected() throws Exception {
        Given.given(directoryTreeSeveralTestFilesInSeveralSubDirectories())
                .whenTestFilesAreCollected()
                .thenAllTestFilesShouldBeCollected();
    }
    @Test
    public void givenDirectoryWithTestAndNoneJavaFiles_allTestFilesShouldBeCollected() throws Exception {
        Given.given(directoryWithTestAndNoneJavaFiles())
                .whenTestFilesAreCollected()
                .thenAllTestFilesShouldBeCollected();
    }
}
