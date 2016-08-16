package castendyck.directorybuilding;

import de.castendyck.file.FileUtils;
import castendyck.filebuilding.FileBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DirectoryBuilder {
    private final Path path;
    private final List<DirectoryBuilder> subDirectoriesBuilders;
    private final List<FileBuilder> fileBuilders;

    private DirectoryBuilder(Path path) {
        this.path = path;
        this.subDirectoriesBuilders = new ArrayList<>();
        this.fileBuilders = new ArrayList<>();
    }

    public static DirectoryBuilder aDirectory(Path path) {
        return new DirectoryBuilder(path);
    }

    public static DirectoryBuilder aDirectory(String name) {
        final Path path = Paths.get(name);
        return new DirectoryBuilder(path);
    }

    public static DirectoryBuilder aSubDirectory(String name) {
        return aDirectory(name);
    }

    public DirectoryBuilder with(DirectoryBuilder directoryBuilder) {
        this.subDirectoriesBuilders.add(directoryBuilder);
        return this;
    }

    public DirectoryBuilder with(FileBuilder fileBuilder) {
        this.fileBuilders.add(fileBuilder);
        return this;
    }

    public DirectoryBuilder withNoFiles() {
        fileBuilders.clear();
        return this;
    }

    private void buildRelativeTo(Path parent) throws IOException {
        final Path realPath = parent.resolve(path);
        FileUtils.createThisAndParentDirectories(realPath);
        createDirectoryTree(realPath);
    }

    private void createDirectoryTree(Path realPath) throws IOException {
        for (DirectoryBuilder subDirectoryBuilder : subDirectoriesBuilders) {
            subDirectoryBuilder.buildRelativeTo(realPath);
        }
        for (FileBuilder fileBuilder : fileBuilders) {
            fileBuilder.inPath(realPath)
                    .build();
        }

    }

    public Path build() throws IOException {
        if(Files.exists(path)) {
            FileUtils.deleteThisPathAndChildren(path);
        }
        createDirectoryTree(path);
        if(!Files.exists(path)) {
            FileUtils.createThisAndParentDirectories(path);
        }
        return path;
    }
}
