package com.atodogas.brainycar.OBD;

import java.io.IOException;
import java.util.ArrayList;


public class OBDMock extends OBDAdapter {
    private OBDDatabase obdDatabase;
    private ArrayList<OBDDTO> obdData;
    private int index;

    public OBDMock(OBDDatabase database){
        this.obdDatabase = database;
        this.index = 0;

        if(this.obdDatabase != null){
            this.obdData = this.obdDatabase.getAllData();
        }
    }

    @Override
    public OBDDTO getRealTimeData() throws IOException, InterruptedException {
        Thread.sleep(1000);
        OBDDTO obddto = new OBDDTO();

        if(obdData.size() > 0){
            obddto = obdData.get(index);

            index++;
            index = index % obdData.size();
        }

        return obddto;
    }

    @Override
    public boolean isConnected() throws IOException, InterruptedException {
        return true;
    }

    @Override
    public ArrayList<String> getTroubleCodes() throws IOException, InterruptedException {
        ArrayList<String> troubleCodes = new ArrayList<>();
        troubleCodes.add("P0123");
        troubleCodes.add("B0293");
        troubleCodes.add("C0323");
        troubleCodes.add("P0293");
        troubleCodes.add("B1103");

        return troubleCodes;
    }

    @Override
    public void close() {

    }
}
