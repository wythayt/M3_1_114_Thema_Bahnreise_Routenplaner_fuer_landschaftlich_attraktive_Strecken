package com.example.m3_app.ui.route_card;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m3_app.R;

import java.util.List;

public class RouteCardAdapter extends RecyclerView.Adapter<RouteCardAdapter.ViewHolder> {

    private final List<RouteCard> routeCards;

    public RouteCardAdapter(List<RouteCard> routeCards) {
        this.routeCards = routeCards;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.routeImage);
            titleView = view.findViewById(R.id.routeTitle);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RouteCard card = routeCards.get(position);
        holder.titleView.setText(card.title);
        holder.imageView.setImageResource(card.imageResId);
    }

    @Override
    public int getItemCount() {
        return routeCards.size();
    }
}

