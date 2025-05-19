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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m3_app.databinding.FragmentSearchTestBinding;
import com.example.m3_app.R;
import com.example.m3_app.ui.route_card.RouteCard;
import com.example.m3_app.ui.route_card.RouteCardAdapter;
import com.example.m3_app.ui.route_config.RouteConfigViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SearchFragmentTest extends Fragment {
    private FragmentSearchTestBinding binding;
    private final List<String> wantedIds = Arrays.asList("ID_01", "ID_02", "ID_03", "ID_04");

    public SearchFragmentTest() {
        super(R.layout.fragment_search_test);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchTestBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        showData();

        binding.radioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_specific) {
                binding.editTextTo2.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.radio_inspire) {
                binding.editTextTo2.setVisibility(View.GONE);
            }
        });
        binding.searchButton.setBackgroundColor(Color.parseColor("#E63946"));
        binding.searchButton.setOnClickListener(v -> {
            showLoading();
            String from = binding.editTextFrom1.getText().toString();
            String to = binding.editTextTo2.getText().toString();

            NavController navController = NavHostFragment.findNavController(this);

            int checkedId = binding.radioGroup1.getCheckedRadioButtonId();

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
            }
        });

        binding.textView4.setOnClickListener(v -> {
            String[] idsArray = wantedIds.toArray(new String[0]);

            NavDirections action = SearchFragmentTestDirections.actionSearchFragmentTestToSuggestionsFragment(idsArray);
            NavController nav = NavHostFragment.findNavController(this);
            nav.navigate(action);
        });


        RouteConfigViewModel configVm = new ViewModelProvider(
                requireActivity(),
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())
        ).get(RouteConfigViewModel.class);

        NavController nav = NavHostFragment.findNavController(this);

        RouteCardAdapter adapter = new RouteCardAdapter();
        adapter.setOnRouteClickListener(card -> {
            NavDirections action = SearchFragmentTestDirections
                    .actionSearchFragmentTestToRouteDetailsFragment(card.id);
            nav.navigate(action);
        });
        binding.routeCardsRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.routeCardsRecycler.setAdapter(adapter);

        configVm.getAllRoutes().observe(getViewLifecycleOwner(), routes -> {
            List<RouteCard> cards = new ArrayList<>();
            wantedIds.stream().map(configVm::getRouteById).filter(Objects::nonNull).forEach(route -> {
                int imgRes = requireContext().getResources()
                        .getIdentifier(route.cardImageResource, "drawable", getContext().getPackageName());
                cards.add(new RouteCard(route.id, route.title, imgRes));
            });
            adapter.setData(cards);
        });

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

    private void showLoading() {
        binding.linearLayoutSub.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void showData() {
        binding.linearLayoutSub.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }
}
