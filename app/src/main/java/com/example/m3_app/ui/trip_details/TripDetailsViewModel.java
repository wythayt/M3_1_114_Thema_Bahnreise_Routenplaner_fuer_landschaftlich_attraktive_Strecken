package com.example.m3_app.ui.trip_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TripDetailsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TripDetailsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is suggestions fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
