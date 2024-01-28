package com.example.client.ui.containers.container;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

import com.example.client.data.api.ContainerAPI;
import com.example.client.data.api.ProductAPI;
import com.example.client.data.model.Container;

import java.io.IOException;

public class ContainerAddViewModel extends ViewModel {
    private long userId;
    public void addContainer(Container addValues) {
        try {
            ContainerAPI.createContainer(new ContainerAPI.UserIdContainer(userId, addValues));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}