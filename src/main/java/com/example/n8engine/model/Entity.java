package com.example.n8engine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldNamespace;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldResource;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonldResource
@JsonldNamespace(name = "n", uri = "http://n8.org/")
@JsonldType("n:Entity")
@JsonIgnoreProperties(ignoreUnknown = true)
final public class Entity {

    @JsonProperty("@id")
    private String id;
    @JsonProperty("n:values")
    private Set<Value> values;
    @JsonProperty("n:hasScore")
    private String score;
    @JsonProperty("n:graph")
    private String graph;

    public Entity(String id) {
        this.values = new HashSet<>();
        this.id = id;
    }

    public void addValue(Value value) {
        this.values.add(value);
    }
}
