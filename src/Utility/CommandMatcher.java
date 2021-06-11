package Utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandMatcher {
    public static Matcher getCommandMatcher(String input, String regex) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(input);
        return matcher;
    }

}
