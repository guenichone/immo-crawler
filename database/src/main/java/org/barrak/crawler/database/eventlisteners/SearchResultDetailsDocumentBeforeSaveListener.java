package org.barrak.crawler.database.eventlisteners;

import org.barrak.crawler.database.document.SearchResultDetailsDocument;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SearchResultDetailsDocumentBeforeSaveListener extends AbstractMongoEventListener<SearchResultDetailsDocument> {

    @Override
    public void onBeforeSave(BeforeSaveEvent<SearchResultDetailsDocument> event) {
        SearchResultDetailsDocument document = event.getSource();
        document.setCreated(LocalDateTime.now());
    }

}
