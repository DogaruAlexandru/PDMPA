package com.example.client.data.model;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Date;

public class Product {
    private final String name, container;
    private final Date expirationDate;
    private final int quantity;

    public Product(String name, Date expirationDate, int quantity, String container) {
        this.name = name;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
        this.container = container;
    }

    public String getName() {
        return name;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getContainer() {
        return container;
    }
}
