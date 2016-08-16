package castendyck.reporting;

import castendyck.media.Media;
import castendyck.reporting.result.Result;

public class When {
    private final Media media;
    private final MockedLogger mockedLogger;
    private final Result result;

    public When(ReportingExample example) {
        this.media = example.getMedia();
        this.mockedLogger = example.getMockedLogger();
        this.result = example.getResult();
    }

    public Then whenPrintedToConsole() {
        result.print(media);
        media.finishPrinting();
        final String output = mockedLogger.getOutput();
        return new Then(output);
    }
}
