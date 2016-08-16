package castendyck.media;

import castendyck.media.internal.MavenLoggingMediaImpl;
import org.apache.maven.plugin.logging.Log;

public class MediaFactory {
    public static Media mediaToMavenConsole(Log log, ResultLogLevel resultLogLevel, ErrorLogLevel errorLogLevel){
        return new MavenLoggingMediaImpl(log, resultLogLevel, errorLogLevel);
    }
}
