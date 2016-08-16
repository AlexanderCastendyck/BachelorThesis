package castendyck.javasourcefile;

import java.nio.file.Path;

public class JavaSoureFileFactory {

    public static JavaSourceFile createNew(Path path){
        return new JavaSourceFile(path);
    }
}
