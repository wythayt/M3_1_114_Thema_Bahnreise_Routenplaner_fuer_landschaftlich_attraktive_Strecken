package com.example.m3_app.ui.activity_center;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.m3_app.databinding.FragmentActivityCenterBinding;

public class ActivityCenterFragment extends Fragment {
    private FragmentActivityCenterBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ActivityCenterViewModel homeViewModel =
                new ViewModelProvider(this).get(ActivityCenterViewModel.class);

        binding = FragmentActivityCenterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textActivityCenter;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
