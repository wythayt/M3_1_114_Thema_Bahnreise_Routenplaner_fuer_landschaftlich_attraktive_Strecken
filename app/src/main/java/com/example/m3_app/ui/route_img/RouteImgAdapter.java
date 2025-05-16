package com.example.m3_app.ui.route_img;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m3_app.R;

import java.util.List;
import java.util.Objects;

public class RouteImgAdapter extends RecyclerView.Adapter<RouteImgAdapter.ViewHolder> {

    public interface OnRouteClickListener {
        void onRouteClick(RouteImgCard card);
    }

    private final List<RouteImgCard> routeImgCards;
    private final OnRouteClickListener listener;

    public RouteImgAdapter(List<RouteImgCard> routeImgCards, OnRouteClickListener listener) {
        this.routeImgCards = routeImgCards;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView, categoryView;
        ImageButton likeButton;
        View cardView;

        //        Button pastButton;
        public ViewHolder(View view) {
            super(view);
            cardView = view;
            imageView = view.findViewById(R.id.imageRoute);
            titleView = view.findViewById(R.id.textTitle);
            categoryView = view.findViewById(R.id.textViewType);
            likeButton = view.findViewById(R.id.buttonLike);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RouteImgCard card = routeImgCards.get(position);
        holder.titleView.setText(card.title);
        holder.categoryView.setText(card.category);
        holder.imageView.setImageResource(card.imageId);

//        holder.likeButton.setOnClickListener(v -> {
//            // Handle like logic here
//        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRouteClick(card);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route_img, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return routeImgCards.size();
    }
}

