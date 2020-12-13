package com.example.n8engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.jena.rdf.model.RDFNode;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
final public class Entity {
    private Set<Value> values;
    private String name;
    private String score;
    private RDFNode graph;

    public void addValue(Value value) {
        this.values.add(value);
    }
}
