package com.example.client.ui.containers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContainersViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ContainersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is containers fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}