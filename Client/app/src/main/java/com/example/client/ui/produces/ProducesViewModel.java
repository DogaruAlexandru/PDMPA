package com.example.client.ui.produces;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProducesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ProducesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is produces fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}