package com.example.nearbyimprovement.improvement;

import com.example.nearbyimprovement.interfaces.Receiver;

public class SyncObject extends PatternComunicationObject implements Receiver {

    public void onComunicacaoDeConclusaoRecebida(String endpointID){

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
    public void receive(byte[] dados, String endpointID) {

    }
}
