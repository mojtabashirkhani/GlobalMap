package com.example.bottomsheet.map;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.example.bottomsheet.BaseBottomSheet;
import com.example.bottomsheet.R;
import com.example.bottomsheet.abstractions.IMap;
import com.example.bottomsheet.searchWatcher.SearchWatcher;
import com.google.android.gms.maps.MapView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.samiei.globalmap.MapInstructor;
import com.samiei.globalmap.Models.MapObjectModel;

import org.osmdroid.config.Configuration;


public class BaseBottomSheetMapView<V extends ViewBinding, M extends MapObjectModel, I extends MapInstructor> extends BaseBottomSheet<V> implements IMap {

    protected M mapModel;
    protected I mapInstructor;
    protected View view;
    protected MapView mapView;

    protected SearchWatcher searchView;
    protected RecyclerView recyclerView;
    protected Button btnApply;
    protected Button btnCancel;

    protected ImageView closeBtn;
    protected ImageView searchIcon;
    protected EditText searchEditText;


    public BaseBottomSheetMapView(V viewBinding,
                                  Context context,
                                  int parentLayoutBottomSheetResId,
                                  I mapInstructor,
                                  M mapModel
                                  ) {

        super(viewBinding, context, parentLayoutBottomSheetResId);

        this.mapModel = mapModel;
        this.mapInstructor = mapInstructor;

        view = viewBinding.getRoot();

        mapView = view.findViewById(R.id.mapView_bottomSheet);
        searchView = view.findViewById(R.id.searchView_mapView_bottomSheet);
        recyclerView = view.findViewById(R.id.recyclerViewSearch_mapView_bottomSheet);

        closeBtn = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        btnApply = view.findViewById(R.id.button_apply_mapView_bottomSheet);
        btnCancel = view.findViewById(R.id.button_cancel_mapView_bottomSheet);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                bottomSheetBehavior.setDraggable(newState != BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        initMapView();
    }

    @Override
    protected void initBottomSheetView(Context context) {
//        viewBottomsheet.setBackgroundResource(R.drawable.bottom_dialog_shape);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        //Retrieve bottom sheet parameters

        //Setup bottom sheet
        BottomSheetBehavior.from(viewBottomsheet).setSkipCollapsed(false);
        BottomSheetBehavior.from(viewBottomsheet).setHideable(true);
    }

    @Override
    public void initMapView() {
        initBottomSheetView(context);
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));

    }

}
