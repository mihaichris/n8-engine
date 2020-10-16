package com.example.n8engine.model;

import java.util.ArrayList;

public class Entity {
    private ArrayList<Value> values;
    private String name;

    public Entity(ArrayList<Value> values, String name) {
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
