package castendyck.inmemoryjar;

import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;


public class InMemoryJarFileBuilder {
    private final List<JarEntry> jarEntries = new ArrayList<>();

    public static InMemoryJarFileBuilder aJarFile() {
        return new InMemoryJarFileBuilder();
    }

    public InMemoryJarFileBuilder withJarEntriesWithNames(List<String> names) {
        names.stream()
                .forEach(this::withAJarEntryWithName);
        return this;
    }
    public InMemoryJarFileBuilder withAJarEntryWithName(String name) {
        final JarEntry mockedJarEntry = Mockito.mock(JarEntry.class);
        when(mockedJarEntry.getName()).thenReturn(name);
        jarEntries.add(mockedJarEntry);
        return this;
    }

    public InMemoryJarFileBuilder withNoEntries() {
        jarEntries.clear();
        return this;
    }


    public JarFile build() {
        final JarFile mockedJarFile = Mockito.mock(JarFile.class);
        // More complicated, because for every invocation, a new stream has to be created,
        // that is independent from the original List
        when(mockedJarFile.stream()).then((Answer<Stream>) invocationOnMock -> {
            final ArrayList<JarEntry> list = new ArrayList<>(jarEntries);
            return list.stream();
        });


        return mockedJarFile;
    }
}
