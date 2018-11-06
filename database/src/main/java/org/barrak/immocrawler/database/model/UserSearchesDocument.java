package org.barrak.immocrawler.database.model;

import org.barrak.immocrawler.database.document.ProviderEnum;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "userSearches")
public class UserSearchesDocument extends UserArticleDocument {

    public UserSearchesDocument() {
    }

    public UserSearchesDocument(String email, ProviderEnum provider, String articleId) {
        super(email, provider, articleId);
    }

}
