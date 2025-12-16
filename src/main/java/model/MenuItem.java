/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Huynh Thai Duy Phuong - CE190603
 */
public class MenuItem {

    private int menuItemId;
    private Category category;
    private String itemName;
    private String imageUrl;
    private int price;
    private String description;
    private String status;

    private List<RecipeItem> items;
    

    public MenuItem(int menuItemId, Category category, String itemName, String imageUrl, int price, String description, String status) {
        this.menuItemId = menuItemId;
        this.category = category;
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
    
    public List<RecipeItem> getItems() {
        return items;
    }

    public void setItems(List<RecipeItem> items) {
        this.items = items;
    }

    public void addItem(RecipeItem item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
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
