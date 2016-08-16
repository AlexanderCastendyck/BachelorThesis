package castendyck.streamfetching;

import castendyck.classfile.ClassFile;

import java.io.InputStream;

public class StreamFetcher {
    public InputStream getRessourceAsStream(String ressource) {
        return StreamFetcher.class.getResourceAsStream(ressource);
    }

    public InputStream getClassFileAsStream(ClassFile classFile) {
        final String ressource = classFile.asString();
        return getRessourceAsStream(ressource);
    }


}
