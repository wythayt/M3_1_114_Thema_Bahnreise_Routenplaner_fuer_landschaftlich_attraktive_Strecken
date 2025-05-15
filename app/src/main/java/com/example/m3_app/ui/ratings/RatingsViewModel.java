package com.example.m3_app.ui.ratings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RatingsViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public RatingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ratings fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}