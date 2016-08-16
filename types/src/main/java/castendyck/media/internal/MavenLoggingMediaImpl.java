package castendyck.media.internal;

import castendyck.media.ErrorLogLevel;
import castendyck.media.Media;
import castendyck.media.ResultLogLevel;
import org.apache.maven.plugin.logging.Log;

public class MavenLoggingMediaImpl implements Media {
    private final Log log;
    private final ResultLogLevel resultLogLevel;
    private final ErrorLogLevel errorLogLevel;
    private final StringBuilder stringBuilder;

    public MavenLoggingMediaImpl(Log log, ResultLogLevel resultLogLevel, ErrorLogLevel errorLogLevel) {
        this.log = log;
        this.resultLogLevel = resultLogLevel;
        this.errorLogLevel = errorLogLevel;
        this.stringBuilder = new StringBuilder();
    }

    @Override
    public ResultLogLevel getResultLogLevel() {
        return resultLogLevel;
    }

    @Override
    public ErrorLogLevel getErrorLogLevel() {
        return errorLogLevel;
    }

    @Override
    public Media starting(String startedElement) {
        return this;
    }

    @Override
    public Media withText(String text) {
        stringBuilder.append(text);
        return this;
    }

    @Override
    public Media withNumber(int number) {
        stringBuilder.append(number);
        return this;
    }

    @Override
    public Media ending(String endingElement) {
        if (endingElement.equalsIgnoreCase("header")) {
            stringBuilder.append("\n");
        }
        if (endingElement.equalsIgnoreCase("analyzedCve")) {
            stringBuilder.append("\n");
        }
        return this;
    }

    @Override
    public Media with(String type, String value) {
        if (type.equalsIgnoreCase("certainty")) {
            final String lowerCase = value.toLowerCase();
            stringBuilder.append("certainty=")
                    .append(lowerCase);
        } else if (type.equalsIgnoreCase("risk")) {
            final String lowerCase = value.toLowerCase();
            stringBuilder.append("risk=")
                    .append(lowerCase);
        } else if (type.equalsIgnoreCase("exceptionClass")) {
            stringBuilder.append("Exception ")
                    .append(value)
                    .append(" was thrown ");
        } else if (type.equalsIgnoreCase("exceptionMessage")) {
            stringBuilder.append("with message ")
                    .append(value);
        }else{
            stringBuilder.append(value);
        }
        return this;
    }

    @Override
    public void finishPrinting() {
        final String output = stringBuilder.toString();
        log.info(output);
    }
}
