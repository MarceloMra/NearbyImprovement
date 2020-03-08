package com.example.nearbyimprovement.model;

import com.example.nearbyimprovement.activities.MainActivity;
import com.example.nearbyimprovement.improvement.PublisherObject;

public class MyPublisherObject extends PublisherObject {
    private MainActivity mainActivity;

    public MyPublisherObject(MainActivity activity) {
        super();
        mainActivity = activity;
    }

    @Override
    protected void novaConexaoEfetuada(String endpointID) {
        mainActivity.liberarCamposEnvio();
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
