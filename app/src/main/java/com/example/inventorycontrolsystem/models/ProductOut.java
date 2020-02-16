package com.example.inventorycontrolsystem.models;

public class ProductOut {
    private String productName;
    private String image;
    private String typeName;
    private String productInNO;
    private String productId;
    private String dateIn;
    private int qty;
    private Double price;

    public ProductOut() {

    }

    public ProductOut(String productName, String image, String typeName, String productInNO, String productId, String dateIn, int qty, Double price) {
        this.productName = productName;
        this.image = image;
        this.typeName = typeName;
        this.productInNO = productInNO;
        this.productId = productId;
        this.dateIn = dateIn;
        this.qty = qty;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getProductInNO() {
        return productInNO;
    }

    public void setProductInNO(String productInNO) {
        this.productInNO = productInNO;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDateIn() {
        return dateIn;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
