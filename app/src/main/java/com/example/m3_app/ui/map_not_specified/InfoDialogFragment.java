package com.example.m3_app.ui.map_not_specified;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.m3_app.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class InfoDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View dialogView = requireActivity()
                .getLayoutInflater()
                .inflate(R.layout.dialog_info, null);

        ImageView ivClose = dialogView.findViewById(R.id.ivClose);
        Button gotIt = dialogView.findViewById(R.id.btnGotIt);
        Button dontShow = dialogView.findViewById(R.id.btnDontShow);

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .setCancelable(false)
                .create();

        ivClose.setOnClickListener(v -> dialog.dismiss());
        gotIt   .setOnClickListener(v -> dialog.dismiss());
        dontShow.setOnClickListener(v -> {
            // TODO: implement “don’t show again”
            dialog.dismiss();
        });

        assert dialog.getWindow()!=null;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow()
                    .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}


