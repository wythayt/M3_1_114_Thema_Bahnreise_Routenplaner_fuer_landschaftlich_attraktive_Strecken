package com.example.m3_app.ui.activity_center;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.m3_app.R;
import com.example.m3_app.databinding.FragmentActivityCenterBinding;
import com.example.m3_app.ui.route_map.RouteMapAdapter;
import com.example.m3_app.ui.route_map.RouteMapCard;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ActivityCenterFragment extends Fragment {
    private FragmentActivityCenterBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentActivityCenterBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        List<RouteMapCard> cards = Arrays.asList(
                new RouteMapCard("Bavarian Bliss", R.drawable.sample_map, "2025-05-22"),
                new RouteMapCard("Through Forests", R.drawable.sample_map, "2025-05-12")
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

        LocalDate today = LocalDate.now();

        List<RouteMapCard> upcomingCards = new ArrayList<>();
        List<RouteMapCard> pastCards = new ArrayList<>();

        for (RouteMapCard card : cards) {
            try {
                LocalDate cardDate = LocalDate.parse(card.date, formatter);

                if (cardDate.isBefore(today)) {
                    pastCards.add(card);
                } else {
                    upcomingCards.add(card);
                }

            } catch (DateTimeParseException e) {
                pastCards.add(card);
            }
        }

        binding.RecyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.RecyclerViewUpcoming.setAdapter(new RouteMapAdapter(upcomingCards));

        binding.RecyclerViewPast.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.RecyclerViewPast.setAdapter(new RouteMapAdapter(pastCards));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();

        requireActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
    }
}
