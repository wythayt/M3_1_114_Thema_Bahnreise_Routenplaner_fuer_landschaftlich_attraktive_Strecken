package com.example.m3_app.ui.activity_center;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.m3_app.R;
import com.example.m3_app.backend.RouteConfig;
import com.example.m3_app.databinding.FragmentActivityCenterBinding;
import com.example.m3_app.ui.map_specified.MapSpecifiedViewModel;
import com.example.m3_app.ui.route_map.RouteMapAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        requireActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        MapSpecifiedViewModel mapVm = new ViewModelProvider(requireActivity(),
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()))
                .get(MapSpecifiedViewModel.class);

        mapVm.getAllRoutes().observe(getViewLifecycleOwner(), allRoutes -> {
            SharedPreferences prefs = requireContext()
                    .getSharedPreferences("BookedTrips", Context.MODE_PRIVATE);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
            LocalDate today = LocalDate.now();

            List<RouteConfig.Route> upcoming = new ArrayList<>();
            List<LocalDate> upcomingDates = new ArrayList<>();
            List<RouteConfig.Route> past = new ArrayList<>();
            List<LocalDate> pastDates= new ArrayList<>();

            for (RouteConfig.Route route : allRoutes) {
                String dateStr = prefs.getString(route.id, null);
                if (dateStr != null) {
                    try {
                        LocalDate bookedDate = LocalDate.parse(dateStr, formatter);
                        if (bookedDate.isBefore(today)) {
                            past.add(route);
                            pastDates.add(bookedDate);
                        } else {
                            upcoming.add(route);
                            upcomingDates.add(bookedDate);
                        }
                    } catch (DateTimeParseException e) {
                        past.add(route);
                    }
                }
            }

            binding.RecyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.RecyclerViewUpcoming.setAdapter(new RouteMapAdapter(upcoming, true, this::navigateToDetails, upcomingDates));

            binding.RecyclerViewPast.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.RecyclerViewPast.setAdapter(new RouteMapAdapter(past, false, this::navigateToDetails, pastDates));
        });
    }

    private void navigateToDetails(RouteConfig.Route route) {
        Bundle bundle = new Bundle();
        bundle.putString("routeId", route.id);
        NavHostFragment.findNavController(this).navigate(R.id.tripDetailsFragment, bundle);
    }

}
