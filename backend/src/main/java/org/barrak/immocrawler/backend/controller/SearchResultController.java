package org.barrak.immocrawler.backend.controller;

import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.crawler.database.repository.SearchResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchResultController {

    @Autowired
    private SearchResultRepository repository;

    @RequestMapping(path = "/")
    public List<SearchResultDocument> findAll() {
        return repository.findAll();
    }

}
