package com.example.n8engine.reponse;

import com.example.n8engine.dto.ProductDto;

import java.util.ArrayList;

public class SearchResponse {

    ArrayList<ProductDto> products;

    public ArrayList<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductDto> products) {
        this.products = products;
    }
}
