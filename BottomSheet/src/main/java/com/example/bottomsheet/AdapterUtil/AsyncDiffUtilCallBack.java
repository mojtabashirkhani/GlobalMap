package com.example.bottomsheet.AdapterUtil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class AsyncDiffUtilCallBack<T extends Comparable<T>> extends  DiffUtil.ItemCallback<T> {
    List<T> oldList;
    List<T> newList;

    public AsyncDiffUtilCallBack(List<T> oldList, List<T> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }



    @Override
    public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {

        return true;
    }

    @Override
    public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
        int result = newItem.compareTo(oldItem);

        if (result==0){
            return true;
        }
        return false;
    }
}
