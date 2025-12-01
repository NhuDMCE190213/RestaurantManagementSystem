/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package model;

import java.time.LocalDate;

/**
 *
 * @author TruongBinhTrong
 */
public class Ingredient {

    private int ingredientId;
    private String ingredientName;
    private int typeId;
    private String typeName;
    private String unit;
    private double totalQuantity;
    private String status;
    private LocalDate expirationDate;
    private boolean expired;
    private boolean expiringSoon;

    public Ingredient() {
    }

    public Ingredient(int ingredientId, String ingredientName, String unit, int typeId, String typeName, String status) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.unit = unit;
        this.typeId = typeId;
        this.typeName = typeName;
        this.status = status;
    }

    public Ingredient(int ingredientId, String ingredientName, String typeName, String unit, double totalQuantity, String status) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.typeName = typeName;
        this.unit = unit;
        this.totalQuantity = totalQuantity;
        this.status = status;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isExpiringSoon() {
        return expiringSoon;
    }

    public void setExpiringSoon(boolean expiringSoon) {
        this.expiringSoon = expiringSoon;
    }
}
