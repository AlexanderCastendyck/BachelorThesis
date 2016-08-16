package castendyck.classfile;

import castendyck.classfile.internal.ClassFileImpl;

public class ClassFileFactory {
    public static ClassFile createNew(String input){
        return ClassFileImpl.parse(input);
    }
}
