package com.example.nearbyimprovement.model;

import com.example.nearbyimprovement.enums.Comportamento;
import com.example.nearbyimprovement.interfaces.Sender;

public class PublisherObject extends PatternComunicationObject implements Sender{

    public PublisherObject() {
        super.comportamento = Comportamento.PUBLISHER;
    }

    @Override
    public void send(byte[] dados, String endPointID) {

    }

    @Override
    public Comportamento getComportamento() {
        return null;
    }
}
