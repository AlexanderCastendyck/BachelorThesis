package castendyck.classpath;

public interface ClassPath {
    String asString();

    String getPackagePart();

    String getClassName();

    boolean isClassFile();
}
