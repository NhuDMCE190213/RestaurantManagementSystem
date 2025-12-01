/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

/**
 *
 * @author Administrator
 */
public class Customer {

    private int customerId;
    private String customerAccount;
    private String password;
    private String customerName;
    private String gender;
    private String phoneNumber;
    private String email;
    private String address;
    private Date dob;
    private String status;

    public Customer(int customerId, String customerAccount, String password, String customerName, String gender, String phoneNumber, String email, String address, Date dob, String status) {
        this.customerId = customerId;
        this.customerAccount = customerAccount;
        this.password = password;
        this.customerName = customerName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.dob = dob;
        this.status = status;
    }
public Customer(int customerId, String customerAccount, String password, String customerName, String gender, String phoneNumber, String email, String address, Date dob) {
        this.customerId = customerId;
        this.customerAccount = customerAccount;
        this.password = password;
        this.customerName = customerName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.dob = dob;
          }
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(String customerAccount) {
        this.customerAccount = customerAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
