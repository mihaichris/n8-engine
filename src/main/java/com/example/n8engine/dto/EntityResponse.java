package com.example.n8engine.dto;

import java.util.Set;

public class EntityResponse {
    private Object entity;
    private Set<Object> ontologyProperties;

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public Set<Object> getOntologyProperties() {
        return ontologyProperties;
    }

    public void setOntologyProperties(Set<Object> ontologyProperties) {
        this.ontologyProperties = ontologyProperties;
    }
}
