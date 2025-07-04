package com.example.m3_app.ui.route_card;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.example.m3_app.R;

import java.util.List;

public class RouteCardAdapter extends RecyclerView.Adapter<RouteCardAdapter.ViewHolder> {
    public interface onRouteClickListener {
        void onRouteClick(RouteCard routeCard);
    }

    private final List<RouteCard> routeCards = new ArrayList<>();
    private onRouteClickListener onRouteClickListener;

    public RouteCardAdapter(List<RouteCard> cards) {
        this.setData(cards);
    }

    public RouteCardAdapter() {
    }

    public void setOnRouteClickListener(onRouteClickListener onRouteClickListener) {
        this.onRouteClickListener = onRouteClickListener;
    }

    public void setData(List<RouteCard> newCards) {
        routeCards.clear();
        routeCards.addAll(newCards);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int vt) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int pos) {
        RouteCard card = routeCards.get(pos);
        h.titleView.setText(card.title);
        h.imageView.setImageResource(card.imageResId);

        Context context = h.itemView.getContext();
        SharedPreferences prefs = context.getSharedPreferences("LikedRoutes", Context.MODE_PRIVATE);
        boolean isLiked = prefs.getBoolean(card.id, false);

        h.likeButton.setImageResource(
                isLiked ? R.drawable.baseline_favorite_24 : R.drawable.ic_favorite_24dp
        );

        h.likeButton.setOnClickListener(v -> {
            boolean newLiked = !prefs.getBoolean(card.id, false);
            prefs.edit().putBoolean(card.id, newLiked).apply();

            h.likeButton.setImageResource(
                    newLiked ? R.drawable.baseline_favorite_24 : R.drawable.ic_favorite_24dp
            );
        });


        h.itemView.setOnClickListener(v -> {
            if (onRouteClickListener != null) onRouteClickListener.onRouteClick(card);
        });
    }

    @Override
    public int getItemCount() {
        return routeCards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;
        ImageView likeButton;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.routeImage);
            titleView = view.findViewById(R.id.routeTitle);
            likeButton = view.findViewById(R.id.buttonLike);
        }
    }
}



