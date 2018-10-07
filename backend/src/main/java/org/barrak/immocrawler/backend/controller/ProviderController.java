package org.barrak.immocrawler.backend.controller;

import org.barrak.crawler.database.document.ProviderEnum;
import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.crawler.database.repository.SearchResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/admin")
public class ProviderController {

    @Autowired
    private SearchResultRepository repository;

    @RequestMapping(path = "/{provider}/all", method = RequestMethod.GET)
    public List<SearchResultDocument> findAll(@PathVariable("provider") ProviderEnum provider) {
        return repository.findByInternalProvider(provider);
    }

    @RequestMapping(path = "/{provider}/cleanDetails", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void cleanDetails(@PathVariable("provider") ProviderEnum provider) {
        List<SearchResultDocument> results = repository.findByInternalProvider(provider);
        results.stream().forEach(article -> article.setDetailsParsed(false));
        repository.saveAll(results);
    }
}
