package org.barrak.crawler.database.eventlisteners;

import org.barrak.crawler.database.document.SearchResultDocument;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SearchResultDocumentBeforeSaveListener extends AbstractMongoEventListener<SearchResultDocument> {

    @Override
    public void onBeforeSave(BeforeSaveEvent<SearchResultDocument> event) {
        SearchResultDocument document = event.getSource();
        document.setCreated(LocalDateTime.now());
    }

}
