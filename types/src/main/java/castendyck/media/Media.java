package castendyck.media;

public interface Media {
    ResultLogLevel getResultLogLevel();
    ErrorLogLevel getErrorLogLevel();

    Media starting(String startedElement);

    Media withText(String text);

    Media withNumber(int number);

    Media ending(String endingElement);

    Media with(String type, String value);

    void finishPrinting();
}
