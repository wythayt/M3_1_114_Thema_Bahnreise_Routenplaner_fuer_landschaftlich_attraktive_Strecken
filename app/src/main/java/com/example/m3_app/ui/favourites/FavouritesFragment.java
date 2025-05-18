package com.example.m3_app.ui.favourites;

import android.content.Context;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.m3_app.R;
import com.example.m3_app.databinding.FragmentFavouritesBinding;
import com.example.m3_app.ui.map_specified.MapSpecifiedViewModel;
import com.example.m3_app.ui.route_img.RouteImgAdapter;
import com.example.m3_app.ui.route_img.RouteImgCard;

import java.util.List;
import java.util.Objects;

public class FavouritesFragment extends Fragment {

    private FragmentFavouritesBinding binding;

    public FavouritesFragment() {
        super(R.layout.fragment_suggestions);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
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
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        binding.RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.back.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(requireParentFragment());
            navController.navigateUp();
        });

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

        MapSpecifiedViewModel mapVm = new ViewModelProvider(
                requireActivity(),
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())
        ).get(MapSpecifiedViewModel.class);

        mapVm.getAllRoutes().observe(getViewLifecycleOwner(), allRoutes -> {
            var prefs = requireContext().getSharedPreferences("LikedRoutes", Context.MODE_PRIVATE);

            List<RouteImgCard> likedCards = allRoutes.stream()
                    .filter(route -> prefs.getBoolean(route.id, false))
                    .map(route -> new RouteImgCard(
                            route.id,
                            route.title,
                            requireContext().getResources().getIdentifier(
                                    route.cardImageResource,
                                    "drawable",
                                    requireContext().getPackageName()
                            ),
                            route.mainCategory
                    ))
                    .toList();

            RouteImgAdapter adapter = new RouteImgAdapter(
                    likedCards,
                    card -> {
                        NavController navController = NavHostFragment.findNavController(FavouritesFragment.this);
                        navController.navigate(
                                FavouritesFragmentDirections.actionFavouritesFragmentToRouteDetailsFragment(card.id)
                        );
                    }
            );
            binding.RecyclerView.setAdapter(adapter);
        });
    }
}