package castendyck.analyzing.reducing.internal;

import castendyck.analyzing.callfinding.CallFinder;
import castendyck.analyzing.callfinding.CallMatcher;
import castendyck.analyzing.reducing.Reducer;
import castendyck.analyzing.reducing.ReducerConfiguration;
import castendyck.callgraph.CallPath;
import castendyck.classidentifier.ClassIdentifier;
import castendyck.cve.CVE;
import castendyck.processedcve.ProcessedCve;
import castendyck.reduction.Reduction;
import castendyck.reduction.ReductionBuilder;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static castendyck.reduction.ReductionBuilder.aReduction;

public class SocketUsedPatternReducerImpl implements Reducer {
    private final Pattern cvePattern = Pattern.compile("Double free vulnerability in PostgreSQL before 9\\.0\\.20, 9\\.1\\.x before 9\\.1\\.16, 9\\.2\\.x " +
            "before 9\\.2\\.11, 9\\.3.x before 9\\.3\\.7, and 9\\.4\\.x before 9\\.4\\.2 allows remote attackers to cause a denial of service (crash) by " +
            "closing an SSL session at a time when the authentication timeout will expire during the session shutdown sequence\\.");

    public SocketUsedPatternReducerImpl(ReducerConfiguration reducerConfiguration) {
    }

    @Override
    public Reduction reduceFurther(Reduction previousReduction, ProcessedCve processedCve) {
        final CVE cve = processedCve.getCve();
        final String description = cve.getDescription();
        final Matcher matcher = cvePattern.matcher(description);
        if (matcher.matches()) {
            final List<CallPath> remainingCallPaths = previousReduction.getRemainingCallPaths();
            final List<CallPath> callPathsUsingSessions = remainingCallPaths.stream()
                    .filter(usesSocket())
                    .collect(Collectors.toList());

            final String reason = getReason();
            final Reduction reduction = aReduction()
                    .containing(callPathsUsingSessions)
                    .withReason(reason)
                    .build();
            return reduction;
        } else {
            return previousReduction;
        }
    }

    private Predicate<CallPath> usesSocket() {
        final CallMatcher socketCallsMatcher = functionIdentifier -> {
            final ClassIdentifier classIdentifier = functionIdentifier.getClassIdentifier();
            final String className = classIdentifier.getClassName();
            final String name = className.toLowerCase();
            final boolean usesSession = name.contains("session") || name.contains("connection") || name.contains("ssl") || name.contains("tls");
            return usesSession;
        };
        return cp -> CallFinder.findCallsThatMatch(cp, socketCallsMatcher).size() > 0;
    }

    @Override
    public String getReason() {
        return "vulnerability needs socket, but none were used";
    }

}
