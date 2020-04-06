package com.example.nearbyimprovement.improvement;

import com.example.nearbyimprovement.interfaces.Receiver;

public abstract class SyncObject extends PatternComunicationObject implements Receiver {

    public abstract void onComunicacaoDeConclusaoRecebida(String endpointID);
}
