package castendyck.vulnerablepoint.usedcpefinding;

import castendyck.cpe.CPE;
import castendyck.maven.pomfile.PomFile;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static castendyck.analyzing.reducing.internal.MockedPomFileBuilder.aPomFile;
import static castendyck.analyzing.reducing.internal.MockedPomFileBuilder.aSubModule;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class UsedCpeFinderTestIT {
    private final UsedCpeFinder finder = UsedCpeFinderFactory.newInstance();

    @Test
    public void findUsedCpesIn_returnsEmptyListForEmptyInputCpeList() throws Exception {
        final List<CPE> cpes = new ArrayList<>();
        final PomFile pomFile = aPomFile()
                .withADependency("vendor", "product", "1.0")
                .build();
        final List<CPE> usedCpes = act(cpes, pomFile);
        assertThat(usedCpes, hasSize(0));
    }

    @Test
    public void findUsedCpesIn_returnsEmptyListForPomFileWithoutDependencies() throws Exception {
        final List<CPE> cpes = new ArrayList<>();
        final PomFile pomFile = aPomFile()
                .withNoDependencies()
                .build();
        final List<CPE> usedCpes = act(cpes, pomFile);
        assertThat(usedCpes, hasSize(0));
    }

    @Test
    public void findUsedCpesIn_returnsOneMatchingCpe() throws Exception {
        final List<CPE> cpes = Collections.singletonList(
                CPE.createNew("cpe:/a:vendor:product:1.0")
        );
        final PomFile pomFile = aPomFile()
                .withADependency("vendor", "product", "1.0")
                .build();
        final List<CPE> usedCpes = act(cpes, pomFile);
        assertThat(usedCpes, equalTo(cpes));
    }

    @Test
    public void findUsedCpesIn_returnsAllMatchingCpes() throws Exception {
        final List<CPE> cpes = Arrays.asList(
                CPE.createNew("cpe:/a:junit:junit:1.9"),
                CPE.createNew("cpe:/a:owasp:owasp-dependency-check:0.0"),
                CPE.createNew("cpe:/a:not:matchedCve:1.0")
        );
        final PomFile pomFile = aPomFile()
                .withADependency("junit", "junit", "1.9")
                .withADependency("owasp", "owasp-dependency-check", "0.0")
                .build();

        final List<CPE> usedCpes = act(cpes, pomFile);

        assertThat(usedCpes, contains(
                CPE.createNew("cpe:/a:junit:junit:1.9"),
                CPE.createNew("cpe:/a:owasp:owasp-dependency-check:0.0")
        ));
    }

    @Test
    public void findUsedCpesIn_findsCpesInSubModulesToo() throws Exception {
        final List<CPE> cpes = Arrays.asList(
                CPE.createNew("cpe:/a:junit:junit:1.9"),
                CPE.createNew("cpe:/a:hamcrest:hamcrest:3.0"),
                CPE.createNew("cpe:/a:not:matchedCve:1.0")
        );
        final PomFile pomFile = aPomFile()
                .withADependency("junit", "junit", "1.9")
                .withASubmodule(aSubModule()
                        .withADependency("hamcrest", "hamcrest", "3.0"))
                .build();

        final List<CPE> usedCpes = act(cpes, pomFile);

        assertThat(usedCpes, contains(
                CPE.createNew("cpe:/a:junit:junit:1.9"),
                CPE.createNew("cpe:/a:hamcrest:hamcrest:3.0")
        ));
    }

    @Test
    public void findUsedCpesIn_findsCpesWithCorrectVersion() throws Exception {
        final List<CPE> cpes = Arrays.asList(
                CPE.createNew("cpe:/a:hamcrest:hamcrest:100.0"),
                CPE.createNew("cpe:/a:hamcrest:hamcrest:33.0"),
                CPE.createNew("cpe:/a:hamcrest:hamcrest:0.0")
        );
        final PomFile pomFile = aPomFile()
                .withADependency("hamcrest", "hamcrest", "33.0")
                .build();

        final List<CPE> usedCpes = act(cpes, pomFile);

        assertThat(usedCpes, contains(
                CPE.createNew("cpe:/a:hamcrest:hamcrest:33.0")
        ));
    }

    @Test
    public void findUsedCpesIn_findsCpeEvenWhenGroupIsSlightlyDifferent() throws Exception {
        final List<CPE> cpes = Collections.singletonList(
                CPE.createNew("cpe:/a:junit:junit:1.9")
        );
        final PomFile pomFile = aPomFile()
                .withADependency("nearly.junit", "junit", "1.9")
                .build();

        final List<CPE> usedCpes = act(cpes, pomFile);

        assertThat(usedCpes, contains(
                CPE.createNew("cpe:/a:junit:junit:1.9")
        ));
    }

    private List<CPE> act(List<CPE> cpes, PomFile pomFile) {
        return finder.findUsedCpesIn(pomFile, cpes);
    }
}