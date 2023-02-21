package com.samiei.globalmap.Interfaces;

import org.osmdroid.util.GeoPoint;

public interface IMapClickEvents {
    void onMarkSingleTap(int index,Object object);
    void onMarkLongTap(int index,Object object);
    void onOtherItemsClick();
    void onOtherItemsClick(GeoPoint point);
}
