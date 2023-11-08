package com.example.client.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class HomeViewModel extends ViewModel {
    public final static List<ProductModel> EMPTY_DAY_LIST = new ArrayList<>(Collections.singletonList(
            new ProductModel("Nothing expires today.", "Nice job, keep it up! \uD83D\uDE09")
    ));

    private final MutableLiveData<String> text;
    private final MutableLiveData<HashMap<CalendarDay, List<ProductModel>>> produces;
    private final MutableLiveData<Set<CalendarDay>> producesDates;

    private HashMap<CalendarDay, List<ProductModel>> data;

    public HomeViewModel() {
        getFromDB();

        text = new MutableLiveData<>();
        text.setValue("Expiration dates");

        produces = new MutableLiveData<>();
        produces.setValue(getDayExpiringProduces());

        producesDates = new MutableLiveData<>();
        producesDates.setValue(getDates());
    }

    private Set<CalendarDay> getDates() {
        //todo get dates from db
        return data.keySet();
    }

    private HashMap<CalendarDay, List<ProductModel>> getDayExpiringProduces() {
        return data;
    }

    private void getFromDB() {
        //todo get from db or saved db on device
        HashMap<CalendarDay, List<ProductModel>> map = new HashMap<>();

        map.put(CalendarDay.from(2023, 10, 27), new ArrayList<>(Arrays.asList(
                new ProductModel("1", "zxc"),
                new ProductModel("2", "asd"),
                new ProductModel("3", "zxc"),
                new ProductModel("4", "asd"))));

        map.put(CalendarDay.from(2023, 11, 1), new ArrayList<>(Arrays.asList(
                new ProductModel("5", "zxc"),
                new ProductModel("6", "asd"),
                new ProductModel("7", "zxc"))));

        map.put(CalendarDay.from(2023, 10, 25), new ArrayList<>(Collections.singletonList(
                new ProductModel("tas", "eee"))));

        data = map;
    }

    public LiveData<String> getText() {
        return text;
    }

    public LiveData<HashMap<CalendarDay, List<ProductModel>>> getProduces() {
        return produces;
    }

    public MutableLiveData<Set<CalendarDay>> getProducesDates() {
        return producesDates;
    }
}