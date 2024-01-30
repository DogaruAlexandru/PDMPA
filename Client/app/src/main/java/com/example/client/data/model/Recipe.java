package com.example.client.data.model;

import java.util.List;

public record Recipe(Long id, String name, List<String> ingredients, String description) {
}
