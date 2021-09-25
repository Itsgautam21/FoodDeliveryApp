package com.example.seller.Models;

public class CategoryModel {

    String categoryImage;
    String categoryName;

    public CategoryModel() {

    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "categoryImage=" + categoryImage +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
