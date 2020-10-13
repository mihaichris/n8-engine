package com.example.n8engine.model;

public class Value {
    private Attribute attribute;
    private String name;

    public Value(Attribute attribute, String name) {
        this.attribute = attribute;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Value{" +
                "attribute=" + this.attribute +
                ", name='" + this.name + '\'' +
                '}';
    }
}
