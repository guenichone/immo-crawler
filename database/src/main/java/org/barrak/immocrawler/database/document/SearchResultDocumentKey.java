package org.barrak.immocrawler.database.document;

import java.util.Objects;

public class SearchResultDocumentKey {

    private ProviderEnum internalProvider;
    private String id;

    public SearchResultDocumentKey(ProviderEnum internalProvider, String id) {
        this.internalProvider = internalProvider;
        this.id = id;
    }

    public ProviderEnum getInternalProvider() {
        return internalProvider;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResultDocumentKey that = (SearchResultDocumentKey) o;
        return internalProvider == that.internalProvider &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(internalProvider, id);
    }
}
