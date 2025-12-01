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
    private int menuItemId;
    private String menuItemName;
    private String status;
    private String recipeName;
    private List<RecipeItem> items;

    public Recipe() {
        items = new ArrayList<>();
    }

//    public Recipe(int recipeId, int menuItemId, String status) {
//        this.recipeId = recipeId;
//        this.menuItemId = menuItemId;
//        this.status = status;
//
//    }
//
//    public Recipe(int recipeId, int menuItemId, String menuItemName, String status) {
//        this.recipeId = recipeId;
//        this.menuItemId = menuItemId;
//        this.menuItemName = menuItemName;
//        this.status = status;
//        this.items = new ArrayList<>();
//    }

    public Recipe(int recipeId, String recipeName, String status) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.status = status;
        this.items = new ArrayList<>();
    }
    

    // getters & setters
    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RecipeItem> getItems() {
        return items;
    }

    public void setItems(List<RecipeItem> items) {
        this.items = items;
    }

    public void addItem(RecipeItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

}
