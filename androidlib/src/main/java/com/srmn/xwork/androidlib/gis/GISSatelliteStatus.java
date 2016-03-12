package com.srmn.xwork.androidlib.gis;

import java.io.Serializable;

/**
 * Created by kiler on 2016/2/16.
 */
public class GISSatelliteStatus implements Serializable {
    private int connnectSatellites = 0;
    private int findSatellites = 0;

    public int getConnnectSatellites() {
        return connnectSatellites;
    }

    public void setConnnectSatellites(int connnectSatellites) {
        this.connnectSatellites = connnectSatellites;
    }

    public int getFindSatellites() {
        return findSatellites;
    }

    public void setFindSatellites(int findSatellites) {
        this.findSatellites = findSatellites;
    }
}
