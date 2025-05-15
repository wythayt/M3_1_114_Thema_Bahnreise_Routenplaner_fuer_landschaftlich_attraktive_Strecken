package com.example.m3_app.ui.ratings;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.m3_app.databinding.FragmentRatingsBinding;

public class RatingsFragment extends Fragment {

    private FragmentRatingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RatingsViewModel ratingsViewModel = new ViewModelProvider(this).get(RatingsViewModel.class);

        binding = FragmentRatingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}