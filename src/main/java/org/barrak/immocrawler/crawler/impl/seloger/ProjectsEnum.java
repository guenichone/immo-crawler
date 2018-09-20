package org.barrak.immocrawler.crawler.impl.seloger;

public enum ProjectsEnum {
    RENT(1),
    BUY(2),
    BUILD(3),
    VIAGER(5);

    private int projectCode;

    ProjectsEnum(int projectCode) {
        this.projectCode = projectCode;
    }

    public int getProjectCode() {
        return projectCode;
    }
}
