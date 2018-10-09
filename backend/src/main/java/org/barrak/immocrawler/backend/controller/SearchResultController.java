package org.barrak.immocrawler.backend.controller;

import org.barrak.immocrawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.database.repository.SearchResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/")
public class SearchResultController {

    @Autowired
    private SearchResultRepository repository;

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public List<SearchResultDocument> findAll() {
        return repository.findAll();
    }

    @RequestMapping(value = "/search/landSurfaceGreaterThan/{surface}", method = RequestMethod.GET)
    public List<SearchResultDocument> findByLandSurfaceGreaterThan(@PathVariable("surface") int surface) {
        return repository.findByLandSurfaceGreaterThan(surface);
    }

    @RequestMapping(value = "/search/landSurfaceGreaterThan/{surface}/link", method = RequestMethod.GET)
    public List<String> findByLandSurfaceGreaterThanLink(@PathVariable("surface") int surface) {
        return repository.findByLandSurfaceGreaterThan(surface).stream()
                .map(res -> res.getUrl()).collect(Collectors.toList());
    }

    @RequestMapping(value = "/search/homeSurfaceGreaterThan/{surface}", method = RequestMethod.GET)
    public List<SearchResultDocument> findByHomeSurfaceGreaterThan(@PathVariable("surface") int surface) {
        return repository.findByHomeSurfaceGreaterThan(surface);
    }

    @RequestMapping(value = "/search/homeSurfaceGreaterThan/{surface}/link", method = RequestMethod.GET)
    public List<String> findByHomeSurfaceGreaterThanLink(@PathVariable("surface") int surface) {
        return repository.findByHomeSurfaceGreaterThan(surface).stream()
                .map(res -> res.getUrl()).collect(Collectors.toList());
    }

    @RequestMapping(path = "/{url}/favorite", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void addToFavorite(@PathVariable("url") String url) {
        SearchResultDocument result = repository.findById(url).get();
        result.setFavorite(true);
        repository.save(result);
    }

    @RequestMapping(path = "/{url}/favorite", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void removeFromFavorite(@PathVariable("url") String url) {
        SearchResultDocument result = repository.findById(url).get();
        result.setFavorite(false);
        repository.save(result);
    }

}
