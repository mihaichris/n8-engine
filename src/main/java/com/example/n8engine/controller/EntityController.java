package com.example.n8engine.controller;


import com.example.n8engine.mapper.JsonLdMapper;
import com.example.n8engine.model.Entity;
import com.example.n8engine.searcher.Searcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Slf4j
@RequestMapping("/api/entity")
final public class EntityController {
    private final JsonLdMapper jsonLdMapper;
    private final Searcher searcher;

    public EntityController(@Qualifier("searcher") Searcher searcher, JsonLdMapper jsonLdMapper) {
        this.searcher = searcher;
        this.jsonLdMapper = jsonLdMapper;
    }

    @PostMapping()
    public  Object findByEntity(@RequestBody String Id) {
        try {
            Entity entity = this.searcher.findEntityByURI(Id);
            return this.jsonLdMapper.mapFromEntity(entity);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception);
        }
    }
}
