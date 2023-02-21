package com.example.bottomsheet.list;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.example.bottomsheet.AdapterUtil.AdapterAction;
import com.example.bottomsheet.AdapterUtil.AdapterItemMultiSelectListener;
import com.example.bottomsheet.AdapterUtil.BottomSheetRecyclerAdapter;
import com.example.bottomsheet.BaseBottomSheetRecyclerModel;
import com.example.bottomsheet.abstractions.IApplyButton;
import com.example.bottomsheet.abstractions.IStaticList;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class BottomSheetStaticListMultiSelect<T extends BaseBottomSheetRecyclerModel, V extends ViewBinding> extends BaseBottomSheetRecyclerView<V> implements IStaticList, IApplyButton {

    private BottomSheetRecyclerAdapter<T> bottomSheetRecyclerAdapter;
    private AdapterItemMultiSelectListener<T> adapterItemMultiSelectListener;
    private ArrayList<T> items;

    private ConstraintLayout.LayoutParams buttonLayoutParams;
    private int collapsedMargin; //Button margin in collapsed state
    private int buttonHeight;

    public BottomSheetStaticListMultiSelect(
            V viewBinding,
            Context context,
            int parentLayoutBottomSheetResId,
            RecyclerView.LayoutManager layoutManager,
            BaseBottomSheetRecyclerViewBuilder<V> bottomSheetRecyclerViewBuilder,
            ArrayList<T> items,
            AdapterItemMultiSelectListener<T> adapterItemMultiSelectListener) {

        super(viewBinding, context, parentLayoutBottomSheetResId, layoutManager, bottomSheetRecyclerViewBuilder);

        this.items = items;
        this.adapterItemMultiSelectListener = adapterItemMultiSelectListener;

        searchView.setVisibility(View.GONE);
        btnApply.setVisibility(View.VISIBLE);

        initRecyclerViewAdapter();
        initApplyButtonView();
        onClickApplyButton();
    }

    @Override
    public void initRecyclerViewAdapter() {

        bottomSheetRecyclerAdapter = new BottomSheetRecyclerAdapter<T>(items, true);
        recyclerView.setAdapter(bottomSheetRecyclerAdapter);
    }

    @Override
    public void initApplyButtonView() {

        buttonLayoutParams = (ConstraintLayout.LayoutParams) btnApply.getLayoutParams();

        //Calculate button margin from top
        buttonHeight = btnApply.getHeight(); //How tall is the button + experimental distance from bottom (Change based on your view)
        collapsedMargin = (int) (peekHeight - buttonHeight * 1.2); //Button margin in bottom sheet collapsed state
        buttonLayoutParams.topMargin = collapsedMargin;
        btnApply.setLayoutParams(buttonLayoutParams);
        recyclerView.setPadding(0,0,0, (int) (buttonHeight *2.2));
        Log.d("BottomSheetDynamic", "collapsedMargin: " + collapsedMargin + " expandedHeight: " + expandedHeight + " buttonHeight: "+ buttonHeight);

        initBottomSheetCallback(bottomSheetBehavior, btnApply, buttonLayoutParams, collapsedMargin, expandedHeight, buttonHeight);

    }

    @Override
    public void onClickApplyButton() {
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterItemMultiSelectListener.onItemMultiSelect(bottomSheetRecyclerAdapter.getSelectedItems(), AdapterAction.SELECT);
                bottomSheetBehavior.setPeekHeight(0);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }
}
