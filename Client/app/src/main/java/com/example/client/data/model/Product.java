package com.example.client.data.model;

import java.util.Date;

public record Product(Long id, String name, Date expirationDate, Float quantity, String container) {
}
