package com.example.m3_app.ui.activity_center;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ActivityCenterViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public ActivityCenterViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is activity center fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
