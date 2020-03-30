package com.example.nearbyimprovement.improvement;

import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.enums.TipoPacote;
import com.example.nearbyimprovement.interfaces.Receiver;
import com.example.nearbyimprovement.interfaces.Sender;

public abstract class ReqReplyObject extends PatternComunicationObject implements Receiver, Sender {

    public ReqReplyObject(Comportamento c) {
        super.comportamento = c;
    }

    @Override
    public void receive(byte[] dados, String endpointID) {

    }

    @Override
    public void send(byte[] dados, String endPointID) {
        nearbyAccessObject.send(endPointID, dados, TipoPacote.CONTENT);
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
}
