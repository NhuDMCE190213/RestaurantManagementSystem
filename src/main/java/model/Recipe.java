/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PHAT
 */
public class Recipe {

    private int recipeId;
    private int recipeItemId;   // recipe_item_id (PK)
    private int menuItemId;
    private String menuItemName;
    private int ingredientId;
    private String ingredientName;
    private double quantity;
    private String unit;
    private String note;
    private String status;

    public Recipe() {
    }

    // Full constructor (including display names)
    public Recipe(int recipeItemId, int menuItemId, String menuItemName, int ingredientId, String ingredientName, double quantity, String unit, String note, String status) {
        this.recipeItemId = recipeItemId;
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unit = unit;
        this.note = note;
        this.status = status;
    }

    // Constructor without display names
    public Recipe(int recipeItemId, int menuItemId, int ingredientId, double quantity, String unit, String note, String status) {
        this.recipeItemId = recipeItemId;
        this.menuItemId = menuItemId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
        this.unit = unit;
        this.note = note;
        this.status = status;
    }

    // getters / setters
    public int getRecipeItemId() {
        return recipeItemId;
    }

    public void setRecipeItemId(int recipeItemId) {
        this.recipeItemId = recipeItemId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
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

}
