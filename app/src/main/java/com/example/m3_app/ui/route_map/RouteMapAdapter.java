package com.example.m3_app.ui.route_map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m3_app.R;
import com.example.m3_app.backend.RouteConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RouteMapAdapter extends RecyclerView.Adapter<com.example.m3_app.ui.route_map.RouteMapAdapter.ViewHolder> {
    public interface OnRouteClickListener {
        void onRouteClick(RouteConfig.Route route);
    }

    private final List<RouteConfig.Route> routes;
    private final boolean isUpcoming;
    private final OnRouteClickListener listener;

    List<LocalDate> dates;

    public RouteMapAdapter(List<RouteConfig.Route> routes, boolean isUpcoming, OnRouteClickListener listener, List<LocalDate> dates) {
        this.routes = routes;
        this.isUpcoming = isUpcoming;
        this.listener = listener;
        this.dates = dates;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView, dateView;
        Button pastButton;
        ImageButton upcomingButton;
        View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            imageView = view.findViewById(R.id.imageMap);
            titleView = view.findViewById(R.id.textTitle);
            dateView = view.findViewById(R.id.textDate);
            upcomingButton = view.findViewById(R.id.buttonGetIn);
            pastButton = view.findViewById(R.id.buttonBook);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route_map, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RouteConfig.Route route = routes.get(position);
        LocalDate date = dates.get(position);
        Context context = holder.itemView.getContext();

        int imgResId = context.getResources().getIdentifier(route.imageResource, "drawable", context.getPackageName());
        holder.imageView.setImageResource(imgResId != 0 ? imgResId : R.drawable.placeholder);
        holder.dateView.setText(formatDate(date.toString()));
        holder.titleView.setText(route.title);

        if (isUpcoming) {
            holder.upcomingButton.setVisibility(View.VISIBLE);
            holder.pastButton.setVisibility(View.GONE);
        } else {
            holder.upcomingButton.setVisibility(View.GONE);
            holder.pastButton.setVisibility(View.VISIBLE);
        }

        View.OnClickListener clickListener = v -> {
            if (listener != null) listener.onRouteClick(route);
        };
        holder.upcomingButton.setOnClickListener(clickListener);
        holder.pastButton.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    private String formatDate(String date) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
            Date resDate = originalFormat.parse(date);
            return displayFormat.format(Objects.requireNonNull(resDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }
}

