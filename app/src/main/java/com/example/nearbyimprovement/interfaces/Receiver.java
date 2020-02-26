package com.example.nearbyimprovement.interfaces;

import com.example.nearbyimprovement.enums.Comportamento;
import com.google.android.gms.nearby.connection.Payload;

public interface Receiver {
    public Payload receive();
    public Comportamento getComportamento();
}
