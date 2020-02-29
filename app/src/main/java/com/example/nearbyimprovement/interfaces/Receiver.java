package com.example.nearbyimprovement.interfaces;

import com.google.android.gms.nearby.connection.Payload;

public interface Receiver {
    public void receive(Payload p);
}
