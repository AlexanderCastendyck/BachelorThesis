package castendyck.javasourcefile;

import de.castendyck.enforcing.NotNullConstraintEnforcer;

import java.nio.file.Path;

public class JavaSourceFile {
    private final Path path;

    JavaSourceFile(Path path) {
        NotNullConstraintEnforcer.ensureNotNull(path);
        final Path absolutePath = path.toAbsolutePath();
        this.path = absolutePath;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JavaSourceFile that = (JavaSourceFile) o;

        return path.equals(that.path);

    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}
