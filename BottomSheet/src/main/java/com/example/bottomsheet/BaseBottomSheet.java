package com.example.bottomsheet;


import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewbinding.ViewBinding;

import com.example.bottomsheet.abstractions.AbstractBottomSheet;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class BaseBottomSheet<V extends ViewBinding> extends AbstractBottomSheet {

    protected V viewBinding;
    protected Context context;
    protected int parentLayoutBottomSheetResId;

    protected View viewBottomsheet;
    protected BottomSheetBehavior bottomSheetBehavior;
    protected int expandedHeight; //Height of bottom sheet in expanded state
    protected int peekHeight;

    public BaseBottomSheet(V viewBinding, Context context, int parentLayoutBottomSheetResId) {
        this.viewBinding = viewBinding;
        this.context = context;
        this.parentLayoutBottomSheetResId = parentLayoutBottomSheetResId;

        View view = viewBinding.getRoot();
        viewBottomsheet = view.findViewById(parentLayoutBottomSheetResId);
        bottomSheetBehavior = BottomSheetBehavior.from(viewBottomsheet);

    }


    @Override
    protected void initBottomSheetView(Context context) {
//        viewBottomsheet.setBackgroundResource(R.drawable.bottom_dialog_shape);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        //Retrieve bottom sheet parameters
        ViewGroup.LayoutParams bottomSheetLayoutParams = viewBottomsheet.getLayoutParams();
        bottomSheetLayoutParams.height = getWindowHeight(context);

        expandedHeight = bottomSheetLayoutParams.height;
        peekHeight = (int) (expandedHeight / 2.3); //Peek height to 70% of expanded height (Change based on your view)

        //Setup bottom sheet
        viewBottomsheet.setLayoutParams(bottomSheetLayoutParams);
        BottomSheetBehavior.from(viewBottomsheet).setSkipCollapsed(false);
        BottomSheetBehavior.from(viewBottomsheet).setPeekHeight(peekHeight);
        BottomSheetBehavior.from(viewBottomsheet).setHideable(true);

    }

    @Override
    public void closeBottomSheet() {
        if (bottomSheetBehavior != null) {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED ||
                    (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) &&
                            bottomSheetBehavior.getPeekHeight() != 0) {
                bottomSheetBehavior.setPeekHeight(0);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
        }
    }

    public int getWindowHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

}
