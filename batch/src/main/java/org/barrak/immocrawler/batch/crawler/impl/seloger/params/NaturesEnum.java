package org.barrak.immocrawler.batch.crawler.impl.seloger.params;

public enum NaturesEnum {

    OLD(1),
    NEW(2)

    ;

    private int type;

    NaturesEnum(int type) {
        this.type = type;
    }
}
