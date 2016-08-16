package castendyck.maven.pomfile;

import castendyck.maven.pomfile.internal.PomFileImpl;
import castendyck.pomfilelocator.PomFileLocator;
import castendyck.pomfilelocator.PomFileLocatorFactory;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class PomFileFactory {

    public static PomFile createFromPath(Path path) throws PomFileCreationException {
        final PomFileLocator pomFileLocator = PomFileLocatorFactory.createDefaultOne();
        return createFromPath(path, pomFileLocator);
    }
    public static PomFile createFromPath(Path path, PomFileLocator pomFileLocator) throws PomFileCreationException {
        final File file = path.toFile();
        return createFromFile(file, pomFileLocator);
    }

    public static PomFile createFromFile(File file) throws PomFileCreationException {
        PomFileLocator pomFileLocator = PomFileLocatorFactory.createDefaultOne();
        return createFromFile(file, pomFileLocator);
    }

    public static PomFile createFromFile(File file, PomFileLocator pomFileLocator) throws PomFileCreationException {
        return createPomFile(file, pomFileLocator);
    }

    private static PomFile createPomFile(File file, PomFileLocator pomFileLocator) throws PomFileCreationException {
        final Model model = createModelFromFile(file);
        model.setPomFile(file);

        final PomFileImpl pomFile = new PomFileImpl(model, pomFileLocator);
        return pomFile;
    }

    private static Model createModelFromFile(File file) throws PomFileCreationException {
        try {
            FileReader fileReader = new FileReader(file);
            MavenXpp3Reader mavenReader = new MavenXpp3Reader();
            return mavenReader.read(fileReader);
        } catch (XmlPullParserException | IOException e) {
            throw new PomFileCreationException("Could note create PomFile from File: " + file, e);
        }
    }

}
