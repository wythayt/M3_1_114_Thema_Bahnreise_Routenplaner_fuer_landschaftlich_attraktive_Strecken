package com.example.m3_app.ui.map_specified;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.m3_app.R;
import com.example.m3_app.backend.RouteConfig;
import com.example.m3_app.backend.utility.RouteFilterUtil;
import com.example.m3_app.databinding.FragmentMapSpecifiedBinding;
import com.example.m3_app.ui.filter.FilterViewModel;
import com.example.m3_app.ui.filter.FiltersBottomSheet;
import com.example.m3_app.ui.route_card.RouteCard;
import com.example.m3_app.ui.route_card.RouteCardAdapter;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.example.m3_app.ui.map_specified.MapSpecifiedFragmentArgs;
import com.example.m3_app.ui.map_specified.MapSpecifiedFragmentDirections;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapSpecifiedFragment extends Fragment {
    private FragmentMapSpecifiedBinding binding;

    public MapSpecifiedFragment() {
        super(R.layout.fragment_map_specified);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View nav = requireActivity().findViewById(R.id.nav_view);
        if (nav != null) nav.setVisibility(View.GONE);

        binding = FragmentMapSpecifiedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        showLoading();
        MapSpecifiedFragmentArgs args = MapSpecifiedFragmentArgs.fromBundle(requireArguments());
        String startLocation = args.getFrom();
        String endLocation = args.getTo();

        if (startLocation == null || startLocation.isEmpty()) {
            startLocation = "Start Location";
        }
        if (endLocation == null || endLocation.isEmpty()) {
            endLocation = "End Destination";
        }

        binding.textView2.setText(startLocation);
        binding.textView3.setText(endLocation);

        binding.button3.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.searchFragmentTest);
        });

        String finalStartLocation = startLocation;
        String finalEndLocation = endLocation;

        binding.button5.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            NavDirections action = MapSpecifiedFragmentDirections
                    .actionMapSpecifiedFragmentToSearchResultsFragment(finalStartLocation, finalEndLocation);
            navController.navigate(action);
        });

        binding.button.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            NavDirections action = MapSpecifiedFragmentDirections
                    .actionMapSpecifiedFragmentToSearchResultsFragment(finalStartLocation, finalEndLocation);
            navController.navigate(action);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

        AppCompatActivity activity = (AppCompatActivity) requireActivity();

        View navBar = activity.findViewById(R.id.nav_view);
        assert navBar != null;
        navBar.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();

        ViewGroup root = (ViewGroup) requireActivity().findViewById(android.R.id.content);
        View navBar = root.findViewById(R.id.nav_view);

        assert navBar != null;
        navBar.setVisibility(View.GONE);

        requireActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        PhotoView photoView = binding.imageView7;
        FloatingActionButton fabZoomIn = binding.fabZoomIn;
        FloatingActionButton fabZoomOut = binding.fabZoomOut;

        photoView.setMinimumScale(0.5f);
        photoView.setMediumScale(2.0f);
        photoView.setMaximumScale(6.0f);

        fabZoomIn.setOnClickListener(v -> {
            float scale = photoView.getScale();
            photoView.setScale(scale * 0.8f, true);
        });

        fabZoomOut.setOnClickListener(v -> {
            float scale = photoView.getScale();
            photoView.setScale(scale * 1.25f, true);

        });

        MaterialButton filtersBtn = binding.button4;
        ColorStateList highlightTint = ColorStateList.valueOf(requireContext().getColor(R.color.forest_green));

        filtersBtn.setOnClickListener(v -> {
            new FiltersBottomSheet()
                    .show(getParentFragmentManager(), "filters");
            filtersBtn.setBackgroundTintList(highlightTint);
        });

        RouteCardAdapter adapter = new RouteCardAdapter();

        adapter.setOnRouteClickListener(card -> {
            NavDirections action = MapSpecifiedFragmentDirections
                    .actionMapSpecifiedFragmentToRouteDetailsFragment(card.id);
            NavHostFragment.findNavController(this).navigate(action);
        });

        binding.routeCardsRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.routeCardsRecycler.setAdapter(adapter);

        MapSpecifiedFragmentArgs args = MapSpecifiedFragmentArgs.fromBundle(requireArguments());
        String startLocation = args.getFrom();
        String endLocation = args.getTo();

        FilterViewModel filterVm = new ViewModelProvider(requireActivity())
                .get(FilterViewModel.class);

        MapSpecifiedViewModel mapVm = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()))
                .get(MapSpecifiedViewModel.class);

        Observer<Object> updateUI = __ -> {
            List<RouteConfig.Route> all = mapVm.getAllRoutes().getValue();
            Set<String> chips          = filterVm.getSelectedChips().getValue();
            if (all == null) return;

            List<RouteConfig.Route> pool = all.stream()
                    .filter(r -> r.fromDestination.equalsIgnoreCase(startLocation)
                            && r.toDestination.equalsIgnoreCase(endLocation))
                    .toList();

            List<RouteConfig.Route> matches = RouteFilterUtil
                    .filterByChips(pool, chips, startLocation, endLocation);

            List<String> remainingIds = matches.isEmpty()
                    ? pool.stream().map(r -> r.id).sorted().toList()
                    : matches.stream().map(r -> r.id).sorted().toList();

            int mapRes;
            if (matches.isEmpty()) {
                mapRes = R.drawable.large_empty_map_google;
            } else {
                List<String> ids = matches.stream()
                        .map(r -> r.id)
                        .sorted()
                        .toList();
                mapRes = mapVm.mapFor(ids);
            }
            binding.imageView7.setImageResource(mapRes);

            List<RouteCard> cards = new ArrayList<>();
            matches.forEach(r -> {
                int thumb = requireContext().getResources()
                        .getIdentifier(r.cardImageResource,
                                "drawable",
                                requireContext().getPackageName());
                cards.add(new RouteCard(r.id, r.title, thumb));
            });
            adapter.setData(cards);
        };

        mapVm.getAllRoutes().observe(getViewLifecycleOwner(), updateUI);
        filterVm.getSelectedChips().observe(getViewLifecycleOwner(), updateUI);

        showResults();
    }

    private void showLoading() {
        binding.imageView7.setVisibility(View.GONE);
        binding.routeCardsRecycler.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void showResults() {
        binding.imageView7.setVisibility(View.VISIBLE);
        binding.routeCardsRecycler.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }
}
