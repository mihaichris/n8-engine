package com.example.n8engine.service;

import com.example.n8engine.dto.ProductDto;
import com.example.n8engine.model.Product;
import com.example.n8engine.repository.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Pattern IGNORED_CHARS_PATTERN = Pattern.compile("\\p{Punct}");

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDto findById(String id) {
        ProductDto productDto = new ProductDto();
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new EntityNotFoundException("id-" + id);
        }
        BeanUtils.copyProperties(product.get(), productDto);
        return productDto;
    }

    @Override
    public Page<ProductDto> findByName(String searchTerm, Pageable pageable) {
        if (StringUtils.isBlank(searchTerm)) {
            Page<Product> products = productRepository.findAll(pageable);
            return new PageImpl<ProductDto>(convert(products.getContent()), pageable, products.getTotalElements());
        }
        Page<Product> products = productRepository.findByNameIn(splitSearchTermAndRemoveIgnoredCharacters(searchTerm), pageable);
        return new PageImpl<ProductDto>(convert(products.getContent()), pageable, products.getTotalElements());
    }

    private Collection<String> splitSearchTermAndRemoveIgnoredCharacters(String searchTerm) {
        String[] searchTerms = StringUtils.split(searchTerm, " ");
        List<String> result = new ArrayList<String>(searchTerms.length);
        for (String term : searchTerms) {
            if (StringUtils.isNotEmpty(term)) {
                result.add(IGNORED_CHARS_PATTERN.matcher(term).replaceAll(" "));
            }
        }
        return result;
    }

    private List<ProductDto> convert(List<Product> products) {
        List<ProductDto> productDtos = new ArrayList<ProductDto>();
        for (Product product : products) {
            ProductDto productDto = new ProductDto();
            BeanUtils.copyProperties(product, productDto);
            productDtos.add(productDto);
        }
        return productDtos;
    }
}
