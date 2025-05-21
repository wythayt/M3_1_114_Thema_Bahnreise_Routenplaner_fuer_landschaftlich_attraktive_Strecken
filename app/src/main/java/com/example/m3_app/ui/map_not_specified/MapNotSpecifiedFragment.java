package com.example.m3_app.ui.map_not_specified;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.m3_app.databinding.FragmentMapNotSpecifiedBinding;
import com.example.m3_app.ui.filter.FilterViewModel;
import com.example.m3_app.ui.filter.FiltersBottomSheetNotSpecified;
import com.example.m3_app.ui.map_specified.MapSpecifiedFragmentArgs;
import com.example.m3_app.ui.map_specified.MapSpecifiedFragmentDirections;
import com.example.m3_app.ui.map_specified.MapSpecifiedViewModel;
import com.example.m3_app.ui.route_card.RouteCard;
import com.example.m3_app.ui.route_card.RouteCardAdapter;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MapNotSpecifiedFragment extends Fragment {
    private FragmentMapNotSpecifiedBinding binding;

    public MapNotSpecifiedFragment() {
        super(R.layout.fragment_map_not_specified);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapNotSpecifiedBinding.inflate(inflater, container, false);
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

        binding.button5.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.searchResultsFragment);
        });

        SharedPreferences prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        if (!prefs.getBoolean(InfoDialogFragment.PREF_DONT_SHOW, false)) {
            new InfoDialogFragment().show(getChildFragmentManager(), "info_dialog");
        }

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Objects.requireNonNull(((AppCompatActivity) requireActivity())
                .getSupportActionBar()).hide();
        requireActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);
        requireActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        MaterialButton filtersBtn = binding.button4;
        ColorStateList beige  = ColorStateList.valueOf(requireContext().getColor(R.color.beige));
        ColorStateList green  = ColorStateList.valueOf(requireContext().getColor(R.color.secondary_green));
        filtersBtn.setOnClickListener(v -> {
            new FiltersBottomSheetNotSpecified().show(getParentFragmentManager(), "filters");
            filtersBtn.setBackgroundTintList(green);
        });
        getParentFragmentManager().addOnBackStackChangedListener(() -> {
            if (getParentFragmentManager().findFragmentByTag("filters") == null)
                filtersBtn.setBackgroundTintList(beige);
        });

        PhotoView pv = binding.imageView7;
        pv.setMinimumScale(0.5f);
        pv.setMediumScale(2f);
        pv.setMaximumScale(6f);
        binding.fabZoomIn.setOnClickListener(v -> pv.setScale(pv.getScale() * .8f,  true));
        binding.fabZoomOut.setOnClickListener(v -> pv.setScale(pv.getScale() * 1.25f, true));

        String  from       = binding.textView2.getText().toString();
        Button  confirmBtn = binding.confirmButton;

        RouteCardAdapter adapter = new RouteCardAdapter();
        adapter.setOnRouteClickListener(card -> {
            NavDirections go = MapSpecifiedFragmentDirections
                    .actionMapSpecifiedFragmentToRouteDetailsFragment(card.id);
            NavHostFragment.findNavController(this).navigate(go);
        });
        binding.routeCardsRecycler.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.routeCardsRecycler.setAdapter(adapter);

        FilterViewModel filterVm = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
        MapSpecifiedViewModel mapVm = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()))
                .get(MapSpecifiedViewModel.class);

        final String[] selectedTo = { null };

        Observer<Object> updateUI = ignored -> {
            List<RouteConfig.Route> all = mapVm.getAllRoutes().getValue();
            if (all == null) return;

            Set<String> chips   = filterVm.getSelectedChips().getValue();
            List<RouteConfig.Route> matches = (selectedTo[0] == null)
                    ? RouteFilterUtil.filterByChips(all, chips, from)
                    : RouteFilterUtil.filterByChips(all, chips, from, selectedTo[0]);

            RouteConfig.Route toShow = matches.isEmpty() ? all.get(0) : matches.get(0);
            int mapRes = getResources().getIdentifier(
                    toShow.imageResource, "drawable", requireContext().getPackageName());
            pv.setImageResource(mapRes);

            List<RouteCard> cards = new ArrayList<>();
            for (RouteConfig.Route r : matches) {
                int thumb = getResources().getIdentifier(
                        r.cardImageResource, "drawable", requireContext().getPackageName());
                cards.add(new RouteCard(r.id, r.title, thumb));
            }
            adapter.setData(cards);
        };

        adapter.setOnRouteClickListener(card -> {
            NavDirections go =
                    MapNotSpecifiedFragmentDirections
                            .actionMapNotSpecifiedFragmentToRouteDetailsFragment(card.id);
            NavHostFragment.findNavController(this).navigate(go);
        });

        View.OnClickListener goToResults = v -> {
            String to = selectedTo[0] == null ? "" : selectedTo[0];
            NavDirections go =
                    MapNotSpecifiedFragmentDirections
                            .actionMapNotSpecifiedToSearchResults(from, to);
            NavHostFragment.findNavController(this).navigate(go);
        };

        binding.button5.setOnClickListener(goToResults);
        binding.button.setOnClickListener(goToResults);

        mapVm.getAllRoutes().observe(getViewLifecycleOwner(), updateUI);
        filterVm.getSelectedChips().observe(getViewLifecycleOwner(), updateUI);

        List<Button> pickButtons = List.of(
                binding.button9
        );

        pickButtons.forEach(btn -> btn.setOnClickListener(v -> {
            pickButtons.forEach(b -> b.setEnabled(true));
            v.setEnabled(false);

            selectedTo[0] = (String) v.getTag();
            confirmBtn.setTag(selectedTo[0]);
            confirmBtn.setVisibility(View.VISIBLE);

            updateUI.onChanged(null);
        }));

        confirmBtn.setOnClickListener(v -> {
            String to = (String) confirmBtn.getTag();
            if (to == null) return;
            NavDirections go = MapNotSpecifiedFragmentDirections
                    .actionMapNotSpecifiedToMapSpecified(from, to);
            NavHostFragment.findNavController(this).navigate(go);
        });

        updateUI.onChanged(null);
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
