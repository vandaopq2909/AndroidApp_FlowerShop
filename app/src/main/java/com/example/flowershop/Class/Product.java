package com.example.flowershop.Class;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Product implements Serializable {
    protected int id;
    protected String name;
    protected double price;
    protected String description;
    protected String imagePath;
    protected boolean bestProduct;
    protected int categoryId;
    protected boolean addToCart;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isBestProduct() {
        return bestProduct;
    }

    public void setBestProduct(boolean bestProduct) {
        this.bestProduct = bestProduct;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isAddToCart() {
        return addToCart;
    }

    public void setAddToCart(boolean addToCart) {
        this.addToCart = addToCart;
    }

    public Product() {
    }

    public Product(int id, String name, double price, String description, String imagePath, boolean bestProduct, int categoryId, boolean addToCart) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imagePath = imagePath;
        this.bestProduct = bestProduct;
        this.categoryId = categoryId;
        this.addToCart = addToCart;
    }
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("price", price);
        map.put("description", description);
        map.put("imagePath", imagePath);
        map.put("bestProduct", bestProduct);
        map.put("categoryId", categoryId);
        map.put("addToCart", addToCart);

        return map;
    }
}
