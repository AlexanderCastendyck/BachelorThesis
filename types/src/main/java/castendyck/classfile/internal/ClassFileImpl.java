package castendyck.classfile.internal;

import castendyck.classfile.ClassFile;
import castendyck.classpath.ClassPath;
import castendyck.classpath.ClassPathFactory;

import java.util.regex.Pattern;

import static de.castendyck.enforcing.NotNullConstraintEnforcer.ensureNotNull;
import static de.castendyck.enforcing.TypePatternEnforcer.ensureInputMatchesPattern;

public class ClassFileImpl implements ClassFile {
    private static final Pattern CLASS_FILE_PATTERN = Pattern.compile("(([\\w_]+/)*[\\w_]+(\\$[\\w_]+)?(\\.class)|java/[\\w_$/]+)");
    private final String path;

    private ClassFileImpl(String path) {
        this.path = path;
    }

    public static ClassFile parse(String input) {
        ensureNotNull(input);
        ensureInputMatchesPattern(input, CLASS_FILE_PATTERN);

        final ClassFile classFile = new ClassFileImpl(input);
        return classFile;
    }

    @Override public String asString() {
        return path;
    }

    @Override public String getPackagePart() {
        if (!path.contains("/")) {
            return "";
        } else {
            final int classNameStart = path.lastIndexOf("/") + 1;
            return path.substring(0, classNameStart);
        }
    }

    @Override public String getClassName() {
        if (!path.contains("/")) {
            return path;
        } else {
            final int classNameStart = path.lastIndexOf("/") + 1;
            return path.substring(classNameStart);
        }
    }

    @Override public ClassPath toClassPath() {
        final ClassPath parse = ClassPathFactory.createNew(path);
        return parse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassFileImpl classFile = (ClassFileImpl) o;

        return path.equals(classFile.path);

    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public String toString() {
        return "ClassFileImpl{" +
                "path='" + path + '\'' +
                '}';
    }
}
