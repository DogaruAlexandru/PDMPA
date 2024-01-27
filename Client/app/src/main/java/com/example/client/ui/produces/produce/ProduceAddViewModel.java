package com.example.client.ui.produces.produce;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.client.data.model.ProductFull;

import java.util.ArrayList;
import java.util.Arrays;

public class ProduceAddViewModel extends ViewModel {
    private ArrayList<String> containerNames;
    private MutableLiveData<ArrayList<String>> containerNamesMutableLiveData;

    private ProduceAddViewModel() {
        setProduct();
    }

    private void setProduct() {
        getFromDB();

        containerNamesMutableLiveData = new MutableLiveData<>();
        containerNamesMutableLiveData.setValue(containerNames);
    }

    private void getFromDB() {
        //todo get from db or saved db on device

        getContainers();
    }

    private void getContainers(){
        containerNames = new ArrayList<>(Arrays.asList("Fridge", "Pantry", "Cellar"));
    }

    public MutableLiveData<ArrayList<String>> getContainerNamesMutableLiveData() {
        return containerNamesMutableLiveData;
    }

    public void addProduct(ProductFull addValues) {
        //todo write the product in db
    }
}