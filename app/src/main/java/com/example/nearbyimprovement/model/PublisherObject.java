package com.example.nearbyimprovement.model;

import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.interfaces.Sender;

public class PublisherObject extends PatternComunicationObject implements Sender{

    public PublisherObject() {
        super.comportamento = Comportamento.PUBLISHER;
    }

    @Override
    protected void novaConexaoEfetuada(String endpointID) {

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
        nearbyAccessObject.send(endPointID, dados);
    }
}
