package castendyck.streamfetching;

import castendyck.inmemorycompiling.ByteCode;

import java.io.InputStream;
import java.util.List;

import static de.castendyck.collections.ExpectOneUniqueElementCollector.expectOneUniqueElement;

public class MockedStreamFetcher extends StreamFetcher {
    private final List<ByteCode> byteCodesToProvide;

    private MockedStreamFetcher(List<ByteCode> byteCodesToProvide) {
        this.byteCodesToProvide = byteCodesToProvide;
    }

    public static MockedStreamFetcher forByteCodes(List<ByteCode> byteCodes) {
        return new MockedStreamFetcher(byteCodes);
    }

    @Override
    public InputStream getRessourceAsStream(String ressource) {
        final ByteCode byteCode = byteCodesToProvide.stream()
                .filter(b -> {
                    final String className = b.getRelatedClassName() + ".class";
                    return className.equals(ressource);
                })
                .collect(expectOneUniqueElement());
        return byteCode.getInputStream();
    }
}

