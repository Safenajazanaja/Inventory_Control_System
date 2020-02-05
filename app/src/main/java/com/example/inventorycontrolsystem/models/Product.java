package com.example.inventorycontrolsystem.models;

public class Product {
    private String productId;
    private String productName;
    private double price;
    private int qty;
    private String image;
    private String typeId;
    private String typeName;

    public Product() {

    }

    public Product(String productId, String productName, double price, int qty, String image, String typeId, String typeName) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.qty = qty;
        this.image = image;
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
