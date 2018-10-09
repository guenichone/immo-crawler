package org.barrak.immocrawler.database.document;

public enum ProviderEnum {

    // CRAWLER
    IMMOREGION(true),
    SELOGER(true),
    // PRIVATE
    PANETTA_IMMO,
    LEBONCOIN_PARTICULIER,
    IMMO_CASCIOLA,
    OPTIMHOME,
    ;

    private boolean isCrawler;

    ProviderEnum() {
        this(false);
    }

    ProviderEnum(boolean isCrawler) {
        this.isCrawler = isCrawler;
    }

    public boolean isCrawler() {
        return isCrawler;
    }
}
