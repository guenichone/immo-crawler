package org.barrak.immocrawler.database.model;

import org.barrak.immocrawler.database.document.ProviderEnum;
import org.springframework.data.annotation.Id;

public abstract class UserArticleDocument {

    @Id
    private UserArticleKey favoriteKey;

    public UserArticleDocument() {
    }

    public UserArticleDocument(UserArticleKey favoriteKey) {
        this.favoriteKey = favoriteKey;
    }

    public UserArticleDocument(String email, ProviderEnum provider, String articleId) {
        this.favoriteKey = new UserArticleKey(email, provider, articleId);
    }

    public UserArticleKey getFavoriteKey() {
        return favoriteKey;
    }
}
