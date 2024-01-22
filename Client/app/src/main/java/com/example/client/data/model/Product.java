package com.example.client.data.model;

import java.util.Date;

public record Product(long id, String name, Date expirationDate, float quantity, String container) {
}
