package com.example.client.ui.produces.produce;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.client.data.api.ContainerAPI;
import com.example.client.data.api.ProductAPI;
import com.example.client.data.model.Container;
import com.example.client.data.model.ProductFull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProduceAddViewModel extends ViewModel {
    private List<String> containerNames;
    private MutableLiveData<ArrayList<String>> containerNamesMutableLiveData;
    private long userId;

    private ProduceAddViewModel() {
    }

    public void setProduct() {
        getFromDB();

        containerNamesMutableLiveData = new MutableLiveData<>();
        containerNamesMutableLiveData.setValue((ArrayList<String>) containerNames);
    }

    private void getFromDB() {
        getContainers();
    }

    private void getContainers() {
        try {
            containerNames = ContainerAPI.getContainers(userId).stream()
                    .map(Container::name)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        containerNames = new ArrayList<>(Arrays.asList("Fridge", "Pantry", "Cellar"));
    }

    public void addProduct(ProductFull addValues) {
        try {
            ProductAPI.createProduct(new ProductAPI.UserIdProductFull(userId, addValues));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public MutableLiveData<ArrayList<String>> getContainerNamesMutableLiveData() {
        return containerNamesMutableLiveData;
    }
}