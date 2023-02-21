package com.example.bottomsheet.AdapterUtil;


public interface AdapterItemListener<T> {
    void onItemSelect(T model, int position, AdapterAction Action);
}
