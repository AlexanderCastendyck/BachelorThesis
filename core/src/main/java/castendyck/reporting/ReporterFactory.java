package castendyck.reporting;

import castendyck.media.ErrorLogLevel;
import castendyck.media.Media;
import castendyck.media.MediaFactory;
import castendyck.media.ResultLogLevel;
import org.apache.maven.plugin.logging.Log;

public class ReporterFactory {
    public static Reporter newInstanceForMavenConsole(Log log) {
        final Media media = MediaFactory.mediaToMavenConsole(log, ResultLogLevel.ALL, ErrorLogLevel.ALL);
        return new ReporterImpl(media);
    }
}
