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
        mainActivity.atualizarSpinIDsConectados();
    }

    @Override
    protected void conexaoEncerrada(String endpointID) {
        mainActivity.mostrarMensagemDeControleEmTela("Conexão com "+endpointID+" foi encerrada!");
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
