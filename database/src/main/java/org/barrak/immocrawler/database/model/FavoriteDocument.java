package org.barrak.immocrawler.database.model;

import org.barrak.immocrawler.database.document.ProviderEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "favorites")
public class FavoriteDocument extends UserArticleDocument {

    public FavoriteDocument() {
    }

    public FavoriteDocument(String email, ProviderEnum provider, String articleId) {
        super(email, provider, articleId);
    }
}
