package com.example.m3_app.ui.route_details;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.m3_app.R;
import com.example.m3_app.backend.RouteConfig;
import com.example.m3_app.databinding.FragmentDetailsBinding;
import com.example.m3_app.ui.map_specified.MapSpecifiedViewModel;

import java.util.Objects;

public class RouteDetailsFragment extends Fragment {

    private FragmentDetailsBinding binding;
    private MapSpecifiedViewModel mapVm;

    public RouteDetailsFragment() {
        super(R.layout.fragment_details);
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

        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.seeMoreFeedback.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.ratingsFragment);
        });

        binding.backText.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(requireParentFragment());
            navController.navigateUp();
        });

        binding.floatingButton.setOnClickListener(v -> {
            String routeId = RouteDetailsFragmentArgs.fromBundle(requireArguments()).getRouteId();
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(
                    RouteDetailsFragmentDirections.
                            actionRouteDetailsFragmentToTripDetailsFragment(routeId)
            );
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
        requireActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();

        mapVm = new ViewModelProvider(
                requireActivity(),
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())
        ).get(MapSpecifiedViewModel.class);

        String routeId = RouteDetailsFragmentArgs.fromBundle(requireArguments()).getRouteId();

        mapVm.getAllRoutes().observe(getViewLifecycleOwner(), routes -> routes.stream()
                .filter(route -> routeId.equals(route.id))
                .findFirst()
                .ifPresent(route -> {
                    bindRoute(route);

                    boolean isLiked = requireContext()
                            .getSharedPreferences("LikedRoutes", Context.MODE_PRIVATE)
                            .getBoolean(route.id, false);

                    int iconRes = isLiked ? R.drawable.baseline_favorite_24 : R.drawable.ic_favorite_24dp;
                    binding.buttonLike.setImageResource(iconRes);
                }));

        binding.shareIcon.setOnClickListener(v -> mapVm.getAllRoutes()
                .observe(getViewLifecycleOwner(), routes -> routes.stream()
                        .filter(route -> routeId.equals(route.id))
                        .findFirst()
                        .ifPresent(route -> {
                            String shareBody = buildShare(route);
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this route!");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

                            startActivity(Intent.createChooser(shareIntent, "Share via"));
                        })));

        binding.buttonLike.setOnClickListener(v -> {
            SharedPreferences prefs = requireContext().getSharedPreferences("LikedRoutes", Context.MODE_PRIVATE);
            boolean isLiked = prefs.getBoolean(routeId, false);

            boolean newLiked = !isLiked;
            prefs.edit().putBoolean(routeId, newLiked).apply();

            int iconRes = newLiked ? R.drawable.baseline_favorite_24 : R.drawable.ic_favorite_24dp;
            binding.buttonLike.setImageResource(iconRes);
        });

    }

    private String buildShare(RouteConfig.Route route) {
        return "Have a look at route \"" + route.title + "\"!\nFrom: " + route.fromDestination + ", To: " + route.toDestination + ", " + route.journeyTime + ".";
    }

    private void bindRoute(RouteConfig.Route r) {
        binding.titleText.setText(r.title);

        int imageRes = requireContext().getResources()
                .getIdentifier(r.cardImageResource, "drawable", requireContext().getPackageName());
        binding.cardImageResource.setImageResource(imageRes != 0 ? imageRes : R.drawable.placeholder);

        binding.mainCategory.setText(r.mainCategory);
        binding.rating.setText(r.rating);

        binding.routeName.setText(r.title);
        binding.journeyTime.setText(r.journeyTime);
        binding.cost.setText(r.cost);
        binding.distance.setText(r.distance);

        String ops = String.join(", ", r.transportOperator);
        binding.operators.setText(ops);
        String sts = String.join(" – ", r.stations.get(0));
        binding.stations.setText(sts);

        int iconRes = requireContext().getResources()
                .getIdentifier(r.mainCategory.toLowerCase().replaceAll("\\s+", ""),
                        "drawable", requireContext().getPackageName());
        binding.categoryIcon.setImageResource(iconRes != 0 ? iconRes : R.drawable.placeholder);
        binding.description.setText(r.description);

        StringBuilder b = getStringBuilder(r);
        binding.categories.setText(b.substring(0, b.length() - 2));
    }

    @NonNull
    private static StringBuilder getStringBuilder(RouteConfig.Route r) {
        StringBuilder b = new StringBuilder();
        if (r.filter.familyFun) b.append("Family Fun, ");
        if (r.filter.historyHeritage) b.append("History & Heritage, ");
        if (r.filter.artCulture) b.append("Art & Culture, ");
        if (r.filter.noTransfer) b.append("No Transfer, ");
        if (r.filter.audioGuide) b.append("Audio Guide, ");
        if (r.filter.withSights) b.append("With Sights, ");
        if (r.filter.topRated) b.append("Top Rated, ");
        if (r.filter.barrierFree) b.append("Barrier Free, ");
        if (r.filter.alongTheRiver) b.append("Along the River, ");
        if (r.filter.forest) b.append("Forest, ");
        if (r.filter.mountains) b.append("Mountains, ");
        return b;
    }
}
