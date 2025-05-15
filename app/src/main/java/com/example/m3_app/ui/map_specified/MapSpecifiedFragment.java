package com.example.m3_app.ui.map_specified;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.m3_app.R;
import com.example.m3_app.databinding.FragmentMapSpecifiedBinding;
import com.example.m3_app.ui.route_card.RouteCard;
import com.example.m3_app.ui.route_card.RouteCardAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MapSpecifiedFragment extends Fragment {
    private FragmentMapSpecifiedBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapSpecifiedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        List<RouteCard> cards = Arrays.asList(
                new RouteCard("Bavarian Bliss", R.drawable.placeholder),
                new RouteCard("Through Forests", R.drawable.placeholder)
        );

        binding.routeCardsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        RouteCardAdapter adapter = new RouteCardAdapter(cards);
        binding.routeCardsRecycler.setAdapter(adapter);

        binding.button3.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.searchFragmentTest);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

        AppCompatActivity activity = (AppCompatActivity) requireActivity();

        assert activity.getSupportActionBar() != null;
        activity.getSupportActionBar().show();

        View navBar = activity.findViewById(R.id.nav_view);
        assert navBar != null;
        navBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();

        ViewGroup root = (ViewGroup) requireActivity().findViewById(android.R.id.content);
        View navBar = root.findViewById(R.id.nav_view);

        assert navBar!=null;
        navBar.setVisibility(View.GONE);

        requireActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
    }
}
