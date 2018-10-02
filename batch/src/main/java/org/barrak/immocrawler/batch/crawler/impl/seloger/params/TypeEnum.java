package org.barrak.immocrawler.batch.crawler.impl.seloger.params;

public enum TypeEnum {

    FLAT(1),
    HOUSE(2),
    LAND(4);

    private int typeCode;

    TypeEnum(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return typeCode;
    }
}
