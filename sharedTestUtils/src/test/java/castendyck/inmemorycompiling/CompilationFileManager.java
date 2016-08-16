package castendyck.inmemorycompiling;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.security.SecureClassLoader;
import java.util.ArrayList;
import java.util.List;

import static de.castendyck.collections.ExpectOneUniqueElementCollector.expectOneUniqueElement;

public class CompilationFileManager extends ForwardingJavaFileManager {
    private final List<ByteCode> storedByteCode;
    private final ClassLoader classLoader;

    public CompilationFileManager(JavaFileManager javaFileManager) {
        super(javaFileManager);
        this.storedByteCode = new ArrayList<>();
        this.classLoader = new CompilationClassLoader();
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return classLoader;
    }


    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        final ByteCode byteCode = new ByteCode(className, kind, classLoader);
        storedByteCode.add(byteCode);
        return byteCode;
    }

    public List<ByteCode> getCompiledByteCode() {
        return storedByteCode;
    }

    private class CompilationClassLoader extends SecureClassLoader {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            final ByteCode matchingByteCode = storedByteCode.stream()
                    .filter(byteCode -> byteCode.getRelatedClassName().equals(name))
                    .collect(expectOneUniqueElement());

            final byte[] byteCode = matchingByteCode.getAsBytes();
            return super.defineClass(name, byteCode, 0, byteCode.length);
        }
    }
}
