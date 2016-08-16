package castendyck.repository;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.LocalRepositoryManager;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestRepositorySession {

    public static RepositorySystemSession getSessionForLocalRepo(RepositorySystem repositorySystem, Path repositoryRoot) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        //set all null to have maximum output/data in tests

        session.setVersionFilter(null);
        //session.setDependencySelector(null);
        session.setDependencyGraphTransformer(null);

        final File basedir = repositoryRoot.toFile();
        LocalRepository localRepo = new LocalRepository(basedir);
        final LocalRepositoryManager localRepositoryManager = repositorySystem.newLocalRepositoryManager(session, localRepo);
        session.setLocalRepositoryManager(localRepositoryManager);

        return session;
    }

    public static RepositorySystemSession getSession(RepositorySystem repositorySystem) {
        final Path repositoryRoot = Paths.get("target/test-repo");
        final RepositorySystemSession systemSession = getSessionForLocalRepo(repositorySystem, repositoryRoot);
        return systemSession;
    }

}
