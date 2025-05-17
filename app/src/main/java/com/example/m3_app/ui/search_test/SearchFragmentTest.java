package com.example.m3_app.ui.search_test;

import android.app.AlertDialog;
import android.graphics.Color;
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

import com.example.m3_app.databinding.FragmentSearchTestBinding;
import com.example.m3_app.R;
import com.example.m3_app.ui.route_card.RouteCard;
import com.example.m3_app.ui.route_card.RouteCardAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SearchFragmentTest extends Fragment {
    private FragmentSearchTestBinding binding;

    public SearchFragmentTest() {
        super(R.layout.fragment_search_test);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchTestBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.radioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_specific) {
                binding.editTextTo2.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.radio_inspire) {
                binding.editTextTo2.setVisibility(View.GONE);
            }
        });
        binding.searchButton.setBackgroundColor(Color.parseColor("#E63946"));
        binding.searchButton.setOnClickListener(v -> {
            String from = binding.editTextFrom1.getText().toString();
            String to = binding.editTextTo2.getText().toString();

            NavController navController = NavHostFragment.findNavController(this);

            int checkedId = binding.radioGroup1.getCheckedRadioButtonId();


            //TODO: error popup-window + retry
            if (checkedId == R.id.radio_specific) {
                if (from.isEmpty() || to.isEmpty()) {
                    new AlertDialog.Builder(requireContext()).setTitle("Missing Input")
                            .setMessage("Please enter both 'From' and 'End' stations")
                            .setPositiveButton("OK", null).show();
                    return;
                }
                SearchFragmentTestDirections.ActionSearchFragmentTestToMapSpecifiedFragment action =
                        SearchFragmentTestDirections
                                .actionSearchFragmentTestToMapSpecifiedFragment(from, to);

                navController.navigate(action);
                //navController.navigate(R.id.mapSpecifiedFragment);
            } else {
                if (from.isEmpty()) {
                    new AlertDialog.Builder(requireContext()).setTitle("Missing Input")
                            .setMessage("Please enter 'From' station")
                            .setPositiveButton("OK", null).show();
                    return;
                }
                SearchFragmentTestDirections.ActionSearchFragmentTestToMapNotSpecifiedFragment action =
                        SearchFragmentTestDirections
                                .actionSearchFragmentTestToMapNotSpecifiedFragment(from, to);
                navController.navigate(action);
                //navController.navigate(R.id.mapNotSpecifiedFragment);
            }
        });

        binding.textView4.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.suggestionsFragment);
        });
        List<RouteCard> cards = Arrays.asList(
                new RouteCard("ID_01","Bavarian Bliss", R.drawable.placeholder),
                new RouteCard("ID_02","Through Forests", R.drawable.placeholder)
        );

        binding.routeCardsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        RouteCardAdapter adapter = new RouteCardAdapter(cards);
        binding.routeCardsRecycler.setAdapter(adapter);

        return root;
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
