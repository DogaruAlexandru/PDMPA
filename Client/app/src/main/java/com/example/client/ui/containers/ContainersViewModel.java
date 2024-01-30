package com.example.client.ui.containers;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.client.data.api.ContainerAPI;
import com.example.client.data.api.ProductAPI;
import com.example.client.data.model.Container;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContainersViewModel extends ViewModel {

    private List<Container> data;
    private MutableLiveData<List<Container>> containers;

    private long userId;

    public ContainersViewModel() {
    }

    public void setData() {
        getFromDB();

        containers = new MutableLiveData<>();
        containers.setValue(data);
    }

    private void getFromDB() {
        try {
            data = ContainerAPI.getContainers(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        data = new ArrayList<>(Arrays.asList(
//                new Container(1L, "pantry"),
//                new Container(2L, "cellar"),
//                new Container(3L, "fridge")
//        ));
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public MutableLiveData<List<Container>> getContainers() {
        return containers;
    }
}