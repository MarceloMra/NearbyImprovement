package com.example.nearbyimprovement.improvement;

import com.example.nearbyimprovement.enums.Comportamento;

public class EndpointInfo {
    private String endpointID;
    private Comportamento comportamento;

    public EndpointInfo(String endpointID, Comportamento comportamento) {
        this.endpointID = endpointID;
        this.comportamento = comportamento;
    }

    public String getEndpointID() {
        return endpointID;
    }

    public void setEndpointID(String endpointID) {
        this.endpointID = endpointID;
    }

    public Comportamento getComportamento() {
        return comportamento;
    }

    public void setComportamento(Comportamento comportamento) {
        this.comportamento = comportamento;
    }
}
