package castendyck.classidentifier;

import castendyck.classfile.ClassFile;
import castendyck.classfile.ClassFileFactory;


public class ClassIdentifier {
    private final ClassFile classFile;

    public ClassIdentifier(ClassFile classFile) {
        this.classFile = classFile;
    }

    public ClassIdentifier(String classFile) {
        final ClassFile parsedClassFile = ClassFileFactory.createNew(classFile);
        this.classFile = parsedClassFile;
    }


    public ClassFile getClassFile() {
        return classFile;
    }

    public String getClassName() {
        return classFile.getClassName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClassIdentifier that = (ClassIdentifier) o;

        return classFile != null ? classFile.equals(that.classFile) : that.classFile == null;
    }

    @Override
    public int hashCode() {
        return classFile != null ? classFile.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ClassIdentifier{" +
                "classFile='" + classFile + '\'' +
                '}';
    }
}
