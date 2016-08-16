package castendyck.sourcecode.sourecodefilecollecting;

import castendyck.sourcecode.sourecodefilecollecting.internal.TestClassesCollectorImpl;

import java.nio.file.Path;

public class TestClassesCollectorFactory {

    public static TestClassesCollector newInstanceForDefaultMavenTestPath(){
        return new TestClassesCollectorImpl();
    }
    public static TestClassesCollector newInstanceWithCustomTestPath(Path testPath){
        return new TestClassesCollectorImpl(testPath);
    }
}
