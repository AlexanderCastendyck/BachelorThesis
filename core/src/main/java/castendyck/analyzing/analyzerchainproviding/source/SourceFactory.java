package castendyck.analyzing.analyzerchainproviding.source;

import castendyck.analyzing.analyzerchainproviding.source.internal.HardCodedSourceImpl;

public class SourceFactory {

    public static Source newInstanceForDefaultMapping(SourceConfiguration configuration) {
        return new HardCodedSourceImpl(configuration);
    }
}
