package com.example.bottomsheet.AdapterUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottomsheet.BaseBottomSheetRecyclerModel;
import com.example.bottomsheet.databinding.ItemBaseSearchBinding;

import java.util.ArrayList;

public class BottomSheetRecyclerAdapter<T extends BaseBottomSheetRecyclerModel> extends RecyclerView.Adapter<BottomSheetRecyclerAdapter<T>.ViewHolder> {

    private ArrayList<T> items;
    private boolean isMultiSelect;
    private AdapterItemListener<T> adapterItemListener;

    public BottomSheetRecyclerAdapter(ArrayList<T> items, boolean isMultiSelect, AdapterItemListener<T> adapterItemListener) {
        this.items = items;
        this.isMultiSelect = isMultiSelect;
        this.adapterItemListener = adapterItemListener;
    }

    public BottomSheetRecyclerAdapter(ArrayList<T> items, boolean isMultiSelect) {
        this.items = items;
        this.isMultiSelect = isMultiSelect;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBaseSearchBinding itemBaseSearchBinding =
                ItemBaseSearchBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBaseSearchBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemBaseSearchBinding binding;
        public ViewHolder(@NonNull ItemBaseSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(T baseSearchDbModel) {
            binding.txtSearch.setText(baseSearchDbModel.getName());

            if (isMultiSelect)
                binding.checkboxSearch.setVisibility(View.VISIBLE);
             else
                binding.checkboxSearch.setVisibility(View.GONE);



            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isMultiSelect)
                        toggleSelection(baseSearchDbModel, binding);
                    else
                        adapterItemListener.onItemSelect(baseSearchDbModel, getAdapterPosition(), AdapterAction.SELECT);
                }
            });


        }
    }

    boolean isSelected(int position) {
        return items.get(position).isSelected();
    }

    public void toggleSelection(T baseSearchDbModel, ItemBaseSearchBinding binding) {
        if (!baseSearchDbModel.isSelected()) {
            baseSearchDbModel.setSelected(true);
            binding.checkboxSearch.setChecked(true);

        } else {
            baseSearchDbModel.setSelected(false);
            binding.checkboxSearch.setChecked(false);

        }

    }

    public void selectAll() {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setSelected(true);

        }
    }

    public void clearSelections() {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setSelected(false);

        }

    }

    public int getSelectedItemCount() {
        int count = 0;

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSelected())
                count++;

        }

        return count;
    }

    public ArrayList<T> getSelectedItems() {
        ArrayList<T> mDifferTemp = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSelected())
                mDifferTemp.add(items.get(i));

        }
        return mDifferTemp;

    }
}
