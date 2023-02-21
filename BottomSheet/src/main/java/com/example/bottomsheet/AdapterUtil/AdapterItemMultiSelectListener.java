package com.example.bottomsheet.AdapterUtil;

import java.util.ArrayList;

public interface AdapterItemMultiSelectListener<T> {
    void onItemMultiSelect(ArrayList<T> model, AdapterAction action);

}
