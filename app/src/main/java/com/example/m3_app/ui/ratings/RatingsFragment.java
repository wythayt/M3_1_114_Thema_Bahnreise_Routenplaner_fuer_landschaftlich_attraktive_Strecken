package com.example.m3_app.ui.ratings;

import androidx.activity.OnBackPressedCallback;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.m3_app.R;
import com.example.m3_app.databinding.FragmentRatingsBinding;
import com.example.m3_app.ui.feedback_card.FeedbackCard;
import com.example.m3_app.ui.feedback_card.FeedbackCardAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RatingsFragment extends Fragment {

    private FragmentRatingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity()
                .getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        NavController navController = NavHostFragment.findNavController(requireParentFragment());
                        navController.navigateUp();
                    }
                });

        binding = FragmentRatingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List<FeedbackCard> cards = Arrays.asList(new FeedbackCard("Wow! Awesome trip!!",
                        new ArrayList<>(Arrays.asList(R.drawable.feedback3, R.drawable.feedback4))),
                new FeedbackCard("It was great!!!",
                        new ArrayList<>(Arrays.asList(R.drawable.feedback_1, R.drawable.feedback2))));
        binding.FeedbackRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.FeedbackRecyclerView.setAdapter(new FeedbackCardAdapter(cards));
        binding.backTextRatings.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(requireParentFragment());
            navController.navigateUp();
        });
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}