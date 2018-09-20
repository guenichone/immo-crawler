package org.barrak.immocrawler.crawler.impl.seloger;

public enum NaturesEnum {

    OLD(1),
    NEW(2)

    ;

    private int type;

    NaturesEnum(int type) {
        this.type = type;
    }
}
