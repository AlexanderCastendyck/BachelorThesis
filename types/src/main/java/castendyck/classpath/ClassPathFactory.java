package castendyck.classpath;

import castendyck.classpath.internal.ClassPathImpl;

public class ClassPathFactory {
    public static ClassPath createNew(String input){
        return ClassPathImpl.parse(input);
    }
}
