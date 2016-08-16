package castendyck.filebuilding;

import de.castendyck.file.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class FileBuilder {
    private final String name;
    private Path path;


    private FileBuilder(String name) {
        this.name = name;
    }

    public static FileBuilder aFile(String name){
        return new FileBuilder(name);
    }

    public FileBuilder inPath(Path path){
        this.path = path;
        return this;
    }

    public File build() throws IOException {
        final Path filePath = path.resolve(name);
        final Path createdFile = FileUtils.createOrOverrideFile(filePath);
        return createdFile.toFile();
    }
}
