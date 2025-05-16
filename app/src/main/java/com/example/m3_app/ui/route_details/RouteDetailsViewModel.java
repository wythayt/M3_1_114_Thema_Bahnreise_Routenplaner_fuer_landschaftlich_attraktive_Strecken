package com.example.m3_app.ui.route_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RouteDetailsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RouteDetailsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is suggestions fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
