package castendyck.classfile;

import castendyck.classpath.ClassPath;

public interface ClassFile {
    String asString();

    String getPackagePart();

    String getClassName();

    ClassPath toClassPath();
}
