package castendyck.analyzing.reducing.internal;

import castendyck.classidentifier.ClassIdentifier;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.javasourcefile.JavaSourceFile;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionIdentifierJavaSourceFileMatcher {
    private static Pattern PATH_IN_TEST_DIRECTORY = Pattern.compile("(/)?([\\w_-]+/)*(src/test/java/)(?<package>([\\w_-]+/)*)((?<class>([\\w_-]+))\\.java)");
    private final String className;

    public FunctionIdentifierJavaSourceFileMatcher(FunctionIdentifier functionIdentifier) {
        final ClassIdentifier classIdentifier = functionIdentifier.getClassIdentifier();
        final String className = classIdentifier.getClassFile().asString();

        //ClassIdentifier always ends with .class -> to match it, remove it
        final int startOfClassSuffix = className.length() - 6;
        final String classnameWithoutClassSuffix = className.substring(0, startOfClassSuffix);
        this.className = classnameWithoutClassSuffix;


    }

    public static FunctionIdentifierJavaSourceFileMatcher aMatcherForFunctionIdentifier(FunctionIdentifier functionIdentifier) {
        return new FunctionIdentifierJavaSourceFileMatcher(functionIdentifier);
    }

    public boolean matches(JavaSourceFile javaSourceFile) {
        final Path absolutePath = javaSourceFile.getPath();
        final String path = absolutePath.toString();
        final Matcher matcher = PATH_IN_TEST_DIRECTORY.matcher(path);
        if (matcher.matches()) {
            final String packageOfFile = matcher.group("package");
            final String classNameOfFile = matcher.group("class");

            final String fullyQualifiedClassName = packageOfFile + classNameOfFile;

            final boolean matched = fullyQualifiedClassName.equals(className);
            return matched;
        }
        return false;
    }
}
