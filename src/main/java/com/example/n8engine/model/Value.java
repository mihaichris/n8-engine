package com.example.n8engine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldId;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldNamespace;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldResource;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonldResource
@JsonldNamespace(name = "n", uri = "http://n8.org/")
@JsonldType("n:Value")
@JsonIgnoreProperties(ignoreUnknown = true)
final public class Value implements Serializable {

    @JsonldId
    private String id;
    @JsonProperty("n:attribute")
    private Attribute attribute;
    @JsonProperty("n:score")
    private String score;

    public Value(String id, Attribute attribute) {
        this.attribute = attribute;
        this.id = id;
    }

    @Override
    public String toString() {
        return this.getAttribute().getId() + " : " + this.getId();
    }
}
