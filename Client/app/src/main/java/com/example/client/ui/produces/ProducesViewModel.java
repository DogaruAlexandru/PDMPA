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
    private final MutableLiveData<List<Product>> produces;

    public ProducesViewModel() {
        getFromDB();

        produces = new MutableLiveData<>();
        produces.setValue(data);
    }

    private void getFromDB() {
        //todo get from db or saved db on device

        data = new ArrayList<>(Arrays.asList(
                new Product(1,"apple", createDate(2024, 1, 1), 1000, "cellar"),
                new Product(2,"pear", createDate(2024, 1, 1), 1000, "cellar"),
                new Product(3,"milk", createDate(2024, 1, 3), 1000, "fridge"),
                new Product(4,"yeast", createDate(2024, 1, 1), 23423, "pantry"),
                new Product(5,"mere", createDate(2024, 1, 1), 1000, "fridge"),
                new Product(6,"dsa", createDate(2024, 1, 5), 123, "fridge"),
                new Product(7,"mere", createDate(2024, 1, 3), 1000, "cellar"),
                new Product(8,"sada", createDate(2024, 1, 2), 1000, "fridge"),
                new Product(9,"das", createDate(2024, 1, 5), 2342, "pantry"),
                new Product(10,"asd", createDate(2024, 1, 1), 1000, "cellar")
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