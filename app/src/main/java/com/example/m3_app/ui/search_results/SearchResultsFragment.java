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
import com.example.m3_app.ui.route_img.RouteImgAdapter;
import com.example.m3_app.ui.route_img.RouteImgCard;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SearchResultsFragment extends Fragment {

    private FragmentSearchResultsBinding binding;

    public SearchResultsFragment() {
        super(R.layout.fragment_search_results);
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
        binding = FragmentSearchResultsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        List<RouteImgCard> cards = Arrays.asList(
                new RouteImgCard("Bavarian Bliss", R.drawable.placeholder, "Along the river", false),
                new RouteImgCard("Through Forests", R.drawable.placeholder, "Through the forest", false),
                new RouteImgCard("Bavarian Bliss", R.drawable.placeholder, "Along the river", false)

        );

        binding.RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.RecyclerView.setAdapter(new RouteImgAdapter(cards, card -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.routeDetailsFragment);
        }));

        binding.back.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(requireParentFragment());
            navController.navigateUp();
        });

//        binding.RecyclerView.findViewById(R.id.cardRoute).setOnClickListener(v -> {
//            NavController navController = NavHostFragment.findNavController(this);
//            navController.navigate(R.id.tripDetailsFragment);
//        });

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