/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

/**
 *
 * @author PHAT
 */
public class Voucher {

    private int voucherId;
    private String voucherCode;
    private String voucherName;
    private String discountType;
    private int discountValue;
    private int quantity;
    private Date startDate;
    private Date endDate;
    private String status;

    public Voucher() {
    }

    public Voucher(int voucherId, String voucherCode, String voucherName, String discountType,
            int discountValue, int quantity, Date startDate, Date endDate, String status) {
        this.voucherId = voucherId;
        this.voucherCode = voucherCode;
        this.voucherName = voucherName;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.quantity = quantity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public int getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(int discountValue) {
        this.discountValue = discountValue;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentDiscount() {
        if (discountType == null) {
            return "N/A";
        }

        if (discountType.equalsIgnoreCase("Percent")) {
            return discountValue + "%";
        } else if (discountType.equalsIgnoreCase("Amount")) {
            return discountValue + " VND";
        } else {
            return discountValue + "";
        }
    }
}
