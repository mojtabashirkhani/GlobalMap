package com.example.bottomsheet.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.example.bottomsheet.AdapterUtil.AdapterAction;
import com.example.bottomsheet.AdapterUtil.AdapterItemListener;
import com.example.bottomsheet.AdapterUtil.asyncSearchAdapter.AsyncSearchListAdapter;
import com.example.bottomsheet.model.BaseBottomSheetRecyclerModel;
import com.example.bottomsheet.abstractions.IApplyButton;
import com.example.bottomsheet.abstractions.ICancelButton;
import com.example.bottomsheet.abstractions.IDynamicList;
import com.example.bottomsheet.abstractions.ISearch;
import com.example.bottomsheet.model.LocationModel;
import com.example.bottomsheet.searchWatcher.SearchWatcher;
import com.example.bottomsheet.searchWatcher.Watcher;
import com.google.android.gms.maps.model.LatLng;
import com.samiei.globalmap.Interfaces.IMapClickEvents;
import com.samiei.globalmap.MapDesigns.OsmDroid;
import com.samiei.globalmap.MapInstructor;
import com.samiei.globalmap.MapServices.MapIrService;
import com.samiei.globalmap.Models.MapObjectModel;
import com.samiei.globalmap.Models.search.Geom;
import com.samiei.globalmap.Models.search.Value;
import com.samiei.globalmap.ResultInterface.IResponse;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Collection;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BottomSheetMap<T extends BaseBottomSheetRecyclerModel, V extends ViewBinding, M extends MapObjectModel, I extends MapInstructor> extends BaseBottomSheetMapView<V, M, I> implements ISearch, IApplyButton, ICancelButton, IDynamicList, AdapterItemListener<T> {

    private ApplyButtonMap applyButtonMap;

    private boolean isSearchEnable;
    private boolean isButtonEnable;
    private String searchHint;

    private DividerItemDecoration dividerItemDecoration;
    private RecyclerView.LayoutManager layoutManager;


    private AsyncSearchListAdapter<T> asyncSearchListAdapter;
    private ArrayList<T> filteredListBaseSearchModel;
    private ArrayList<String> addressList;
    private MapIrService mapIrService;
    private String select;
    private String filter;


    public BottomSheetMap(V viewBinding,
                          Context context,
                          int parentLayoutBottomSheetResId,
                          I mapInstructor,
                          M mapModel,
                          BottomSheetMapBuilder<V> mapViewBuilder,
                          ApplyButtonMap applyButtonMap) {

        super(viewBinding, context, parentLayoutBottomSheetResId, mapInstructor, mapModel);

        this.isSearchEnable = mapViewBuilder.isSearchEnable;
        this.isButtonEnable = mapViewBuilder.isButtonEnable;
        this.searchHint = mapViewBuilder.searchHint;
        this.layoutManager = mapViewBuilder.layoutManager;
        this.dividerItemDecoration = mapViewBuilder.dividerItemDecoration;
        this.mapIrService = mapViewBuilder.mapIrService;
        this.select = mapViewBuilder.select;
        this.filter = mapViewBuilder.filter;

        this.applyButtonMap = applyButtonMap;

        if (isSearchEnable) {
            searchView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(layoutManager);
            filteredListBaseSearchModel = new ArrayList<>();
            addressList = new ArrayList<>();
            recyclerView.setItemAnimator(null);
            asyncSearchListAdapter = new AsyncSearchListAdapter<T>(false, this);

            if (dividerItemDecoration != null)
                recyclerView.addItemDecoration(dividerItemDecoration);

            initSearchView(context, searchView, searchHint, closeBtn, searchIcon, searchEditText);
            closeButtonSearchListener();

        } else
            searchView.setVisibility(View.GONE);

        if (isButtonEnable) {
            btnApply.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
            initApplyButtonView();
            onClickApplyButton();
            initCancelButtonView();
            onClickCancelButton();

        } else {
            btnApply.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);

        }

    }

    @Override
    public void initMapView() {
        super.initMapView();
        mapInstructor.onMapContentClickListener(mapInstructor.CURRENT_LOCATION_GROUP_ID(), new IMapClickEvents() {
            @Override
            public void onMarkSingleTap(int index, Object object) {

            }

            @Override
            public void onMarkLongTap(int index, Object object) {

            }

            @Override
            public void onOtherItemsClick() {

            }

            @Override
            public void onOtherItemsClick(GeoPoint point) {

                mapInstructor.removeExistingFeatures(mapInstructor.CURRENT_LOCATION_GROUP_ID());

                mapModel.setLatLng(new LatLng(point.getLatitude(), point.getLongitude()));


                mapInstructor.addSingleLocationLayer(mapModel);
                mapView.invalidate();



            }
        });
    }

    @Override
    protected void initSearchView(Context context, SearchWatcher searchView, String searchHint, ImageView closeBtn, ImageView searchIcon, EditText searchEditText) {
        super.initSearchView(context, searchView, searchHint, closeBtn, searchIcon, searchEditText);
        searchView.addTextWatcher(new Watcher() {
            @Override
            public void onTextChange(String s) {
                filteredListBaseSearchModel.clear();
                addressList.clear();
                recyclerView.setVisibility(View.VISIBLE);
                mapView.setVisibility(View.INVISIBLE);
               /* shimmerFrameLayout.setVisibility(View.VISIBLE);

                shimmerLoading.startLoading(new ShimmerFrameLayout[]{shimmerFrameLayout}
                , new View[]{recyclerView});*/

                if (!s.trim().isEmpty()) {

                    if (!(select.trim().isEmpty()) && (filter.trim().isEmpty())) {
                        mapIrService.getAutoCompleteSearchResult(s, select, new IResponse() {
                            @SuppressLint("CheckResult")
                            @Override
                            public void onSuccess(ArrayList arrayListData) {
//
                                filteredListBaseSearchModel.addAll(arrayListData);

                                initListAdapter();

                                /*shimmerLoading.stopLoading();
                                shimmerFrameLayout.setVisibility(View.GONE);*/

                            }

                            @Override
                            public void onFailed(String type, String error) {
                                /*shimmerLoading.stopLoading();
                                shimmerFrameLayout.setVisibility(View.GONE);*/

                            }
                        });
                    } else if (!(select.trim().isEmpty()) && !(filter.trim().isEmpty())) {
                        mapIrService.getAutoCompleteSearchResult(s, select, filter, new IResponse() {
                            @Override
                            public void onSuccess(ArrayList arrayListData) {

                                io.reactivex.Observable.fromIterable((ArrayList<Value>)arrayListData)
                                        .subscribeOn(Schedulers.computation())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .map(value -> value.getAddress())
                                        .distinct()
                                        .map(address -> new LocationModel((String) address, "address"))
                                        .toList()
                                        .subscribe(list -> filteredListBaseSearchModel.addAll((Collection<? extends T>) list));
//                                filteredListBaseSearchModel.addAll(arrayListData);

                                initListAdapter();

                                /*shimmerLoading.stopLoading();
                                shimmerFrameLayout.setVisibility(View.GONE);*/
                            }

                            @Override
                            public void onFailed(String type, String error) {
                                //TODO handle error
                                /*shimmerLoading.stopLoading();
                                shimmerFrameLayout.setVisibility(View.GONE);*/

                            }
                        });
                    }


                } else {
                    initListAdapter();

                    /*shimmerLoading.stopLoading();
                    shimmerFrameLayout.setVisibility(View.GONE);*/

                    if (mapView.getVisibility() == View.INVISIBLE)
                    mapView.setVisibility(View.VISIBLE);

                }
            }
        }, 1000);
    }

    @Override
    public void closeButtonSearchListener() {
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", false);

            }
        });
    }

    @Override
    public void initApplyButtonView() {

    }

    @Override
    public void onClickApplyButton() {
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchView != null)
                    searchView.setQuery("", false);

                if (mapModel != null)
                    applyButtonMap.onApplyButtonListener(mapModel.getLatLng());

                BottomSheetMap.super.closeBottomSheet();
            }
        });
    }

    @Override
    public void initCancelButtonView() {

    }

    @Override
    public void onClickCancelButton() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchView != null)
                    searchView.setQuery("", false);

                BottomSheetMap.super.closeBottomSheet();
            }
        });
    }

    @Override
    public void initListAdapter() {
        asyncSearchListAdapter.submitList(filteredListBaseSearchModel);
        recyclerView.setAdapter(asyncSearchListAdapter);

    }

    @Override
    public void onItemSelect(T model, int position, AdapterAction Action) {
        recyclerView.setVisibility(View.INVISIBLE);

        if (!(select.trim().isEmpty()) && (filter.trim().isEmpty())) {

            mapIrService.getLocationFromAddress(model.getName(), select, new IResponse() {
                @Override
                public void onSuccess(ArrayList arrayListData) {
//                                            Log.d("BottomSheetMap", "location: " + arrayListData.size());
                    Observable.just(((Geom) arrayListData.get(0)).getCoordinates())
                            .map(coordinates -> new LatLng((Double) coordinates.get(1), (Double) coordinates.get(0)))
                            .subscribe(latLng -> mapModel.setLatLng(latLng));

                    Log.d("BottomSheetMap", "latlng: " + mapModel.getLatLng());

                    mapInstructor.removeExistingFeatures(mapInstructor.CURRENT_LOCATION_GROUP_ID());
                    mapInstructor.addSingleLocationLayer(mapModel);
                    mapInstructor.zoomCameraToSpecificPosition(mapInstructor.convertLatLngToGeoPoint(mapModel.getLatLng()), 20);

                    mapView.invalidate();
                    mapView.setVisibility(View.VISIBLE);

                }

                @Override
                public void onFailed(String type, String error) {
                    //TODO should handle error
                }
            });

        } else if (!(select.trim().isEmpty()) && !(filter.trim().isEmpty())) {

            mapIrService.getLocationFromAddress(model.getName(), select, filter, new IResponse() {
                @Override
                public void onSuccess(ArrayList arrayListData) {
//                                            Log.d("BottomSheetMap", "location: " + arrayListData.size());
                    Observable.just(((Geom) arrayListData.get(0)).getCoordinates())
                            .map(coordinates -> new LatLng((Double) coordinates.get(1), (Double) coordinates.get(0)))
                            .subscribe(latLng -> mapModel.setLatLng(latLng));

                    Log.d("BottomSheetMap", "latlng: " + mapModel.getLatLng());

                    ((OsmDroid) mapInstructor).removeExistingFeatures(mapInstructor.CURRENT_LOCATION_GROUP_ID());
                    ((OsmDroid) mapInstructor).addSingleLocationLayer(mapModel);
                    ((OsmDroid) mapInstructor).zoomCameraToSpecificPosition(mapInstructor.convertLatLngToGeoPoint(mapModel.getLatLng()), 20);

                    mapView.invalidate();
                    mapView.setVisibility(View.VISIBLE);

                }

                @Override
                public void onFailed(String type, String error) {
                    //TODO should handle error
                }
            });

        }

    }

    public static class BottomSheetMapBuilder<V extends ViewBinding> {
        private boolean isSearchEnable;
        private boolean isButtonEnable;
        private String searchHint;
        private RecyclerView.LayoutManager layoutManager;
        private DividerItemDecoration dividerItemDecoration;
        private MapIrService mapIrService;
        private String select = "";
        private String filter = "";

        public BottomSheetMapBuilder<V> setButtonApplyEnable(boolean isButtonEnable) {
            this.isButtonEnable = isButtonEnable;
            return this;
        }

        public BottomSheetMapBuilder<V> setSearchEnable(boolean isSearchEnable) {
            this.isSearchEnable = isSearchEnable;
            return this;
        }

        public BottomSheetMapBuilder<V> setSearchHint(String searchHint) {
            this.searchHint = searchHint;
            return this;
        }

        public BottomSheetMapBuilder<V> setRecyclerViewLayoutManager(RecyclerView.LayoutManager layoutManager) {
            this.layoutManager = layoutManager;
            return this;
        }

        public BottomSheetMapBuilder<V> setDividerItemDecoration(DividerItemDecoration dividerItemDecoration) {
            this.dividerItemDecoration = dividerItemDecoration;
            return this;
        }

        public BottomSheetMapBuilder<V> setMapIrService(MapIrService mapIrService) {
            this.mapIrService = mapIrService;
            return this;
        }

        public BottomSheetMapBuilder<V> setSelectSearch(String select) {
            this.select = select;
            return this;
        }

        public BottomSheetMapBuilder<V> setFilterSearch(String filter) {
            this.filter = filter;
            return this;
        }

    }

}
