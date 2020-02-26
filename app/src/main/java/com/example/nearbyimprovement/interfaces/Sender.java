package com.example.nearbyimprovement.interfaces;

import com.example.nearbyimprovement.enums.Comportamento;

public interface Sender {
    public void send(byte[] dados, String endPointID);
    public Comportamento getComportamento();
}
