package com.example.m3_app.ui.route_map;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RouteMapAdapter extends RecyclerView.Adapter<com.example.m3_app.ui.route_map.RouteMapAdapter.ViewHolder> {

    private final List<RouteMapCard> routeMapCards;

    public RouteMapAdapter(List<RouteMapCard> routeMapCards) {
        this.routeMapCards = routeMapCards;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView, dateView;
        Button pastButton;
        ImageButton upcomingButton;

        public ViewHolder(View view) {
            super(view);
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
        RouteMapCard card = routeMapCards.get(position);
        holder.titleView.setText(card.title);
        holder.dateView.setText(formatDate(card.date));
        holder.imageView.setImageResource(card.imageMapId);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        LocalDate today = LocalDate.now();
        LocalDate cardDate = LocalDate.parse(card.date, formatter);

        if(cardDate.isBefore(today)) {
            holder.upcomingButton.setVisibility(View.VISIBLE);
            holder.pastButton.setVisibility(View.GONE);
        } else {
            holder.upcomingButton.setVisibility(View.GONE);
            holder.pastButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return routeMapCards.size();
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

