package com.example.m3_app.ui.map_specified;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapSpecifiedViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public MapSpecifiedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is map specified fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

