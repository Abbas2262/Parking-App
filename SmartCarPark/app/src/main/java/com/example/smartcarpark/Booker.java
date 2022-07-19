package com.example.smartcarpark;

public class Booker {

    public String customerId;
    public String customerName;
    public String customerGenre;

    public Booker(){

    }

    public Booker(String customerId, String customerName, String customerGenre) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerGenre = customerGenre;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerGenre() {
        return customerGenre;
    }
}
