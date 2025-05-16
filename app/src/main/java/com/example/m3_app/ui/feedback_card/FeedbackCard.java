package com.example.m3_app.ui.feedback_card;

import java.util.List;

public class FeedbackCard {
    public String comment;

    public List<Integer> userImages;

    public FeedbackCard(String comment, List<Integer> userImages) {
        this.comment = comment;
        this.userImages = userImages;
    }
}
