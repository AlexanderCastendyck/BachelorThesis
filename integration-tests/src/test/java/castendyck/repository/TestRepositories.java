package castendyck.repository;

import org.eclipse.aether.repository.RemoteRepository;

import java.util.Collections;
import java.util.List;

public class TestRepositories {
    public static List<RemoteRepository> getRepositories() {
        RemoteRepository central = new RemoteRepository.Builder("central", "default", "http://repo1.maven.org/maven2/")
                .build();

        return Collections.singletonList(central);
    }
}
