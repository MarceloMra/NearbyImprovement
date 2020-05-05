package com.example.nearbyimprovement.model;

import com.example.nearbyfenix.improvement.EndpointInfo;
import com.example.nearbyfenix.improvement.PublisherObject;
import com.example.nearbyimprovement.activities.MainActivity;

public class MyPublisherObject extends PublisherObject {
    private MainActivity mainActivity;

    public MyPublisherObject(MainActivity activity) {
        super();
        mainActivity = activity;
    }

    @Override
    protected void novaConexaoEfetuada(EndpointInfo endpointInfo) {
        mainActivity.liberarCamposEnvio();
        mainActivity.atualizarSpinIDsConectados();
        mainActivity.mostrarMensagemDeControleEmTela("Nova conexão com "+endpointInfo.getEndpointID()+"!");
    }

    @Override
    protected void conexaoEncerrada(EndpointInfo endpointInfo) {
        mainActivity.mostrarMensagemDeControleEmTela("Conexão com "+endpointInfo.getEndpointID()+" foi encerrada!");
        mainActivity.atualizarSpinIDsConectados();
    }

    @Override
    public void onSuccessStartAdvertising() {
        mainActivity.mostrarMensagemDeControleEmTela("Anunciamento iniciado!");
    }

    @Override
    public void onFeilureStartAdvertising(Exception e) {
        mainActivity.mostrarMensagemDeControleEmTela("Erro ao iniciar Descoberta: "+e.getMessage());
    }

    @Override
    public void onSuccessStartDiscovery() {
        mainActivity.mostrarMensagemDeControleEmTela("Descoberta iniciada!");
    }

    @Override
    public void onFeilureStartDiscovery(Exception e) {
        mainActivity.mostrarMensagemDeControleEmTela("Erro ao iniciar Descoberta: "+e.getMessage());
    }
}
