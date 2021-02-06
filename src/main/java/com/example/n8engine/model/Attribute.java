package com.example.n8engine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldId;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldNamespace;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldResource;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonldResource
@JsonldNamespace(name = "n", uri = "http://n8.org/")
@JsonldType("n:Attribute")
@JsonIgnoreProperties("values")
final public class Attribute {

    @JsonldId
    private String id;

    private ArrayList<Value> values;

    public Attribute(String id) {
        this.id = id;
        this.values = new ArrayList<>();
    }

    public void addValue(Value value) {
        this.values.add(value);
    }
}
