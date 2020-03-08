package com.example.nearbyimprovement.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbyimprovement.R;

import java.util.ArrayList;

public class AdapterMensagens extends RecyclerView.Adapter<AdapterMensagens.ViewHolderMensagens> {
    private ArrayList<Mensagem> mensagens;

    @Override
    public ViewHolderMensagens onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemmsg, parent, false);
        ViewHolderMensagens holder = new ViewHolderMensagens(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderMensagens holder, int position) {
        holder.txtHora.setText(mensagens.get(position).getHora());
        holder.txtMensagem.setText(mensagens.get(position).getMensagem());
    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }


    public class ViewHolderMensagens extends RecyclerView.ViewHolder {
        TextView txtMensagem;
        TextView txtHora;
        RelativeLayout itemmsgLayout;

        public ViewHolderMensagens(View itemView) {
            super(itemView);
            txtMensagem = (TextView)itemView.findViewById(R.id.mensagem);
            txtHora = (TextView)itemView.findViewById(R.id.hora);
            itemmsgLayout = (RelativeLayout)itemView.findViewById(R.id.itemmsg);
        }

        public TextView getTxtMensagem() {
            return txtMensagem;
        }

        public TextView getTxtHora() {
            return txtHora;
        }
    }


    public AdapterMensagens(ArrayList<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }


}
