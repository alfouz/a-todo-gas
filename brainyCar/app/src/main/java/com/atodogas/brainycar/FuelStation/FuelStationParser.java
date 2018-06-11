package com.atodogas.brainycar.FuelStation;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FuelStationParser {
    public ArrayList<FuelStation> parserJson(JSONObject json){
        ArrayList<FuelStation> fuelStations = new ArrayList<>();

        try {
            JSONArray jsonArray = json.getJSONArray("ListaEESSPrecio");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject j = jsonArray.getJSONObject(i);

                FuelStation fuelStation = new FuelStation();

                if(j.has("Rótulo")){
                    fuelStation.setTitle(j.getString("Rótulo"));
                }
                else {
                    fuelStation.setTitle("Sin nombre");
                }

                fuelStation.setTime(j.getString("Horario"));
                fuelStation.setLatitude(Double.parseDouble(j.getString("Latitud").replace(",", ".")));
                fuelStation.setLongitude(Double.parseDouble(j.getString("Longitud (WGS84)").replace(",", ".")));

                if(j.getString("Precio Gasoleo A") != "null"){
                    fuelStation.setDieselA(Float.parseFloat(j.getString("Precio Gasoleo A").replace(",", ".")));
                }

                if(j.getString("Precio Gasolina 95 Protección") != "null"){
                    fuelStation.setGasoline95(Float.parseFloat(j.getString("Precio Gasolina 95 Protección").replace(",", ".")));
                }

                if(j.getString("Precio Gasolina  98") != "null"){
                    fuelStation.setGasoline98(Float.parseFloat(j.getString("Precio Gasolina  98").replace(",", ".")));
                }

                fuelStations.add(fuelStation);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return fuelStations;
    }
}
