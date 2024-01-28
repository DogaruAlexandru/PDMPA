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

public class ProduceEditViewModel extends ViewModel {
    private long userId;
    private long productId;
    private ProductFull productFull;
    private List<String> containerNames;
    private MutableLiveData<ProductFull> productFullMutableLiveData;
    private MutableLiveData<ArrayList<String>> containerNamesMutableLiveData;

    private ProduceEditViewModel() {
    }

    public void setData() {
        getFromDB();

        productFullMutableLiveData = new MutableLiveData<>();
        productFullMutableLiveData.setValue(productFull);

        containerNamesMutableLiveData = new MutableLiveData<>();
        containerNamesMutableLiveData.setValue((ArrayList<String>) containerNames);
    }

    private void getFromDB() {
        getProductInfo();
        getContainers();
    }

    private void getProductInfo() {
        try {
            productFull = ProductAPI.getProduct(productId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        productFull = new ProductFull(
//                5L,
//                "Mock Product",
//                "Pantry",
//                new Date(),         // Mock expiration date
//                10f,                // Mock quantity
//                new Date(),         // Mock added date
//                100.0f,             // Mock energy value
//                5.0f,               // Mock fat value
//                null,               // Mock carbohydrate value
//                300.0f,             // Mock sodium
//                150.0f,             // Mock calcium
//                15.0f,              // Mock protein
//                25.0f,              // Mock vitamin
//                "Mock Vitamin Type",
//                "Mock Allergens"
//        );
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

    public void updateProduct(ProductFull editedValues) {
        try {
            ProductAPI.updateProduct(editedValues);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteProduct() {
        try {
            ProductAPI.deleteProduct(productId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public MutableLiveData<ProductFull> getProductFullMutableLiveData() {
        return productFullMutableLiveData;
    }

    public MutableLiveData<ArrayList<String>> getContainerNamesMutableLiveData() {
        return containerNamesMutableLiveData;
    }
}