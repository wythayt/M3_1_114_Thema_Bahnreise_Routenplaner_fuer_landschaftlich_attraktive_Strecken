package com.example.m3_app.ui.search_test;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.m3_app.databinding.FragmentSearchTestBinding;
import com.example.m3_app.R;

public class SearchFragmentTest extends Fragment {
    private FragmentSearchTestBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchTestBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.radioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_specific) {
                binding.editTextTo2.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.radio_inspire) {
                binding.editTextTo2.setVisibility(View.GONE);
            }
        });
        binding.searchButton.setBackgroundColor(Color.parseColor("#E63946"));
        binding.searchButton.setOnClickListener(v -> {
            String from = binding.editTextFrom1.getText().toString();
            String to = binding.editTextTo2.getText().toString();

            Toast.makeText(getContext(), "From: " + from + ", To: " + to, Toast.LENGTH_SHORT).show();

            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.mapSpecifiedFragment);

            int checkedId = binding.radioGroup1.getCheckedRadioButtonId();

            if (checkedId == R.id.radio_specific) {
                navController.navigate(R.id.mapSpecifiedFragment);
            } else {
                navController.navigate(R.id.mapNotSpecifiedFragment);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
