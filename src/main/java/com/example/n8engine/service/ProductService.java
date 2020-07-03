package com.example.n8engine.service;

import com.example.n8engine.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductDto findById(String id);

    Page<ProductDto> findByName(String searchTerm, Pageable pageable);
}
