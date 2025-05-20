package com.example.m3_app.ui.filter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FilterViewModel extends ViewModel {
    private final MutableLiveData<Set<String>> selectedChips = new MutableLiveData<>(new HashSet<>());
    public LiveData<Set<String>> getSelectedChips() {
        return selectedChips;
    }

    public void updateChip(String key, boolean isChecked) {
        Set<String> mySet = new HashSet<>(Objects.requireNonNull(selectedChips.getValue()));

        if (isChecked) {
            mySet.add(key);
        } else {
            mySet.remove(key);
        }

        selectedChips.setValue(Collections.unmodifiableSet(mySet));
    }
}
