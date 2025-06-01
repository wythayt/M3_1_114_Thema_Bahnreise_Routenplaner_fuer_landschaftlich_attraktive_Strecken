package com.example.m3_app.ui.filter;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.m3_app.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.example.m3_app.databinding.SheetFiltersBinding;
import com.google.android.material.chip.Chip;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FiltersBottomSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        com.example.m3_app.databinding.SheetFiltersBinding binding = SheetFiltersBinding.inflate(inflater, container, false);
        binding.btnClose.setOnClickListener(v -> dismiss());

        FilterViewModel viewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);

        List<Chip> allChips = Stream.of(binding.chipGroup, binding.chipGroup2, binding.chipGroup3)
                .flatMap(group -> IntStream.range(0, group.getChildCount()).mapToObj(group::getChildAt))
                .filter(child -> child instanceof Chip)
                .map(child -> (Chip) child)
                .collect(Collectors.toList());

        viewModel.getSelectedChips().observe(getViewLifecycleOwner(), selected ->
                allChips.forEach(chip -> chip.setChecked(selected.contains(chip.getTag().toString()))));

        allChips.forEach(chip ->
                chip.setOnCheckedChangeListener((c, checked) ->
                        viewModel.updateChip(c.getTag().toString(), checked)));
        return binding.getRoot();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        MaterialButton filtersBtn = requireActivity().findViewById(R.id.button4);

        assert filtersBtn != null;
        filtersBtn.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.secondary_green)));
    }
}

