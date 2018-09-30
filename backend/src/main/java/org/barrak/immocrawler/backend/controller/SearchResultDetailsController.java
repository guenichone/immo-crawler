package org.barrak.immocrawler.backend.controller;

import org.barrak.crawler.database.document.SearchResultDetailsDocument;
import org.barrak.crawler.database.repository.SearchResultDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/surfaceGreaterThan/{surface}")
    public List<SearchResultDetailsDocument> findByLandSurfaceGreaterThan(@PathVariable("surface") int surface) {
        return repository.findByLandSurfaceGreaterThan(surface);
    }

    @RequestMapping("/top20")
    public List<SearchResultDetailsDocument> findTop20() {
        return repository.findAll(PageRequest.of(0, 20)).getContent();
    }

}
