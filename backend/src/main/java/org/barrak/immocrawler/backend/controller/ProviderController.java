package org.barrak.immocrawler.backend.controller;

import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.model.ArticleDocument;
import org.barrak.immocrawler.database.repository.ArticleByProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/providers")
public class ProviderController {

    @Autowired
    private ArticleByProviderRepository repository;

    @GetMapping(path = "/{provider}/all")
    public List<ArticleDocument> findAll(@PathVariable("provider") ProviderEnum provider) {
        return repository.findByInternalProvider(provider);
    }

    @DeleteMapping(path = "/{provider}/all")
    public void deleteAll(@PathVariable("provider") ProviderEnum provider) {
        repository.deleteByInternalProvider(provider);
    }

    @GetMapping(path = "/{provider}/cleanDetails")
    @ResponseStatus(HttpStatus.OK)
    public void cleanDetails(@PathVariable("provider") ProviderEnum provider) {
        List<ArticleDocument> results = repository.findByInternalProvider(provider);
        results.stream().forEach(article -> article.setDetailsParsed(false));
        repository.saveAll(results);
    }
}
