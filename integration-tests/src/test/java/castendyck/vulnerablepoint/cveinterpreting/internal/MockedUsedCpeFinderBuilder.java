package castendyck.vulnerablepoint.cveinterpreting.internal;

import castendyck.cpe.CPE;
import castendyck.vulnerablepoint.usedcpefinding.UsedCpeFinder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MockedUsedCpeFinderBuilder {
    public static MockedUsedCpeFinderBuilder aUsedCpeFinder() {
        return new MockedUsedCpeFinderBuilder();
    }

    public UsedCpeFinder thatReturnsAllCpesAsUsed() {
        final UsedCpeFinder usedCpeFinder = Mockito.mock(UsedCpeFinder.class);
        final ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        when(usedCpeFinder.findUsedCpesIn(any(), argumentCaptor.capture())).thenAnswer((Answer<List<CPE>>) invocationOnMock -> argumentCaptor.getValue());
        return usedCpeFinder;
    }
}
