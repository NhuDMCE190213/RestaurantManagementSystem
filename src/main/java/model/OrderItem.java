/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Dai Minh Nhu - CE190213
 */
public class OrderItem {

    private int orderItemId;
    private Order order;
    private MenuItem menuItem;
    private int unitPrice;
    private int quantity;

    public OrderItem(int orderItemId, Order order, MenuItem menuItem, int unitPrice, int quantity) {
        this.orderItemId = orderItemId;
        this.order = order;
        this.menuItem = menuItem;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPriceVND() {
        String str = "";

        String temp = getUnitPrice() + "";

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
