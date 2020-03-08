package com.example.nearbyimprovement.improvement;

import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.interfaces.Receiver;

public class SubscriberObject extends PatternComunicationObject implements Receiver {


    public SubscriberObject() {
        super.comportamento = Comportamento.SUBSCRIBER;
    }

    public void subscrever(String endpointID){
        nearbyAccessObject.comunicacaoSubscricao(endpointID, "-@-subscribe-@-");
    }


    public void onOkSubscription(String endpointID){

    }

    public void onFailSubscription(String endpointID){

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
