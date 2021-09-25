package com.example.seller.Models;

import org.jetbrains.annotations.NotNull;

public class MenuModel {

    String foodImage;
    String foodName;
    String foodDescription;
    String foodPrice;
    Boolean isVeg;
    boolean isAvailable;

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public MenuModel() {

    }

    @NotNull
    @Override
    public String toString() {
        return "MenuModel{" +
                "foodImage='" + foodImage + '\'' +
                ", foodName='" + foodName + '\'' +
                ", foodDescription='" + foodDescription + '\'' +
                ", foodPrice='" + foodPrice + '\'' +
                ", isVeg=" + isVeg +
                ", isAvailable=" + isAvailable +
                '}';
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public Boolean getVeg() {
        return isVeg;
    }

    public void setVeg(Boolean veg) {
        isVeg = veg;
    }

}
