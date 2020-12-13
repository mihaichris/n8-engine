package com.example.n8engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
final public class Value {
    private Attribute attribute;
    private String name;

    public Value(String name, Attribute attribute) {
        this.attribute = attribute;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.getAttribute().getName() + " : " + this.getName();
    }
}
