package castendyck.classpath.internal;


import castendyck.classpath.ClassPath;

import java.util.regex.Pattern;

import static de.castendyck.enforcing.TypePatternEnforcer.ensureInputMatchesPattern;
import static de.castendyck.enforcing.NotNullConstraintEnforcer.ensureNotNull;


public class ClassPathImpl implements ClassPath {
    //Regex: (Package/)*(className|classname$InnerClass)(directory/|.xml|.class...)
    private final static Pattern CLASS_PATH_PATTERN = Pattern.compile("([\\w_$-\\.]+/)*[\\w_$-\\.]+(\\.[\\w_-]+|/)?");

    private final String classpath;

    private ClassPathImpl(String classpath) {
        this.classpath = classpath;
    }

    public static ClassPath parse(String classpath) {
        ensureNotNull(classpath);
        ensureInputMatchesPattern(classpath, CLASS_PATH_PATTERN);

        return new ClassPathImpl(classpath);
    }


    @Override public String asString() {
        return classpath;
    }

    @Override public String getPackagePart() {
        if (!classpath.contains("/")) {
            return "";
        } else {
            final int classNameStart = classpath.lastIndexOf("/") + 1;
            return classpath.substring(0, classNameStart);
        }
    }

    @Override public String getClassName() {
        if (!classpath.contains("/")) {
            return classpath;
        } else {
            final int classNameStart = classpath.lastIndexOf("/") + 1;
            return classpath.substring(classNameStart);
        }
    }

    @Override public boolean isClassFile() {
        final boolean isClass = classpath.endsWith(".class");
        return isClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassPathImpl classPath = (ClassPathImpl) o;

        return classpath != null ? classpath.equals(classPath.classpath) : classPath.classpath == null;

    }

    @Override
    public int hashCode() {
        return classpath != null ? classpath.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ClassPath{" +
                "classpath='" + classpath + '\'' +
                '}';
    }
}
