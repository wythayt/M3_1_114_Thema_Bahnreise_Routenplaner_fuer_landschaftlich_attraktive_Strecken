package com.example.m3_app.ui.map_not_specified;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.DialogFragment;

import com.example.m3_app.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class InfoDialogFragment extends DialogFragment {
    public static final String PREF_DONT_SHOW = "dont_show_info_dialog";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View dialogView = requireActivity()
                .getLayoutInflater()
                .inflate(R.layout.dialog_info, null);

        ImageView ivClose  = dialogView.findViewById(R.id.ivClose);
        Button gotIt = dialogView.findViewById(R.id.btnGotIt);
        Button dontShow = dialogView.findViewById(R.id.btnDontShow);

        TextView tv = dialogView.findViewById(R.id.tvMessage);

        Html.ImageGetter imageGetter = source -> {
            if (source.endsWith("/")) {
                source = source.substring(0, source.length() - 1);
            }
            int id = requireContext().getResources()
                    .getIdentifier(source, "drawable", requireContext().getPackageName());
            if (id == 0) {
                ColorDrawable empty = new ColorDrawable(Color.TRANSPARENT);
                empty.setBounds(0,0,0,0);
                return empty;
            }
            Drawable d = ContextCompat.getDrawable(requireContext(), id);
            assert d != null;
            d.setBounds(0,0,d.getIntrinsicWidth(), d.getIntrinsicHeight());
            return d;
        };


        CharSequence styled = HtmlCompat.fromHtml(
                getString(R.string.hint_message),
                HtmlCompat.FROM_HTML_MODE_LEGACY,
                imageGetter,
                null
        );
        tv.setText(styled);

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .setCancelable(false)
                .create();

        ivClose.setOnClickListener(v -> dialog.dismiss());
        gotIt.setOnClickListener(v -> dialog.dismiss());
        dontShow.setOnClickListener(v -> {
            requireContext()
                    .getSharedPreferences("prefs", Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean(PREF_DONT_SHOW, true)
                    .apply();
            dialog.dismiss();
        });

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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


