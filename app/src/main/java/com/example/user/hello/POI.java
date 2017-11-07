package com.example.user.hello;

import com.skp.Tmap.TMapPOIItem;

/**
 * Created by user on 2017-08-21.
 */

public class POI {
    TMapPOIItem item;

    public POI(TMapPOIItem item){
        this.item = item;
    }

    @Override
    public String toString() {
        return item.getPOIName();
    }



}
