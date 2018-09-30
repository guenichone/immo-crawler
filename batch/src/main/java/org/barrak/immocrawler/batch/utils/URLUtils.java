package org.barrak.immocrawler.batch.utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class URLUtils {

    public static void updateUrlParam(List<NameValuePair> params, String paramName,
                                                     Function<NameValuePair, NameValuePair> processParam) {
        updateUrlParam(params, paramName, processParam, null);
    }

    public static void updateUrlParam(List<NameValuePair> params, String paramName, String value) {
        updateUrlParam(params, paramName, null, value);
    }

    public static void updateUrlParam(List<NameValuePair> params, String paramName,
                              Function<NameValuePair, NameValuePair> processParam, String valueIfAbsent) {

        Optional<NameValuePair> optNameParam = params.stream()
                .filter(kvp -> paramName.equals(kvp.getName()))
                .findFirst();

        if (optNameParam.isPresent()) {
            if (processParam != null) {
                NameValuePair nameParam = optNameParam.get();
                params.remove(nameParam);
                params.add(processParam.apply(nameParam));
            }
        } else {
            if (valueIfAbsent != null) {
                params.add(new BasicNameValuePair(paramName, valueIfAbsent));
            }
        }
    }
}
