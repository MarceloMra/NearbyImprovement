package com.example.nearbyimprovement.model;

import com.example.nearbyimprovement.activities.MainActivity;
import com.example.nearbyimprovement.improvement.Services;
import com.example.nearbyimprovement.improvement.SubscriberObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MySubscriberObject extends SubscriberObject {
    private MainActivity mainActivity;

    public MySubscriberObject(MainActivity mainActivity) {
        super();
        this.mainActivity = mainActivity;
    }

    @Override
    public void onOkSubscription(String endpointID){
        mainActivity.mostrarMensagemDeControleEmTela("Subscrição realizada com sucesso para "+endpointID);
    }

    @Override
    public void onFailSubscription(String endpointID){
        mainActivity.mostrarMensagemDeControleEmTela("Falha ao tentar subscrever o serviço de "+endpointID);
    }

    @Override
    protected void novaConexaoEfetuada(String endpointID) {
        mainActivity.liberarCamposEnvio();
        mainActivity.atualizarSpinIDsConectados();
        mainActivity.mostrarMensagemDeControleEmTela("Nova conexão com "+endpointID+"!");
    }

    @Override
    protected void conexaoEncerrada(String endpointID) {
        mainActivity.mostrarMensagemDeControleEmTela("Conexão com "+endpointID+" foi encerrada!");
        mainActivity.atualizarSpinIDsConectados();
    }

    @Override
    public void receive(byte[] dados, String endpointID) {
        try {
            mainActivity.addNovaMensagem(new String(((byte[]) (Services.deserialize(dados))), StandardCharsets.UTF_8), endpointID, "Recebido");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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