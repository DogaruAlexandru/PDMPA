package com.example.client.data.model;

import java.io.Serializable;
import java.util.Date;

public record ProductFull(Long productId,
                          String productName,
                          String productContainer,
                          Date expirationDate,
                          Float quantity,
                          Date addedDate,
                          Float energyValue,
                          Float fatValue,
                          Float carbohydrateValue,
                          Float sodium,
                          Float calcium,
                          Float protein,
                          Float vitamin,
                          String vitaminType,
                          String allergens) {
}