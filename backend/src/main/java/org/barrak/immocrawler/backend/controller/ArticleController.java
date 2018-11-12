package org.barrak.immocrawler.backend.controller;

import org.barrak.immocrawler.backend.controller.exception.ResourceNotFoundException;
import org.barrak.immocrawler.backend.model.Article;
import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.model.ArticleDocument;
import org.barrak.immocrawler.database.model.ArticleDocumentKey;
import org.barrak.immocrawler.database.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/articles")
public class ArticleController {

    @Autowired
    private ArticleRepository repository;

    @GetMapping(value = "/{provider}/{id}")
    @ResponseBody
    public Article getById(@PathVariable("provider") ProviderEnum provider, @PathVariable("id") String id) {
        ArticleDocumentKey key = new ArticleDocumentKey(provider, id);
        return new Article(repository.findById(key).orElseThrow(() -> new ResourceNotFoundException()));
    }

    @GetMapping(value = "/{provider}/{id}/sold")
    @ResponseBody
    public Article setArticleSold(@PathVariable("provider") ProviderEnum provider, @PathVariable("id") String id) {
        ArticleDocumentKey key = new ArticleDocumentKey(provider, id);
        ArticleDocument article = repository.findById(key).orElseThrow(() -> new ResourceNotFoundException());
        article.setSold(true);
        repository.save(article);
        return new Article(article);
    }
}
