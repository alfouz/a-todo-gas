package com.atodogas.brainycar.Utils;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by eladr on 28/05/2018.
 */

//Auxiliar class to get points coloured
public class ColoredPoint {
    public LatLng coords;
    public int color;

    public ColoredPoint(LatLng coords, int color) {
        this.coords = coords;
        this.color = color;
    }
}