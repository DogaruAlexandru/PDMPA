package com.example.client.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.client.data.model.Product;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class HomeViewModel extends ViewModel {
    public final static List<Product> EMPTY_DAY_LIST = new ArrayList<>(Collections.singletonList(
            new Product(-1L, "Nothing expires today.", null, -1f,
                    "Nice job, keep it up! \uD83D\uDE09")
    ));

    private final MutableLiveData<String> text;
    private MutableLiveData<Map<CalendarDay, List<Product>>> produces;
    private MutableLiveData<Set<CalendarDay>> producesDates;

    private List<Product> data;

    public HomeViewModel() {
        setData();

        text = new MutableLiveData<>();
        text.setValue("Expiration dates");
    }

    public void setData() {
        getFromDB();

        produces = new MutableLiveData<>();
        produces.setValue(createProducesByDate());

        producesDates = new MutableLiveData<>();
        producesDates.setValue(Objects.requireNonNull(produces.getValue()).keySet());
    }

    @NonNull
    private Map<CalendarDay, List<Product>> createProducesByDate() {
        Map<Date, List<Product>> map = data.stream().collect(Collectors.groupingBy(Product::expirationDate));
        return map.entrySet().stream().collect(Collectors.toMap(
                entry -> convertDateToCalendarDay(entry.getKey()),
                Map.Entry::getValue
        ));
    }

    private static CalendarDay convertDateToCalendarDay(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return CalendarDay.from(year, month, day);
    }

    private void getFromDB() {
        //todo get from db or saved db on device

        data = new ArrayList<>(Arrays.asList(
                new Product(1L, "apple", createDate(2024, 1, 25), 1000f, "cellar"),
                new Product(2L, "pear", createDate(2024, 1, 1), 1000f, "cellar"),
                new Product(3L, "milk", createDate(2024, 1, 3), 1000f, "fridge"),
                new Product(4L, "yeast", createDate(2024, 1, 25), 23423f, "pantry"),
                new Product(5L, "mere", createDate(2024, 1, 27), 1000f, "fridge"),
                new Product(6L, "dsa", createDate(2024, 1, 25), 123f, "fridge"),
                new Product(7L, "mere", createDate(2024, 1, 3), 1000f, "cellar"),
                new Product(8L, "sada", createDate(2024, 1, 22), 1000f, "fridge"),
                new Product(9L, "das", createDate(2024, 1, 27), 2342f, "pantry"),
                new Product(10L, "asd", createDate(2024, 1, 1), 1000f, "cellar")
        ));
    }

    private static Date createDate(int year, int month, int day) {
        // Month value is 0-based in Calendar, so subtract 1 from the provided month
        Calendar calendar = new GregorianCalendar(year, month - 1, day);
        return calendar.getTime();
    }

    public LiveData<String> getText() {
        return text;
    }

    public LiveData<Map<CalendarDay, List<Product>>> getProduces() {
        return produces;
    }

    public MutableLiveData<Set<CalendarDay>> getProducesDates() {
        return producesDates;
    }
}