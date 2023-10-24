package com.example.client.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final static ExpirationTodayModel EMPTY_DAY_LIST_OBJ = new ExpirationTodayModel(
            "Nothing expires today.", "Nice job, keep it up! \uD83D\uDE09");
    private final MutableLiveData<String> mText;
    private final MutableLiveData<List<ExpirationTodayModel>> items;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Expiration dates");

        items = new MutableLiveData<>();
        items.setValue(getTodayExpiringProduces());
    }

    private List<ExpirationTodayModel> getTodayExpiringProduces() {

        List<ExpirationTodayModel> list = getFromDB();

        if (list.size() == 0) {
            list.add(EMPTY_DAY_LIST_OBJ);
        }

        return list;
    }

    private List<ExpirationTodayModel> getFromDB() {
        //todo get from db or saved db on device

        List<ExpirationTodayModel> list = new ArrayList<>();
        list.add(new ExpirationTodayModel("1", "asd"));
        list.add(new ExpirationTodayModel("2", "erfr"));
        list.add(new ExpirationTodayModel("3", "erfr"));
        list.add(new ExpirationTodayModel("4", "asd"));
        list.add(new ExpirationTodayModel("5", "asd"));
        list.add(new ExpirationTodayModel("Ani", "asd"));
        list.add(new ExpirationTodayModel("2143", "erfr"));
        list.add(new ExpirationTodayModel("3cwfbeg43", "erfr"));
        list.add(new ExpirationTodayModel("tg3c2f", "asd"));
        list.add(new ExpirationTodayModel("3434v3v", "asd"));

        return list;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<ExpirationTodayModel>> getItems() {
        return items;
    }
}