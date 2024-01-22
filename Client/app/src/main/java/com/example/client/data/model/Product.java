package com.example.client.data.model;

import java.util.Date;

public record Product(int id, String name, Date expirationDate, int quantity, String container) {
}
