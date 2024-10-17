package com.example.Sachpee.Model;

public class ProductDetail {
    private int codeCategory;
    private String nameProduct;
    private String imgProduct;
    private String author;
    private String description;

    // Constructor
    public ProductDetail(int codeCategory, String nameProduct, String imgProduct, String author, String description) {
        this.codeCategory = codeCategory;
        this.nameProduct = nameProduct;
        this.imgProduct = imgProduct;
        this.author = author;
        this.description = description;
    }

    public int getCodeCategory() {
        return codeCategory;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public String getImgProduct() {
        return imgProduct;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public void setCodeCategory(int codeCategory) {
        this.codeCategory = codeCategory;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public void setImgProduct(String imgProduct) {
        this.imgProduct = imgProduct;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
