package castendyck.sourcecode.localrepositorywithsourcecode;

import de.castendyck.collections.ObjectListToStringCollector;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarFileMatcher extends TypeSafeDiagnosingMatcher<JarFile> {
    private final List<String> files;
    private List<String> notYetMatchedFiles;

    public JarFileMatcher(List<String> files) {
        this.files = files;
    }

    public static JarFileMatcher aJarFileContaining(String ... files){
        final List<String> fileList = Arrays.asList(files);
        return new JarFileMatcher(fileList);
    }

    @Override
    protected boolean matchesSafely(JarFile jarFile, Description description) {
        notYetMatchedFiles = new ArrayList<>(files);
        final Enumeration<JarEntry> entries = jarFile.entries();
        while(entries.hasMoreElements()){
            final JarEntry jarEntry = entries.nextElement();
            final String name = jarEntry.getName();
            if(notYetMatchedFiles.contains(name)){
                notYetMatchedFiles.remove(name);
            }
        }

        if(notYetMatchedFiles.isEmpty()){
            return true;
        }else{
            final String notMatchedElements = notYetMatchedFiles.stream()
                    .collect(ObjectListToStringCollector.collectToString());
            throw new AssertionError("could not match "+notMatchedElements +" elements.");
        }
    }

    @Override
    public void describeTo(Description description) {

    }
}
