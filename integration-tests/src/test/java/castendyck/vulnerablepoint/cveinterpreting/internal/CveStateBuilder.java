package castendyck.vulnerablepoint.cveinterpreting.internal;

import castendyck.cve.CVE;
import castendyck.cve.CveTestBuilder;
import castendyck.maven.pomfile.PomFile;
import castendyck.vulnerablepoint.usedcpefinding.UsedCpeFinder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.mockito.Mockito.when;

public class CveStateBuilder {
    private final CVE cve;

    public CveStateBuilder(CVE cve) {
        this.cve = cve;
    }

    public static CveStateBuilder aCve(CveTestBuilder cveTestBuilder) {
        final CVE cve = cveTestBuilder.build();
        return new CveStateBuilder(cve);
    }

    public CveInterpreterTestExample build() {
        final UsedCpeFinder mockedCpeFinder = Mockito.mock(UsedCpeFinder.class);
        final ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        final ArgumentCaptor<PomFile> argumentCaptor2 = ArgumentCaptor.forClass(PomFile.class);
        when(mockedCpeFinder.findUsedCpesIn(argumentCaptor2.capture(), argumentCaptor.capture())).thenAnswer((Answer<List>) invocationOnMock -> argumentCaptor.getValue());
        final CveInterpreterForFunctionKeywordImpl interpreter = new CveInterpreterForFunctionKeywordImpl(mockedCpeFinder);
        final PomFile mockedPomFile = Mockito.mock(PomFile.class);
        return new CveInterpreterTestExample(interpreter, cve, mockedPomFile, null);
    }
}
