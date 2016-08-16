package castendyck.callgraph;

import de.castendyck.collections.ExpectOneUniqueElementCollector;
import castendyck.streamfetching.StreamFetcher;
import castendyck.inmemorycompiling.ByteCode;
import castendyck.localrepository.LocalRepositoryBuilder;
import castendyck.repository.LocalRepository;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class CallGraphCollectorConfigurationBuilder {
    private LocalRepositoryBuilder localRepositoryBuilder;
    private List<ByteCode> byteCodes;

    public static CallGraphCollectorConfigurationBuilder aConfiguration() {
        return new CallGraphCollectorConfigurationBuilder();
    }

    public CallGraphCollectorConfigurationBuilder with(LocalRepositoryBuilder localRepositoryBuilder) {
        this.localRepositoryBuilder = localRepositoryBuilder;
        return this;
    }

    public CallGraphCollectorConfigurationBuilder with(List<ByteCode> byteCodes) {
        this.byteCodes = byteCodes;
        return this;
    }

    public CallGraphCollectorConfiguration build() throws IOException {
        StreamFetcher streamFetcher = Mockito.mock(StreamFetcher.class);
        when(streamFetcher.getClassFileAsStream(any())).thenCallRealMethod();

        final Set<String> storedClasses = localRepositoryBuilder.getStoredClasses();
        for (String className : storedClasses) {
            final ByteCode byteCode = byteCodeForClass(className, byteCodes);
            final String fullClassName = className + ".class";
            when(streamFetcher.getRessourceAsStream(fullClassName)).thenAnswer((Answer<InputStream>) invocationOnMock -> byteCode.getInputStream());

        }

        final LocalRepository localRepository = localRepositoryBuilder.build();
        return new CallGraphCollectorConfiguration(localRepository, streamFetcher);
    }
    private static ByteCode byteCodeForClass(String className, List<ByteCode> byteCodes) {
        final ByteCode matchingByteCode = byteCodes.stream()
                .filter(b -> b.getRelatedClassName().equals(className))
                .collect(ExpectOneUniqueElementCollector.expectOneUniqueElement());
        return matchingByteCode;
    }
}
