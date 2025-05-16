package com.example.m3_app.ui.feedback_card;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m3_app.R;

import java.util.List;

public class FeedbackCardAdapter extends RecyclerView.Adapter<FeedbackCardAdapter.ViewHolder> {

    private final List<FeedbackCard> feedbackCards;

    public FeedbackCardAdapter(List<FeedbackCard> feedbackCards) {
        this.feedbackCards = feedbackCards;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView1;
        ImageView imageView2;
        TextView commentView;

        public ViewHolder(View view) {
            super(view);
            imageView1 = view.findViewById(R.id.firstPhoto);
            imageView2 = view.findViewById(R.id.secondPhoto);
            commentView = view.findViewById(R.id.textTitleComment);
        }
    }
    @NonNull
    @Override
    public FeedbackCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feedback, parent, false);
        return new FeedbackCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackCardAdapter.ViewHolder holder, int position) {
        FeedbackCard card = feedbackCards.get(position);
        holder.commentView.setText(card.comment);
        holder.imageView1.setImageResource(card.userImages.get(0));
        holder.imageView2.setImageResource(card.userImages.get(1));
    }

    @Override
    public int getItemCount() {
        return feedbackCards.size();
    }
}
