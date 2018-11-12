package org.barrak.immocrawler.backend.controller;

import org.barrak.immocrawler.backend.model.Article;
import org.barrak.immocrawler.database.model.ArticleDocument;
import org.barrak.immocrawler.database.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/search")
public class SearchResultController {

    @Autowired
    private ArticleRepository repository;

    @GetMapping(path = "/all")
    public List<ArticleDocument> findAll() {
        return repository.findAll();
    }

    @GetMapping(value = "/landSurfaceGreaterThan/{surface}")
    public List<Article> findByLandSurfaceGreaterThan(@PathVariable("surface") int surface) {
        return mapArticleList(repository.findByLandSurfaceGreaterThanAndNotSold(surface));
    }

    @GetMapping(value = "/landSurfaceGreaterThan/{surface}/link")
    public List<String> findByLandSurfaceGreaterThanAndNotSoldLink(@PathVariable("surface") int surface) {
        return repository.findByLandSurfaceGreaterThanAndNotSold(surface).stream()
                .map(res -> res.getUrl()).collect(Collectors.toList());
    }

    @GetMapping(value = "/homeSurfaceGreaterThan/{surface}")
    public List<Article> findByHomeSurfaceGreaterThanAndNotSold(@PathVariable("surface") int surface) {
        return mapArticleList(repository.findByHomeSurfaceGreaterThanAndNotSold(surface));
    }

    @GetMapping(value = "/homeSurfaceGreaterThan/{surface}/link")
    public List<String> findByHomeSurfaceGreaterThanAndNotSoldLink(@PathVariable("surface") int surface) {
        return repository.findByHomeSurfaceGreaterThanAndNotSold(surface).stream()
                .map(res -> res.getUrl()).collect(Collectors.toList());
    }

    private List<Article> mapArticleList(List<ArticleDocument> articleDocumentList) {
        return articleDocumentList.stream()
                .map(articleDocument -> new Article(articleDocument))
                .collect(Collectors.toList());
    }

}
