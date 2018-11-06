package org.barrak.immocrawler.backend.controller;

import org.barrak.immocrawler.database.model.ArticleDocument;
import org.barrak.immocrawler.database.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/articles")
public class SearchResultController {

    @Autowired
    private ArticleRepository repository;

    @GetMapping(path = "/all")
    public List<ArticleDocument> findAll() {
        return repository.findAll();
    }

    @GetMapping(value = "/search/landSurfaceGreaterThan/{surface}")
    public List<ArticleDocument> findByLandSurfaceGreaterThan(@PathVariable("surface") int surface) {
        return repository.findByLandSurfaceGreaterThan(surface);
    }

    @GetMapping(value = "/search/landSurfaceGreaterThan/{surface}/link")
    public List<String> findByLandSurfaceGreaterThanLink(@PathVariable("surface") int surface) {
        return repository.findByLandSurfaceGreaterThan(surface).stream()
                .map(res -> res.getUrl()).collect(Collectors.toList());
    }

    @GetMapping(value = "/search/homeSurfaceGreaterThan/{surface}")
    public List<ArticleDocument> findByHomeSurfaceGreaterThan(@PathVariable("surface") int surface) {
        return repository.findByHomeSurfaceGreaterThan(surface);
    }

    @GetMapping(value = "/search/homeSurfaceGreaterThan/{surface}/link")
    public List<String> findByHomeSurfaceGreaterThanLink(@PathVariable("surface") int surface) {
        return repository.findByHomeSurfaceGreaterThan(surface).stream()
                .map(res -> res.getUrl()).collect(Collectors.toList());
    }

}
