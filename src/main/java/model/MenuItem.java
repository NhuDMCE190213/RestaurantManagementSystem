/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Huynh Thai Duy Phuong - CE190603
 */
public class MenuItem {

    private int menuItemId;
    private Category category;
    private Recipe recipe;
    private String itemName;
    private String imageUrl;
    private int price;
    private String description;
    private String status;

    public MenuItem() {
    }

    public MenuItem(int menuItemId, Category category, Recipe recipe, String itemName, String imageUrl, int price, String description, String status) {
        this.menuItemId = menuItemId;
        this.category = category;
        this.recipe = recipe;
        this.itemName = itemName;
        this.imageUrl = imageUrl;
        this.price = price;
        this.description = description;
        this.status = status;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriceVND() {
        String str = "";

        String temp = getPrice()+ "";

        while (temp.length() > 0) {
            if (temp.length() > 3) {
                str = temp.substring(temp.length() - 3, temp.length()) + str;
                temp = temp.substring(0, temp.length() - 3);
            } else {
                str = temp + str;
                temp = "";
            }
            if (temp.length() > 0) {
                str = "." + str;
            }
        }

        str += " VND";

        return str;
    }
}
