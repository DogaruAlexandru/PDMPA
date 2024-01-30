package com.example.client.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public record LoggedInUser(Long userId, String email) {

}