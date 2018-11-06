package org.barrak.immocrawler.database.model;

import org.barrak.immocrawler.database.document.ProviderEnum;

import java.util.Objects;

public class ArticleDocumentKey {

    private ProviderEnum provider;
    private String id;

    public ArticleDocumentKey(ProviderEnum provider, String id) {
        this.provider = provider;
        this.id = id;
    }

    public ProviderEnum getProvider() {
        return provider;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleDocumentKey that = (ArticleDocumentKey) o;
        return provider == that.provider &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, id);
    }
}
