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
    public final static List<ProducesModel> EMPTY_DAY_LIST = new ArrayList<>(Collections.singletonList(
            new ProducesModel("Nothing expires today.", "Nice job, keep it up! \uD83D\uDE09")
    ));

    private final MutableLiveData<String> text;
    private MutableLiveData<HashMap<CalendarDay, List<ProducesModel>>> produces;
    private MutableLiveData<Set<CalendarDay>> producesDates;

    private HashMap<CalendarDay, List<ProducesModel>> data;

    public HomeViewModel() {
        setData();

        text = new MutableLiveData<>();
        text.setValue("Expiration dates");
    }

    public void setData(){
        getFromDB();

        produces = new MutableLiveData<>();
        produces.setValue(getDayExpiringProduces());

        producesDates = new MutableLiveData<>();
        producesDates.setValue(getDates());
    }

    private Set<CalendarDay> getDates() {
        //todo get dates from db
        return data.keySet();
    }

    private HashMap<CalendarDay, List<ProducesModel>> getDayExpiringProduces() {
        return data;
    }

    private void getFromDB() {
        //todo get from db or saved db on device

        HashMap<CalendarDay, List<ProducesModel>> map = new HashMap<>();

        map.put(CalendarDay.from(2024, 1, 27), new ArrayList<>(Arrays.asList(
                new ProducesModel("1", "zxc"),
                new ProducesModel("2", "asd"),
                new ProducesModel("3", "zxc"),
                new ProducesModel("4", "asd"))));

        map.put(CalendarDay.from(2024, 1, 1), new ArrayList<>(Arrays.asList(
                new ProducesModel("5", "zxc"),
                new ProducesModel("6", "asd"),
                new ProducesModel("7", "zxc"))));

        map.put(CalendarDay.from(2024, 1, 25), new ArrayList<>(Collections.singletonList(
                new ProducesModel("tas", "eee"))));

        data = map;
    }

    public LiveData<String> getText() {
        return text;
    }

    public LiveData<HashMap<CalendarDay, List<ProducesModel>>> getProduces() {
        return produces;
    }

    public MutableLiveData<Set<CalendarDay>> getProducesDates() {
        return producesDates;
    }
}