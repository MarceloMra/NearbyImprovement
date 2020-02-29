package com.example.nearbyimprovement.model;

import com.example.nearbyimprovement.enums.Comportamento;

import java.util.ArrayList;

public abstract class PatternComunicationObject {
    protected NearbyAccessObject nearbyAccessObject;
    protected Comportamento comportamento;
    private ArrayList<String> endpointIDsConnected;

    public ArrayList<String> getEndpointIDsConnected() {
        return endpointIDsConnected;
    }

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

    public void setNearbyAccessObject(NearbyAccessObject nearbyAccessObject) {
        this.nearbyAccessObject = nearbyAccessObject;
    }

    public void addNewEndpointID(String endpointID){
        endpointIDsConnected.add(endpointID);
        novaConexaoEfetuada(endpointID);
    }

    protected abstract void novaConexaoEfetuada(String endpointID);

    public void removeEndpointID(String endpointID){
        endpointIDsConnected.remove(endpointID);
    }
}
