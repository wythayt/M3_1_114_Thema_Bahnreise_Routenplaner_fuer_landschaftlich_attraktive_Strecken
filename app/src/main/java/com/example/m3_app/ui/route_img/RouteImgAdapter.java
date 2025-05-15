package com.example.m3_app.ui.route_img;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m3_app.R;

import java.util.List;

public class RouteImgAdapter extends RecyclerView.Adapter<RouteImgAdapter.ViewHolder> {

    private final List<RouteImgCard> routeImgCards;

    public RouteImgAdapter(List<RouteImgCard> routeImgCards) {
        this.routeImgCards = routeImgCards;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView, categoryView;
//        Button pastButton;
        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageRoute);
            titleView = view.findViewById(R.id.textTitle);
            categoryView = view.findViewById(R.id.textViewType);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route_img, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RouteImgCard card = routeImgCards.get(position);
        holder.titleView.setText(card.title);
        holder.categoryView.setText(card.category);
        holder.imageView.setImageResource(card.imageId);
    }

    @Override
    public int getItemCount() {
        return routeImgCards.size();
    }
}

