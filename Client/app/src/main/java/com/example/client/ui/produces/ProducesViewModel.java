package com.example.client.ui.produces;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.client.data.model.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ProducesViewModel extends ViewModel {

    private List<Product> data;
    private MutableLiveData<List<Product>> produces;

    public ProducesViewModel() {
        setData();
    }

    public void setData() {
        getFromDB();

        produces = new MutableLiveData<>();
        produces.setValue(data);
    }

    private void getFromDB() {
        //todo get from db or saved db on device

        data = new ArrayList<>(Arrays.asList(
                new Product(1L,"apple", createDate(2024, 1, 1), 1000f, "cellar"),
                new Product(2L,"pear", createDate(2024, 1, 1), 1000f, "cellar"),
                new Product(3L,"milk", createDate(2024, 1, 3), 1000f, "fridge"),
                new Product(4L,"yeast", createDate(2024, 1, 1), 23423f, "pantry"),
                new Product(5L,"mere", createDate(2024, 1, 1), 1000f, "fridge"),
                new Product(6L,"dsa", createDate(2024, 1, 5), 123f, "fridge"),
                new Product(7L,"mere", createDate(2024, 1, 3), 1000f, "cellar"),
                new Product(8L,"sada", createDate(2024, 1, 2), 1000f, "fridge"),
                new Product(9L,"das", createDate(2024, 1, 5), 2342f, "pantry"),
                new Product(10L,"asd", createDate(2024, 1, 1), 1000f, "cellar")
        ));
    }

    private static Date createDate(int year, int month, int day) {
        // Month value is 0-based in Calendar, so subtract 1 from the provided month
        Calendar calendar = new GregorianCalendar(year, month - 1, day);
        return calendar.getTime();
    }

    public String[] getFieldNames(){
        return new String[]{"Name", "Container", "Expiration Date", "Quantity"};
    }

    public MutableLiveData<List<Product>> getProduces() {
        return produces;
    }
}