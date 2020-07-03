package com.example.n8engine.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.List;

@SolrDocument(collection = "products")
public class Product {

    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String CATEGORY_NAME_FIELD = "category_name";
    public static final String BRAND_NAME_FIELD = "category_name";
    public static final String VENDOR_NAME_FIELD = "category_name";
    public static final String AVAILABLE_FIELD = "inStock";
    public static final String RESEALED_FIELD = "resealed";
    public static final String FEATURES_FIELD = "features";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String PRICE_FIELD = "price";
    public static final String RATING_FIELD = "rating";
    public static final String REVIEWS_FIELD = "reviews";
    public static final String POPULARITY_FIELD = "popularity";
    public static final String TAGS_FIELD = "tags";

    @Id
    @Indexed(name = ID_FIELD)
    private String Id;

    @Indexed(name = NAME_FIELD)
    private String name;

    @Indexed(name = CATEGORY_NAME_FIELD)
    private String categoryName;

    @Indexed(name = BRAND_NAME_FIELD)
    private String brandName;

    @Indexed(name = VENDOR_NAME_FIELD)
    private String vendorName;

    @Indexed(name = AVAILABLE_FIELD)
    private boolean available;

    @Indexed(name = RESEALED_FIELD)
    private boolean resealed;

    @Indexed(name = FEATURES_FIELD)
    private List<String> features;

    @Indexed(name = DESCRIPTION_FIELD)
    private String description;

    @Indexed(name = PRICE_FIELD)
    private float price;

    @Indexed(name = RATING_FIELD)
    private float rating;

    @Indexed(name = REVIEWS_FIELD)
    private int reviews;

    @Indexed(name = POPULARITY_FIELD)
    private float popularity;

    @Indexed(name = TAGS_FIELD)
    private List<String> tags;


    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isResealed() {
        return resealed;
    }

    public void setResealed(boolean resealed) {
        this.resealed = resealed;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                '}';
    }
}
