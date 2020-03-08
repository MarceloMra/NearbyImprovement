package com.example.nearbyimprovement.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Mensagem {
    private String mensagem;
    private String hora;
    private SimpleDateFormat sdf;

    public Mensagem(String mensagem){
        sdf = new SimpleDateFormat("HH:mm");
        Date hora = Calendar.getInstance().getTime();
        this.mensagem = mensagem;
        this.hora = sdf.format(hora);
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getHora() {
        return hora;
    }

    public void setData(String data) {
        this.hora = data;
    }


}
