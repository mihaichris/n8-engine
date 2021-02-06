package com.example.n8engine.mapper;

import com.example.n8engine.model.Entity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jsonldjava.utils.JsonUtils;
import ioinformarics.oss.jackson.module.jsonld.JsonldModule;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class JsonLdMapper {

    private final ObjectMapper objectMapper;

    public Object mapFromEntity(Entity entity) {
        objectMapper.registerModule(new JsonldModule());
        try {
            String personJsonLd = objectMapper.writeValueAsString(entity);
            return JsonUtils.fromString(personJsonLd);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
        return null;
    }
}
