package com.example.client.ui.produce;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.client.data.model.ProductFull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ProduceEditViewModel extends ViewModel {
    private long productId;
    private ProductFull productFull;
    private ArrayList<String> containerNames;
    private MutableLiveData<ProductFull> productFullMutableLiveData;
    private MutableLiveData<ArrayList<String>> containerNamesMutableLiveData;

    private ProduceEditViewModel() {
        setProduct();
    }

    private void setProduct() {
        getFromDB();

        productFullMutableLiveData = new MutableLiveData<>();
        productFullMutableLiveData.setValue(productFull);

        containerNamesMutableLiveData = new MutableLiveData<>();
        containerNamesMutableLiveData.setValue(containerNames);
    }

    private void getFromDB() {
        //todo get from db or saved db on device

        getProductInfo();
        getContainers();
    }

    private void getProductInfo() {
        productFull = new ProductFull(
                5L,
                "Mock Product",
                "Pantry",
                new Date(),         // Mock expiration date
                10f,                // Mock quantity
                new Date(),         // Mock added date
                100.0f,             // Mock energy value
                5.0f,               // Mock fat value
                null,               // Mock carbohydrate value
                300.0f,             // Mock sodium
                150.0f,             // Mock calcium
                15.0f,              // Mock protein
                25.0f,              // Mock vitamin
                "Mock Vitamin Type",
                "Mock Allergens"
        );
    }

    private void getContainers(){
        containerNames = new ArrayList<>(Arrays.asList("Fridge", "Pantry", "Cellar"));
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

    public void updateProduct(ProductFull editedValues) {
        //todo look the difference between old and new product full and see differences and send them to be modified
    }

    public void deleteProduct() {
        //todo delete product with id product id
    }
}