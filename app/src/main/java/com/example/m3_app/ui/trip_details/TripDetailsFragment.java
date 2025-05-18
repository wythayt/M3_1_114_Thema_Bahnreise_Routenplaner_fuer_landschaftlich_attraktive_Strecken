package com.example.m3_app.ui.trip_details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.m3_app.R;
import com.example.m3_app.backend.RouteConfig;
import com.example.m3_app.databinding.FragmentTripDetailsBinding;
import com.example.m3_app.ui.map_specified.MapSpecifiedViewModel;
import com.example.m3_app.ui.route_details.RouteDetailsFragmentArgs;

import java.util.List;
import java.util.Objects;

public class TripDetailsFragment extends Fragment {

    private FragmentTripDetailsBinding binding;

    private MapSpecifiedViewModel mapVm;

    public TripDetailsFragment() {
        super(R.layout.fragment_trip_details);
    }

    private ImageButton buttonClose;
    private ImageButton buttonHelp;
    private ImageView imageMap;
    private TextView endStop;
    private LinearLayout containerTripSegments;

    private boolean isFirstSegment = true;

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
        binding = FragmentTripDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        buttonClose = view.findViewById(R.id.buttonClose);
        buttonHelp = view.findViewById(R.id.buttonHelp);
        imageMap = view.findViewById(R.id.imageMap);
        containerTripSegments = view.findViewById(R.id.containerTripSegments);
        endStop = view.findViewById(R.id.endStop);

        imageMap.setImageResource(R.drawable.placeholder);

        buttonClose.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        buttonHelp.setOnClickListener(v -> {
            String routeId = RouteDetailsFragmentArgs.fromBundle(requireArguments()).getRouteId();
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(
                    TripDetailsFragmentDirections.
                            actionTripDetailsFragmentToRouteDetailsFragment(routeId)
            );
        });

        return view;
    }

    private void addSegment(String stationName, String trainName, List<String> stops, String connection, String company) {
        LayoutInflater inflater = getLayoutInflater();

        if (!isFirstSegment) {
            View connectionView = inflater.inflate(R.layout.route_connection, containerTripSegments, false);
            TextView connectionText = (TextView) connectionView;
            String text = "Layover:" + connection + "\nTransfer to the connecting train";
            connectionText.setText(text);
            containerTripSegments.addView(connectionText);
        }

        View segmentView = inflater.inflate(R.layout.route_segment, containerTripSegments, false);

        TextView stationNameView = segmentView.findViewById(R.id.textStationName);
        TextView trainNameView = segmentView.findViewById(R.id.textTrainName);
        TextView toggleStopsView = segmentView.findViewById(R.id.textToggleStops);
        LinearLayout stopsContainer = segmentView.findViewById(R.id.containerStops);
        ImageView logo = segmentView.findViewById(R.id.logo);

        int companyLogoRes = R.drawable.train_24dp;
        if (Objects.equals(company, "ÖBB")) {
            companyLogoRes = R.drawable.oebb_logo;
        } else if (Objects.equals(company, "DB")) {
            companyLogoRes = R.drawable.db_logo;
        } else if (Objects.equals(company, "Westbahn")) {
            companyLogoRes = R.drawable.westbahn_logo;
        }

        final int qrIconRes = R.drawable.qr_code_24dp;
        final int qrImageRes = R.drawable.sample_qr;

        logo.setImageResource(companyLogoRes);
        logo.setTag("company");

        logo.setOnClickListener(v -> {
            String currentTag = (String) logo.getTag();
            if ("company".equals(currentTag)) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Ticket Confirmation")
                        .setMessage("Have you bought a ticket from " + company + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            logo.setImageResource(qrIconRes);
                            logo.setTag("qr");
                        })
                        .setNegativeButton("No", null)
                        .show();
            } else if ("qr".equals(currentTag)) {
                ImageView qrView = new ImageView(requireContext());
                qrView.setImageResource(qrImageRes);
                new AlertDialog.Builder(requireContext())
                        .setView(qrView)
                        .setPositiveButton("Close", null)
                        .show();
            }
        });

        stationNameView.setText(stationName);
        trainNameView.setText(trainName);

        stopsContainer.removeAllViews();
        for (String stop : stops) {
            TextView stopView = new TextView(requireContext());
            stopView.setText("• " + stop);
            stopView.setTextSize(14);
            stopView.setPadding(0, 8, 0, 8);
            stopsContainer.addView(stopView);
        }

        toggleStopsView.setText(stops.size() + " stops ▼");
        toggleStopsView.setOnClickListener(v -> {
            if (stopsContainer.getVisibility() == View.GONE) {
                stopsContainer.setVisibility(View.VISIBLE);
                toggleStopsView.setText(stops.size() + " stops ▲");
            } else {
                stopsContainer.setVisibility(View.GONE);
                toggleStopsView.setText(stops.size() + " stops ▼");
            }
        });

        containerTripSegments.addView(segmentView);
        isFirstSegment = false;
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

        mapVm = new ViewModelProvider(
                requireActivity(),
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())
        ).get(MapSpecifiedViewModel.class);

        String routeId = TripDetailsFragmentArgs.fromBundle(requireArguments()).getRouteId();

        mapVm.getAllRoutes().observe(getViewLifecycleOwner(), routes -> routes.stream()
                .filter(route -> routeId.equals(route.id))
                .findFirst()
                .ifPresent(this::bindRoute));
    }

    private void bindRoute(RouteConfig.Route r) {
        int imageMapRes = requireContext().getResources()
                .getIdentifier(r.imageResource, "drawable", requireContext().getPackageName());
        binding.imageMap.setImageResource(imageMapRes != 0 ? imageMapRes : R.drawable.placeholder);

        for (List<String> l : r.stations) {
            addSegment(l.get(0), "train name", l.subList(1, l.size()), r.transferTime, r.transportOperator.get(r.stations.indexOf(l)));
        }

        binding.endStop.setText(r.toDestination);
    }
}