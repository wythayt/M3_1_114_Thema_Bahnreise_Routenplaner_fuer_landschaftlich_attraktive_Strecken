package com.example.m3_app.ui.search_results;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.m3_app.R;
import com.example.m3_app.databinding.FragmentSearchResultsBinding;
import com.example.m3_app.ui.filter.FilterViewModel;
import com.example.m3_app.ui.map_specified.MapSpecifiedViewModel;
import com.example.m3_app.ui.route_img.RouteImgAdapter;
import com.example.m3_app.ui.route_img.RouteImgCard;

import java.util.List;
import java.util.Objects;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.m3_app.backend.RouteConfig;
import com.example.m3_app.backend.utility.RouteFilterUtil;
import com.example.m3_app.ui.map_specified.MapSpecifiedFragmentArgs;
import java.util.ArrayList;
import java.util.Set;

public class SearchResultsFragment extends Fragment {

    private FragmentSearchResultsBinding binding;
    private RouteImgAdapter adapter;
    private FilterViewModel filterVm;
    private MapSpecifiedViewModel mapVm;
    private String fromDestination;
    private String toDestination;

    public SearchResultsFragment() {
        super(R.layout.fragment_search_results);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO replace "Filter" button with "Sort by"?
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

        binding = FragmentSearchResultsBinding.inflate(inflater, container, false);

        MapSpecifiedFragmentArgs args = MapSpecifiedFragmentArgs.fromBundle(requireArguments());
        fromDestination = args.getFrom();
        toDestination   = args.getTo();

        adapter = new RouteImgAdapter(new ArrayList<>(), card -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.routeDetailsFragment);
        });
        binding.RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.RecyclerView.setAdapter(adapter);

        binding.back.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(requireParentFragment());
            navController.navigateUp();
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        requireActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        filterVm = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
        mapVm = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())
        ).get(MapSpecifiedViewModel.class);

        Observer<Object> rebuild = unused -> {
            List<RouteConfig.Route> allRoutes = mapVm.getAllRoutes().getValue();
            Set<String> selectedChips = filterVm.getSelectedChips().getValue();
            if (allRoutes == null) return;

            List<RouteConfig.Route> matches = RouteFilterUtil.filterByChips(
                    allRoutes, selectedChips, fromDestination, toDestination);

            List<RouteImgCard> cards = new ArrayList<>();
            for (RouteConfig.Route r : matches) {
                int imageRes = requireContext().getResources()
                        .getIdentifier(r.cardImageResource, "drawable", requireContext().getPackageName());
                cards.add(new RouteImgCard(
                        r.title,
                        imageRes != 0 ? imageRes : R.drawable.placeholder,
                        r.mainCategory,
                        false
                ));
            }

            adapter.updateData(cards);
        };

        mapVm.getAllRoutes().observe(getViewLifecycleOwner(), rebuild);
        filterVm.getSelectedChips().observe(getViewLifecycleOwner(), rebuild);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}