package castendyck.repository;

import castendyck.repository.internal.LocalRepositoryImpl;

import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalRepositoryFactory {
    public static LocalRepository createNewInPath(Path path){
        return new LocalRepositoryImpl(path);
    }

    public static LocalRepository createNewInStandardMavenLocation(){
        final String userDir = System.getProperty("user.home");
        final Path repositoryPath = Paths.get(userDir, ".m2", "repository");
        return new LocalRepositoryImpl(repositoryPath);
    }
}
