package org.barrak.immocrawler.database.model;

import org.barrak.immocrawler.database.document.ProviderEnum;

import java.util.Objects;

public class UserArticleKey {

    private String email;
    private ArticleDocumentKey articleDocumentKey;

    public UserArticleKey() {
    }

    public UserArticleKey(String email, ArticleDocumentKey articleDocumentKey) {
        this.email = email;
        this.articleDocumentKey = articleDocumentKey;
    }

    public UserArticleKey(String email, ProviderEnum provider, String articleId) {
        this.email = email;
        this.articleDocumentKey = new ArticleDocumentKey(provider, articleId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserArticleKey that = (UserArticleKey) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(articleDocumentKey, that.articleDocumentKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, articleDocumentKey);
    }
}
