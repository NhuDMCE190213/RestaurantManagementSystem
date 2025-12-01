/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author PHAT
 */
public class RecipeItem {

    private int recipeItemId;
    private int recipeId;
    private int ingredientId;
    private double quantity;
    private String unit;
    private String note;
    private String status;
    
    private String ingredientName;

    public RecipeItem() {
    }

    public RecipeItem(int recipeItemId, int recipeId, int ingredientId, double quantity, String unit, String note, String status) {
        this.recipeItemId = recipeItemId;
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
        this.unit = unit;
        this.note = note;
        this.status = status;
    }

    public RecipeItem(int recipeItemId, int recipeId, int ingredientId, double quantity, String unit, String note, String status, String ingredientName) {
        this.recipeItemId = recipeItemId;
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
        this.unit = unit;
        this.note = note;
        this.status = status;
        this.ingredientName = ingredientName;
    }

    
    // getters & setters
    public int getRecipeItemId() {
        return recipeItemId;
    }

    public void setRecipeItemId(int recipeItemId) {
        this.recipeItemId = recipeItemId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }
}
