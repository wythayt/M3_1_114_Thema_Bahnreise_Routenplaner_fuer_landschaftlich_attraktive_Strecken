package com.example.m3_app.ui.search_test;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m3_app.R;
import com.example.m3_app.databinding.FragmentSearchTestBinding;
import com.example.m3_app.ui.route_card.RouteCard;
import com.example.m3_app.ui.route_card.RouteCardAdapter;
import com.example.m3_app.ui.route_config.RouteConfigViewModel;
import com.example.m3_app.ui.search_test.SearchPrefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SearchFragmentTest extends Fragment {
    private FragmentSearchTestBinding binding;
    private SearchPrefs searchPrefs;

    private final List<String> wantedIds = List.of("ID_01", "ID_02", "ID_03", "ID_04");

    public SearchFragmentTest() {
        super(R.layout.fragment_search_test);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchTestBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchPrefs = new SearchPrefs(requireContext());

        String lastFrom = searchPrefs.getLastFrom();
        String lastTo   = searchPrefs.getLastTo();
        if (!TextUtils.isEmpty(lastFrom)) {
            binding.editTextFrom1.setText(lastFrom);
        }
        if (!TextUtils.isEmpty(lastTo)) {
            binding.editTextTo2.setText(lastTo);
        }

        AutoCompleteTextView fromAutoComplete = binding.editTextFrom1;
        AutoCompleteTextView toAutoComplete   = binding.editTextTo2;

        Set<String> rawFromSet = searchPrefs.getHistoryFrom();
        if (!rawFromSet.isEmpty()) {
            List<String> fromList = new ArrayList<>(rawFromSet);
            Collections.reverse(fromList);
            ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    fromList
            );
            fromAutoComplete.setAdapter(fromAdapter);
            fromAutoComplete.setThreshold(1);
        }

        Set<String> rawToSet = searchPrefs.getHistoryTo();
        if (!rawToSet.isEmpty()) {
            List<String> toList = new ArrayList<>(rawToSet);
            Collections.reverse(toList);
            ArrayAdapter<String> toAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    toList
            );
            toAutoComplete.setAdapter(toAdapter);
            toAutoComplete.setThreshold(1);
        }

        binding.radioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_specific) {
                binding.editTextTo2.setVisibility(View.VISIBLE);
            } else {
                binding.editTextTo2.setVisibility(View.GONE);
            }
        });

        binding.searchButton.setBackgroundColor(Color.parseColor("#E63946"));
        binding.searchButton.setOnClickListener(v -> {
            String from = binding.editTextFrom1.getText().toString().trim();
            String to   = binding.editTextTo2.getText().toString().trim();
            int checkedId = binding.radioGroup1.getCheckedRadioButtonId();

            if (checkedId == R.id.radio_specific) {
                if (TextUtils.isEmpty(from) || TextUtils.isEmpty(to)) {
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Missing Input")
                            .setMessage("Please enter both 'From' and 'End' stations")
                            .setPositiveButton("OK", null).show();
                    return;
                }

                searchPrefs.saveLastPair(from, to);
                searchPrefs.addToHistoryFrom(from);
                searchPrefs.addToHistoryTo(to);

                NavDirections action = SearchFragmentTestDirections
                        .actionSearchFragmentTestToMapSpecifiedFragment(from, to);
                NavHostFragment.findNavController(this).navigate(action);

            } else {
                if (TextUtils.isEmpty(from)) {
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Missing Input")
                            .setMessage("Please enter 'From' station")
                            .setPositiveButton("OK", null).show();
                    return;
                }

                searchPrefs.saveLastPair(from, "");
                searchPrefs.addToHistoryFrom(from);

                NavDirections action = SearchFragmentTestDirections
                        .actionSearchFragmentTestToMapNotSpecifiedFragment(from, "");
                NavHostFragment.findNavController(this).navigate(action);
            }
        });

        binding.textView4.setOnClickListener(v -> {
            String[] idsArray = wantedIds.toArray(new String[0]);
            NavDirections action = SearchFragmentTestDirections
                    .actionSearchFragmentTestToSuggestionsFragment(idsArray);
            NavHostFragment.findNavController(this).navigate(action);
        });

        RouteConfigViewModel configVm = new ViewModelProvider(
                requireActivity(),
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())
        ).get(RouteConfigViewModel.class);

        RouteCardAdapter adapter = new RouteCardAdapter();
        adapter.setOnRouteClickListener(card -> {
            NavDirections action = SearchFragmentTestDirections
                    .actionSearchFragmentTestToRouteDetailsFragment(card.id);
            NavHostFragment.findNavController(this).navigate(action);
        });
        binding.routeCardsRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.routeCardsRecycler.setAdapter(adapter);

        configVm.getAllRoutes().observe(getViewLifecycleOwner(), routes -> {
            List<RouteCard> cards = new ArrayList<>();
            for (String id : wantedIds) {
                var route = configVm.getRouteById(id);
                if (route != null) {
                    int imgRes = requireContext().getResources()
                            .getIdentifier(route.cardImageResource, "drawable", requireContext().getPackageName());
                    cards.add(new RouteCard(route.id, route.title, imgRes));
                }
            }
            adapter.setData(cards);
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        requireActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
