package org.barrak.immocrawler.batch.crawler.impl.leboncoin;

import org.jsoup.Connection;

public class FakeBrowserConnectionUpdater {

    public static Connection addConnectionParams(Connection connection) {
        return connection.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
                .header("connection", "keep-alive")
                .header("upgrade-insecure-requests", "1")
                .header("dnt", "1")
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("accept-encoding", "gzip, deflate, br")
                .header("accept-language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7,nl;q=0.6");
    }

}
