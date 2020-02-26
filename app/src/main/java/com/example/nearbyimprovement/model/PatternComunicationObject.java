package com.example.nearbyimprovement.model;

import com.example.nearbyimprovement.enums.Comportamento;

public abstract class PatternComunicationObject {
    protected NearbyAccessObject nearbyAccessObject;
    protected Comportamento comportamento;


    public void startAdvertising(){

    }
    public void startDiscovery(){

    }

    public void setNearbyAccessObject(NearbyAccessObject nearbyAccessObject) {
        this.nearbyAccessObject = nearbyAccessObject;
    }
}
