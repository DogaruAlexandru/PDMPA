package com.example.client.ui.containers;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.client.data.model.Container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContainersViewModel extends ViewModel {

    private List<Container> data;
    private MutableLiveData<List<Container>> containers;

    public ContainersViewModel() {
        setData();
    }

    public void setData() {
        getFromDB();

        containers = new MutableLiveData<>();
        containers.setValue(data);
    }

    private void getFromDB() {
        //todo get from db or saved db on device

        data = new ArrayList<>(Arrays.asList(
                new Container(1L, "pantry"),
                new Container(2L, "cellar"),
                new Container(3L, "fridge")
        ));
    }

    public MutableLiveData<List<Container>> getContainers() {
        return containers;
    }
}