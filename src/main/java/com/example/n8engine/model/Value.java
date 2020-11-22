package com.example.n8engine.model;

final public class Value {
    private Attribute attribute;
    private String name;

    public Value(String name, Attribute attribute) {
        this.attribute = attribute;
        this.name = name;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.getAttribute().getName() + " : " + this.getName();
    }
}
