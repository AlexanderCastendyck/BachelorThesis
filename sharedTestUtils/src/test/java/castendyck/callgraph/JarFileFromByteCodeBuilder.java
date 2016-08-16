package castendyck.callgraph;

import de.castendyck.file.FileUtils;
import castendyck.inmemorycompiling.ByteCode;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.*;

public class JarFileFromByteCodeBuilder {
    private final List<ByteCode> byteCodes = new ArrayList<>();
    private Path path;

    public static JarFileFromByteCodeBuilder aJarFile(){
        return new JarFileFromByteCodeBuilder();
    }
    public JarFileFromByteCodeBuilder withByteCodesForAClass(ByteCode byteCode){
        this.byteCodes.add(byteCode);
        return this;
    }
    public JarFileFromByteCodeBuilder storedAs(Path path){
        this.path = path;
        return this;
    }
    public void build() throws IOException {
        final Manifest manifest = new Manifest();
        final Attributes mainAttributes = manifest.getMainAttributes();
        mainAttributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");

        final Path existingPath = FileUtils.createOrOverrideFile(path);
        final FileOutputStream outputStream = new FileOutputStream(existingPath.toFile());
        final JarOutputStream jarOutputStream = new JarOutputStream(outputStream, manifest);

        for (ByteCode byteCode : byteCodes) {
            final String relatedClassName = byteCode.getRelatedClassName() + ".class";
            final JarEntry jarEntry = new JarEntry(relatedClassName);
            jarOutputStream.putNextEntry(jarEntry);
            writeToJarEntry(jarOutputStream, byteCode);
        }
        jarOutputStream.close();
    }

    private void writeToJarEntry(JarOutputStream jarOutputStream, ByteCode byteCode) throws IOException {
        final InputStream inputStream = byteCode.getInputStream();
        final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        byte[] buffer = new byte[1024];
        while (true)
        {
            int count = bufferedInputStream.read(buffer);
            if (count == -1)
                break;
            jarOutputStream.write(buffer, 0, count);
        }
        jarOutputStream.closeEntry();
        inputStream.close();
    }

    public JarFileFromByteCodeBuilder withByteCodesForAClass(List<ByteCode> byteCodes) {
        byteCodes.stream()
                .forEach(this::withByteCodesForAClass);
        return this;
    }
}
