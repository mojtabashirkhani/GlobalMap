package com.example.bottomsheet.AdapterUtil;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public abstract class SyncDiffUtilCallBack<T extends Comparable<T>> extends  DiffUtil.Callback{
    List<T> oldList;
    List<T> newList;

    public abstract Bundle  getChangedPayload(T newItem,T oldItem);

    public SyncDiffUtilCallBack(List<T> oldList, List<T> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return  oldList !=null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = newList.get(newItemPosition).compareTo(oldList.get(oldItemPosition));

        if (result==0){
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        T newItem = newList.get(newItemPosition);
        T oldItem = oldList.get(oldItemPosition);
        return getChangedPayload(newItem, oldItem);
    }
}
