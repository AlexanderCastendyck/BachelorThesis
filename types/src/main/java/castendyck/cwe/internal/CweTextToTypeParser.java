package castendyck.cwe.internal;

import castendyck.cwe.CweType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CweTextToTypeParser {
    private final static Pattern CWE_PATTERN = Pattern.compile("(?<ID>CWE-[\\d]+)( ?:? ?)(.*)");

    public static CweType parse(String input){
        if(input == null){
            return CweType.CWE_UNKNOWN;
        }

        final String trimmedInput = input.trim();
        final Matcher matcher = CWE_PATTERN.matcher(trimmedInput);
        if(matcher.matches()){
            final String id = matcher.group("ID");
            final String formattedId = formatId(id);
            return getCweTypeFor(formattedId);
        }else{
            return CweType.CWE_UNKNOWN;
        }
    }

    private static String formatId(String id) {
        return id.replace("-", "_");
    }

    private static CweType getCweTypeFor(String id) {
        try {
            final CweType cweType = CweType.valueOf(id);
            return cweType;
        }catch (IllegalArgumentException e){
            return CweType.CWE_UNKNOWN;
        }
    }
}
