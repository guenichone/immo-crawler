package org.barrak.immocrawler.batch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParserUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParserUtils.class);

    public static String inlineText(String text) {
        return text.replaceAll("\n", "").replaceAll("\t", "");
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
}
