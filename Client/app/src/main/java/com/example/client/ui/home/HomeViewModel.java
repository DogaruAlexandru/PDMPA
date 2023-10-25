package com.example.client.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class HomeViewModel extends ViewModel {

    public final static List<ProducesModel> EMPTY_DAY_LIST = new ArrayList<>();
    private final static ProducesModel EMPTY_DAY = new ProducesModel(
            "Nothing expires today.", "Nice job, keep it up! \uD83D\uDE09");
    private final MutableLiveData<String> text;
    private final MutableLiveData<HashMap<CalendarDay, List<ProducesModel>>> items;
    private final MutableLiveData<HashSet<CalendarDay>> datesWithEvents;

    public HomeViewModel() {
        EMPTY_DAY_LIST.add(EMPTY_DAY);

        text = new MutableLiveData<>();
        text.setValue("Expiration dates");

        items = new MutableLiveData<>();
        items.setValue(getDayExpiringProduces());

        datesWithEvents = new MutableLiveData<>();
        datesWithEvents.setValue(getDates());
    }

    private HashSet<CalendarDay> getDates() {
        //todo get dates from db

        HashSet<CalendarDay> eventDates = new HashSet<>();
        eventDates.add(CalendarDay.from(2023, 10, 27));
        eventDates.add(CalendarDay.from(2023, 11, 1));

        return eventDates;
    }

    private HashMap<CalendarDay, List<ProducesModel>> getDayExpiringProduces() {

        return getFromDB();
    }

    private HashMap<CalendarDay, List<ProducesModel>> getFromDB() {
        //todo get from db or saved db on device

        HashMap<CalendarDay, List<ProducesModel>> map = new HashMap<>();

        map.put(CalendarDay.from(2023, 10, 27), new ArrayList<>());
        Objects.requireNonNull(map.get(CalendarDay.from(2023, 10, 27))).
                add(new ProducesModel("1", "zxc"));
        Objects.requireNonNull(map.get(CalendarDay.from(2023, 10, 27))).
                add(new ProducesModel("2", "asd"));
        Objects.requireNonNull(map.get(CalendarDay.from(2023, 10, 27))).
                add(new ProducesModel("3", "zxc"));
        Objects.requireNonNull(map.get(CalendarDay.from(2023, 10, 27))).
                add(new ProducesModel("4", "asd"));

        map.put(CalendarDay.from(2023, 11, 1), new ArrayList<>());
        Objects.requireNonNull(map.get(CalendarDay.from(2023, 11, 1))).
                add(new ProducesModel("5", "zxc"));
        Objects.requireNonNull(map.get(CalendarDay.from(2023, 11, 1))).
                add(new ProducesModel("6", "asd"));
        Objects.requireNonNull(map.get(CalendarDay.from(2023, 11, 1))).
                add(new ProducesModel("7", "asd"));
        Objects.requireNonNull(map.get(CalendarDay.from(2023, 11, 1))).
                add(new ProducesModel("8", "zxc"));

        map.put(CalendarDay.from(2023, 10, 25), new ArrayList<>());
        Objects.requireNonNull(map.get(CalendarDay.from(2023, 10, 25))).
                add(new ProducesModel("tas", "eee"));

        return map;
    }

    public LiveData<String> getText() {
        return text;
    }

    public LiveData<HashMap<CalendarDay, List<ProducesModel>>> getItems() {
        return items;
    }

    public MutableLiveData<HashSet<CalendarDay>> getDatesWithEvents() {
        return datesWithEvents;
    }
}