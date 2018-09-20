package org.barrak.immocrawler.crawler.impl.seloger;

import com.sun.org.apache.bcel.internal.generic.LAND;

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
