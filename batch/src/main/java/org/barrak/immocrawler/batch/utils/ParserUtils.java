package org.barrak.immocrawler.batch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParserUtils.class);

    public static String inlineText(String text) {
        if (text != null) {
            return text.replaceAll("\n", "").replaceAll("\t", "").trim();
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
        List<String> allMatches = new ArrayList<>();
        Matcher m = Pattern.compile("[0-9]+([,.][0-9]{1,2})? ares").matcher(description);
        while (m.find()) {
            allMatches.add(m.group());
        }
        if (allMatches.size() == 1) {
            return (int) ParserUtils.getNumericOnly(allMatches.get(0));
        } else if (allMatches.size() > 1) {
            return allMatches.stream().mapToInt(val -> (int) ParserUtils.getNumericOnly(val)).sum();
        } else {
            return -1;
        }
    }
}
