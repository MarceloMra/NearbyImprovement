package com.example.nearbyimprovement.model;

import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.interfaces.Receiver;

public class SubscriberObject extends PatternComunicationObject implements Receiver {


    public SubscriberObject() {
        super.comportamento = Comportamento.SUBSCRIBER;
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
    public void receive(byte[] dados, String endpointID) {

    }

}
