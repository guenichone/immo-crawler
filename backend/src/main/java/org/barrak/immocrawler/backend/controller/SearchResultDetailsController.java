package org.barrak.immocrawler.backend.controller;

import org.barrak.crawler.database.document.SearchResultDetailsDocument;
import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.crawler.database.repository.SearchResultDetailsRepository;
import org.barrak.crawler.database.repository.SearchResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/details")
public class SearchResultDetailsController {

    @Autowired
    private SearchResultDetailsRepository repository;

    @RequestMapping("/")
    public List<SearchResultDetailsDocument> findAll() {
        return repository.findAll();
    }

}
