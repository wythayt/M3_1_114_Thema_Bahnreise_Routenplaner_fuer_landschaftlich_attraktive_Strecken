package com.example.m3_app.ui.suggestions;

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
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.m3_app.R;
import com.example.m3_app.databinding.FragmentSuggestionsBinding;
import com.example.m3_app.ui.route_config.RouteConfigViewModel;
import com.example.m3_app.ui.route_img.RouteImgAdapter;
import com.example.m3_app.ui.route_img.RouteImgCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SuggestionsFragment extends Fragment {

    private FragmentSuggestionsBinding binding;

    public SuggestionsFragment() {
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
        binding = FragmentSuggestionsBinding.inflate(inflater, container, false);
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

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        requireActivity().getWindow()
                .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN
                );

        String[] idsArray = SuggestionsFragmentArgs.fromBundle(getArguments()).getRouteIds();
        List<String> suggestedIds = idsArray != null ? Arrays.asList(idsArray) : Collections.emptyList();

        RouteConfigViewModel configVm = new ViewModelProvider(
                requireActivity(),
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())
        ).get(RouteConfigViewModel.class);

        NavController nav = NavHostFragment.findNavController(this);
        RouteImgAdapter adapter = new RouteImgAdapter(new ArrayList<>(), card -> {
            NavDirections action =
                    SuggestionsFragmentDirections
                            .actionSuggestionsFragmentToRouteDetailsFragment(card.getId());
            nav.navigate(action);
        });

        binding.RecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));
        binding.RecyclerView.setAdapter(adapter);

        configVm.getAllRoutes().observe(getViewLifecycleOwner(), routes -> {
            List<RouteImgCard> cards = new ArrayList<>();
            suggestedIds.stream().map(configVm::getRouteById).filter(Objects::nonNull).forEach(r -> {
                int imageRes = requireContext().getResources()
                        .getIdentifier(r.cardImageResource, "drawable",
                                requireContext().getPackageName());
                cards.add(new RouteImgCard(
                        r.id,
                        r.title,
                        imageRes != 0 ? imageRes : R.drawable.placeholder,
                        r.mainCategory
                ));
            });
            adapter.updateData(cards);
        });

        binding.back.setOnClickListener(v ->
                NavHostFragment.findNavController(requireParentFragment()).navigateUp());
    }

}