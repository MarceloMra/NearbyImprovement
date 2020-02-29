package com.example.nearbyimprovement.model;

import com.example.nearbyimprovement.enums.Comportamento;

import java.util.ArrayList;

public abstract class PatternComunicationObject {
    protected NearbyAccessObject nearbyAccessObject;
    protected Comportamento comportamento;
    private ArrayList<String> endpointIDsConnected;

    public PatternComunicationObject() {
        endpointIDsConnected = new ArrayList<>();
    }

    public Comportamento getComportamento() {
        return comportamento;
    }

    public void startAdvertising(){
        nearbyAccessObject.startAdvertising();
    }
    public void startDiscovery(){
        nearbyAccessObject.startDiscovery();
    }

    public void addNewEndpointID(String endpointID){
        endpointIDsConnected.add(endpointID);
        novaConexaoEfetuada(endpointID);
    }

    protected abstract void novaConexaoEfetuada(String endpointID);
    public abstract void onSuccessStartAdvertising();
    public abstract void onFeilureStartAdvertising(Exception e);
    public abstract void onSuccessStartDiscovery();
    public abstract void onFeilureStartDiscovery(Exception e);

    public void removeEndpointID(String endpointID){
        endpointIDsConnected.remove(endpointID);
    }

    public ArrayList<String> getEndpointIDsConnected() {
        return endpointIDsConnected;
    }

    public void setNearbyAccessObject(NearbyAccessObject nearbyAccessObject) {
        this.nearbyAccessObject = nearbyAccessObject;
    }
}
