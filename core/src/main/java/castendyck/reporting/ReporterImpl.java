package castendyck.reporting;

import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.media.Media;
import castendyck.reporting.result.Result;

public class ReporterImpl implements Reporter {
    private final Media media;

    public ReporterImpl(Media media) {
        NotNullConstraintEnforcer.ensureNotNull(media);
        this.media = media;
    }

    @Override
    public void report(Result result) {
        result.print(media);
        media.finishPrinting();
    }
}
