/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author Dai Minh Nhu - CE190213
 */
public class Order {

    private int orderId;
    private Reservation reservation;
    private Employee emp;
    private Voucher voucher;
    private Date orderDate;
    private Time orderTime;
    private String paymentMethod;
    private String status;
    
    public Order(int orderId, Reservation reservation, Employee emp, Voucher voucher, Date orderDate, Time orderTime, String paymentMethod, String status) {
        this.orderId = orderId;
        this.reservation = reservation;
        this.emp = emp;
        this.voucher = voucher;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Employee getEmp() {
        return emp;
    }

    public void setEmp(Employee emp) {
        this.emp = emp;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Time getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Time orderTime) {
        this.orderTime = orderTime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
