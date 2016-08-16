package castendyck.sourcecode.jarpacking.internal;

import de.castendyck.file.FileUtils;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarFileBuilder {
    private final Path outPutFile;
    private final List<FileInformation> includedFiles;

    public JarFileBuilder(Path outPutFile) {
        this.outPutFile = outPutFile;
        this.includedFiles = new ArrayList<>();
    }

    public static JarFileBuilder aJarFile(String fileName, Path targetDirectory) {
        final Path outPutFile = targetDirectory.resolve(fileName);
        return new JarFileBuilder(outPutFile);
    }

    public JarFileBuilder containingAClassFile(File file, String fullyQualifiedClassName) {
        final FileInformation fileInformation = new FileInformation(file, fullyQualifiedClassName);
        includedFiles.add(fileInformation);
        return this;
    }

    public Path build() throws IOException {
        final Manifest manifest = new Manifest();
        final Attributes mainAttributes = manifest.getMainAttributes();
        mainAttributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");

        final Path existingPath = FileUtils.createOrOverrideFile(outPutFile);
        final FileOutputStream outputStream = new FileOutputStream(existingPath.toFile());
        final JarOutputStream jarOutputStream = new JarOutputStream(outputStream, manifest);

        for (FileInformation fileInformation : includedFiles) {
            final String relatedClassName = fileInformation.fullyQualifiedClassName;
            final JarEntry jarEntry = new JarEntry(relatedClassName);
            jarOutputStream.putNextEntry(jarEntry);
            writeToJarEntry(jarOutputStream, fileInformation);
        }
        jarOutputStream.close();
        return existingPath;
    }

    private void writeToJarEntry(JarOutputStream jarOutputStream, FileInformation fileInformationyteCode) throws IOException {
        final InputStream inputStream = new FileInputStream(fileInformationyteCode.file);
        final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        byte[] buffer = new byte[1024];
        while (true) {
            int count = bufferedInputStream.read(buffer);
            if (count == -1)
                break;
            jarOutputStream.write(buffer, 0, count);
        }
        jarOutputStream.closeEntry();
        inputStream.close();
    }

    private class FileInformation {
        private final File file;
        private final String fullyQualifiedClassName;

        public FileInformation(File file, String fullyQualifiedClassName) {
            this.file = file;
            this.fullyQualifiedClassName = fullyQualifiedClassName;
        }
    }

}
