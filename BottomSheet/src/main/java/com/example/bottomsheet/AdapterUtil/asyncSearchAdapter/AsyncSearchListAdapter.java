package com.example.bottomsheet.AdapterUtil.asyncSearchAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottomsheet.AdapterUtil.AdapterAction;
import com.example.bottomsheet.AdapterUtil.AdapterItemListener;
import com.example.bottomsheet.BaseBottomSheetRecyclerModel;
import com.example.bottomsheet.databinding.ItemBaseSearchBinding;

import java.util.ArrayList;
import java.util.List;

public class AsyncSearchListAdapter<T extends BaseBottomSheetRecyclerModel> extends ListAdapter<T, AsyncSearchListAdapter<T>.ViewHolder> {

    private boolean isMultiSelect;
    private final AsyncListDiffer<T> mDiffer = new AsyncListDiffer<T>(this, new SearchListAdapterDiffUtil<T>());
    private AdapterItemListener<T> adapterItemListener;


    public AsyncSearchListAdapter(boolean isMultiSelect, AdapterItemListener<T> adapterItemListener) {

        super(new SearchListAdapterDiffUtil<T>());
        this.isMultiSelect = isMultiSelect;
        this.adapterItemListener = adapterItemListener;

    }


    public AsyncSearchListAdapter(boolean isMultiSelect) {

        super(new SearchListAdapterDiffUtil<T>());
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
        holder.setIsRecyclable(false);
        holder.bind(mDiffer.getCurrentList().get(position));

    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    @Override
    public void submitList(List<T> list) {
        mDiffer.submitList(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemBaseSearchBinding binding;

        public ViewHolder(@NonNull ItemBaseSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void bind(T baseSearchDbModel) {
            binding.txtSearch.setText(baseSearchDbModel.getName());

            if (!isMultiSelect) {
                binding.checkboxSearch.setVisibility(View.GONE);
            }

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isMultiSelect)
                        toggleSelection(baseSearchDbModel, binding);
                    else
                        adapterItemListener.onItemSelect(baseSearchDbModel, getAbsoluteAdapterPosition(), AdapterAction.SELECT);
                }
            });

        }
    }

    boolean isSelected(int position) {
        return mDiffer.getCurrentList().get(position).isSelected();
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
        for (int i = 0; i < mDiffer.getCurrentList().size(); i++) {
            mDiffer.getCurrentList().get(i).setSelected(true);

        }
    }

    public void clearSelections() {
        for (int i = 0; i < mDiffer.getCurrentList().size(); i++) {
            mDiffer.getCurrentList().get(i).setSelected(false);

        }

    }

    public int getSelectedItemCount() {
        int count = 0;

        for (int i = 0; i < mDiffer.getCurrentList().size(); i++) {
            if (mDiffer.getCurrentList().get(i).isSelected())
                count++;

        }

        return count;
    }

    public ArrayList<T> getSelectedItems() {
        ArrayList<T> mDifferTemp = new ArrayList<>();

        for (int i = 0; i < mDiffer.getCurrentList().size(); i++) {
            if (mDiffer.getCurrentList().get(i).isSelected())
                mDifferTemp.add(mDiffer.getCurrentList().get(i));

        }
        return mDifferTemp;

    }
}
