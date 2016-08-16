package de.castendyck.javaapi;

public class JavaApiClassMatcher {
    public static final String JAVA_PACKAGE_PREFIX = "java/";
    public static final String JAVAX_PACKAGE_PREFIX = "javax/";

    public static boolean isClassFromJavaApi(String className) {
        return className.startsWith(JAVA_PACKAGE_PREFIX) || className.startsWith(JAVAX_PACKAGE_PREFIX);
    }

}
