/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package model;

/**
 *
 * @author TruongBinhTrong
 */
public class Ingredient {

    private int ingredientId;
    private String ingredientName;
    private String unit;
    private Type type;
    private String status;

    public Ingredient(int ingredientId, String ingredientName, String unit, Type type, String status) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;

        this.unit = unit;
        this.type = type;
        this.status = status;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
