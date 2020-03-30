package com.example.nearbyimprovement.improvement;

import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.enums.TipoPacote;
import com.example.nearbyimprovement.interfaces.Sender;

import java.util.ArrayList;

public abstract class PublisherObject extends PatternComunicationObject implements Sender{
    private ArrayList<String> endpointIDsSubscribed;

    public PublisherObject() {
        super();
        super.comportamento = Comportamento.PUBLISHER;
        endpointIDsSubscribed = new ArrayList<>();
    }

    public void addNovaSubscricao(String endpointID){
        if(!endpointIDsSubscribed.contains(endpointID)){
            endpointIDsSubscribed.add(endpointID);
        }
    }

    public void removeSubscricao(String endpointID){
        if(endpointIDsSubscribed.contains(endpointID)){
            endpointIDsSubscribed.remove(endpointID);
        }
    }

    @Override
    protected void novaConexaoEfetuada(EndpointInfo endpointInfo) {

    }

    @Override
    protected void conexaoEncerrada(EndpointInfo endpointInfo) {

    }

    @Override
    public void onSuccessStartAdvertising() {

    }

    @Override
    public void onFeilureStartAdvertising(Exception e) {

    }

    @Override
    public void onSuccessStartDiscovery() {

    }

    @Override
    public void onFeilureStartDiscovery(Exception e) {

    }

    @Override
    public void send(byte[] dados, String endPointID) {
        for(String endpoint : endpointIDsSubscribed){
            nearbyAccessObject.send(endpoint, dados, TipoPacote.CONTENT);
        }
    }
}
