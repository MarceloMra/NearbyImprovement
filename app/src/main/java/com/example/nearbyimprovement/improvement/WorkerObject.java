package com.example.nearbyimprovement.improvement;

import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.interfaces.Receiver;
import com.example.nearbyimprovement.interfaces.Sender;

public class WorkerObject extends PatternComunicationObject implements Sender, Receiver {

    public WorkerObject() {
        super();
        super.comportamento = Comportamento.WORKER;
    }

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

    @Override
    public void send(byte[] dados, String endPointID) {

    }
}
