package com.example.n8engine.model;

import java.util.ArrayList;

final public class Entity {
    private ArrayList<Value> values;
    private String name;

    public Entity(String name, ArrayList<Value> values) {
        this.values = values;
        this.name = name;
    }

    public ArrayList<Value> getValues() {
        return values;
    }

    public String getName() {
        return name;
    }
}
