package castendyck.callgraph;

import castendyck.streamfetching.StreamFetcher;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.repository.LocalRepository;

public class CallGraphCollectorConfiguration {
    private final LocalRepository localRepository;
    private final StreamFetcher streamFetcher;

    public CallGraphCollectorConfiguration(LocalRepository localRepository) {
        NotNullConstraintEnforcer.ensureNotNull(localRepository);
        this.localRepository = localRepository;
        this.streamFetcher = new StreamFetcher();
    }

    public CallGraphCollectorConfiguration(LocalRepository localRepository, StreamFetcher streamFetcher) {
        NotNullConstraintEnforcer.ensureNotNull(localRepository);
        this.localRepository = localRepository;
        NotNullConstraintEnforcer.ensureNotNull(streamFetcher);
        this.streamFetcher = streamFetcher;
    }

    public LocalRepository getLocalRepository() {
        return localRepository;
    }

    public StreamFetcher getStreamFetcher() {
        return streamFetcher;
    }
}
