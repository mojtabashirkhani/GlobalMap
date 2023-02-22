package com.example.bottomsheet.list;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.example.bottomsheet.AdapterUtil.AdapterItemListener;
import com.example.bottomsheet.AdapterUtil.BottomSheetRecyclerAdapter;
import com.example.bottomsheet.model.BaseBottomSheetRecyclerModel;
import com.example.bottomsheet.abstractions.IStaticList;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class BottomSheetStaticListSingleSelect<T extends BaseBottomSheetRecyclerModel, V extends ViewBinding> extends BaseBottomSheetRecyclerView<V> implements IStaticList {

    private BottomSheetRecyclerAdapter<T> bottomSheetRecyclerAdapter;
    private AdapterItemListener<T> adapterItemListener;
    private ArrayList<T> items;


    public BottomSheetStaticListSingleSelect(
            V viewBinding,
            Context context,
            int parentLayoutBottomSheetResId,
            RecyclerView.LayoutManager layoutManager,
            BaseBottomSheetRecyclerViewBuilder<V> bottomSheetRecyclerViewBuilder,
            ArrayList<T> items,
            AdapterItemListener<T> adapterItemListener
            ) {

        super(viewBinding, context, parentLayoutBottomSheetResId, layoutManager, bottomSheetRecyclerViewBuilder);

        this.items = items;
        this.adapterItemListener = adapterItemListener;

        searchView.setVisibility(View.GONE);
        btnApply.setVisibility(View.INVISIBLE);

        recyclerView.setPadding(0,0,0, (int) (btnApply.getHeight() *1.5));

        initRecyclerViewAdapter();
    }

    @Override
    public void initRecyclerViewAdapter() {
        bottomSheetRecyclerAdapter = new BottomSheetRecyclerAdapter<T>(items, false, (model, position, action) -> {
            adapterItemListener.onItemSelect(model, position, action);
            bottomSheetBehavior.setPeekHeight(0);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });

        recyclerView.setAdapter(bottomSheetRecyclerAdapter);
    }
}
