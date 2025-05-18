package com.example.m3_app.ui.favourites;

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
import com.example.m3_app.databinding.FragmentFavouritesBinding;
import com.example.m3_app.ui.route_img.RouteImgAdapter;
import com.example.m3_app.ui.route_img.RouteImgCard;

import java.util.Arrays;
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


//        List<RouteImgCard> cards = Arrays.asList(
//                new RouteImgCard("Bavarian Bliss", R.drawable.placeholder, "Along the river", true),
//                new RouteImgCard("Through Forests", R.drawable.placeholder, "Through the forest", true)
//        );

        binding.RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.RecyclerView.setAdapter(new RouteImgAdapter(cards, card -> {
//            NavController navController = NavHostFragment.findNavController(this);
//            navController.navigate(R.id.routeDetailsFragment);
//        }));

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
    }
}