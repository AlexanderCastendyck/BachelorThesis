package castendyck.sourcecode.sourecodefilecollecting;

import castendyck.javasourcefile.JavaSourceFile;

import java.nio.file.Path;
import java.util.List;

public interface TestClassesCollector {

    List<JavaSourceFile> collectFilesFromMavenTestRepository(Path startingPath);
}
