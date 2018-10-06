package org.barrak.immocrawler.batch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParserUtils.class);

    public static String inlineText(String text) {
        if (text != null) {
            return text
                    .replaceAll("\r", " ")
                    .replaceAll("\n", "")
                    .replaceAll("\t", " ")
                    .trim();
        } else {
            return null;
        }
    }

    public static double getNumericOnly(String text) {
        return getNumericOnly(text, "none");
    }

    public static double getNumericOnly(String text, String context) {
        try {
            return Double.valueOf(text.replaceAll("[^\\d\\,]", "").replaceAll(",", "."));
        } catch (NullPointerException ex) {
            LOGGER.error("NullPointerException for '" + text + "' from " + context);
        } catch (NumberFormatException ex) {
            LOGGER.error("NumberFormatException for '" + text + "' from " + context , ex);
        }
        return -1;
    }

    public static int findLandSurfaceInDescription(String description) {
        List<String> allMatches = matchesByRegex(description, "[0-9]+([,.][0-9]{1,2})? ares");
        if (allMatches.size() == 1) {
            return (int) ParserUtils.getNumericOnly(allMatches.get(0));
        } else if (allMatches.size() > 1) {
            return allMatches.stream().mapToInt(val -> (int) ParserUtils.getNumericOnly(val)).sum();
        } else {
            return -1;
        }
    }

    public static String matchByRegex(String text, String regexp) {
        List<String> results = matchesByRegex(text, regexp);
        return results.isEmpty() ? null : results.get(0);
    }

    public static List<String> matchesByRegex(String text, String regexp) {
        List<String> allMatches = new ArrayList<>();
        Matcher m = Pattern.compile(regexp).matcher(text);
        while (m.find()) {
            allMatches.add(m.group());
        }
        return allMatches;
    }

    public static List<Map<String, String>> matchesByRegexGroup(String text, String regexp, String... names) {
        List<Map<String, String>> allMatches = new ArrayList<>();
        Matcher m = Pattern.compile(regexp).matcher(text);
        while (m.find()) {
            Map<String, String> groups = new HashMap<>();
            for (String name : names) {
                groups.put(name, m.group(name));
            }
            allMatches.add(groups);
        }
        return allMatches;
    }
}
