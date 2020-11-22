package com.example.n8engine.model;

import java.util.ArrayList;

final public class Attribute {
    private String name;
    private ArrayList<Value> values;

    public Attribute(String name) {
        this.name = name;
        this.values = new ArrayList<Value>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Value> getValues() {
        return values;
    }

    public void addValue(Value value) {
        this.values.add(value);
    }
}
