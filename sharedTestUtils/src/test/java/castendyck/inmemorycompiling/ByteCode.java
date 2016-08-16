package castendyck.inmemorycompiling;

import javax.tools.SimpleJavaFileObject;
import java.io.*;
import java.net.URI;

public class ByteCode extends SimpleJavaFileObject{
    private final ByteArrayOutputStream outputStream;
    private final String name;
    private final ClassLoader associatedClassLoader;

    public ByteCode(String name, Kind kind, ClassLoader associatedClassLoader) {
        super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
        this.outputStream = new ByteArrayOutputStream();
        this.name = name;
        this.associatedClassLoader = associatedClassLoader;
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return outputStream;
    }

    public InputStream getInputStream(){
        final byte[] bytes = outputStream.toByteArray();
        return new ByteArrayInputStream(bytes);
    }
    public byte[] getAsBytes(){
        final byte[] bytes = outputStream.toByteArray();
        return bytes;
    }

    public String getRelatedClassName() {
        return name;
    }

    public ClassLoader getAssociatedClassLoader() {
        return associatedClassLoader;
    }
}
