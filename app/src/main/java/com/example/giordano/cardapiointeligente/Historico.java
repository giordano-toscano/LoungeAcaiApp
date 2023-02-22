package com.example.giordano.cardapiointeligente;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import com.example.giordano.cardapiointeligente.Database.BancoController;
import com.example.giordano.cardapiointeligente.Model.Acompanhamento;
import com.example.giordano.cardapiointeligente.Model.Adicional;
import com.example.giordano.cardapiointeligente.Model.Frutas;
import com.example.giordano.cardapiointeligente.Model.Sabor;
import com.example.giordano.cardapiointeligente.Model.Tamanho;
import com.example.giordano.cardapiointeligente.Util.DateConvert;
import com.example.giordano.cardapiointeligente.Util.ParserOrder;

import java.util.Date;

public class Historico extends AppCompatActivity {

    View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        BancoController bancoController = new BancoController(getApplicationContext());

        //Deixar o app fullscreen
        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        //Alterações na Action Bar//
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3A0152")));
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(5);

        /**Tamanho + Forma de Adoçar*/
        for (Tamanho t: Tamanho.values()) {
            for (Adicional a: Adicional.values()) {
                String uniaoId = ParserOrder.removerAcentos(t.getDescricao().replaceAll(" ","").replaceAll("ç","c").toLowerCase()+"_"+a.getDescricao().replaceAll(" ","").replaceAll("ç","c").toLowerCase());
                int resID = getResources().getIdentifier(uniaoId, "id", getPackageName());
                TextView txt = findViewById(resID);
                int valor = bancoController.getSomaPorColunaPorData(uniaoId, DateConvert.toPtBr(new Date()));
                txt.setText(""+valor);
                if(valor==0){
                    View item = findViewById(resID);
                    View parent = (View) item.getParent();
                    parent.setVisibility(View.GONE);
                }
            }
        }
        /**Tamanho + Sabor*/
        for (Tamanho t: Tamanho.values()) {
            for (Sabor s: Sabor.values()) {
                String uniaoId = ParserOrder.removerAcentos(t.getDescricao().replaceAll(" ","").replaceAll("ç","c").toLowerCase()+"_"+s.getDescricao().replaceAll(" ","").replaceAll("ç","c").toLowerCase());
                int resID = getResources().getIdentifier(uniaoId, "id", getPackageName());
                TextView txt = (TextView)findViewById(resID);
                int valor = bancoController.getSomaPorColunaPorData(uniaoId, DateConvert.toPtBr(new Date()));
                txt.setText(""+valor);
                if(valor==0){
                    View item = findViewById(resID);
                    View parent = (View) item.getParent();
                    parent.setVisibility(View.GONE);
                }
            }
        }
        /**Pedidos para viagem / local*/
        TextView txtViagem = findViewById(R.id.pedido_viagem);
        int valorViagem = bancoController.getSomaPorColunaPorData("viagem",DateConvert.toPtBr(new Date()));
        txtViagem.setText(""+valorViagem);
        TextView txtLocal = findViewById(R.id.pedido_local);
        int valorLocal = bancoController.getSomaPorColunaPorData("local",DateConvert.toPtBr(new Date()));
        txtLocal.setText(""+valorLocal);
        /**Pedidos por Acompanhamento*/
        for (Acompanhamento a: Acompanhamento.values()) {
            String uniaoId = ParserOrder.removerAcentos(a.getDescricao().replaceAll(" ","").replaceAll("ç","c").toLowerCase());
            int resID = getResources().getIdentifier(uniaoId, "id", getPackageName());
            TextView txt = (TextView)findViewById(resID);
            int valor = bancoController.getSomaPorColunaPorData(uniaoId,DateConvert.toPtBr(new Date()));
            txt.setText(""+valor);
            if(valor==0){
                View item = findViewById(resID);
                View parent = (View) item.getParent();
                parent.setVisibility(View.GONE);
            }
        }
        /**Pedidos por Frutas*/
        for (Frutas f: Frutas.values()) {
            String uniaoId = ParserOrder.removerAcentos(f.getNome().replaceAll(" ","").replaceAll("ç","c").toLowerCase());
            int resID = getResources().getIdentifier(uniaoId, "id", getPackageName());
            TextView txt = (TextView)findViewById(resID);
            int valor = bancoController.getSomaPorColunaPorData(uniaoId,DateConvert.toPtBr(new Date()));
            txt.setText(""+valor);
            if(valor==0){
                View item = findViewById(resID);
                View parent = (View) item.getParent();
                parent.setVisibility(View.GONE);
            }
        }
        /**Valor médio / total*/
        TextView txtValorMedio = (TextView)findViewById(R.id.valor_medio);
        double valorTotal = bancoController.getSomaPorColunaPorData("valor_pedido", DateConvert.toPtBr(new Date()));
        int totalPedidos = bancoController.getCountPorData(DateConvert.toPtBr(new Date()));
        double valorMedio = (totalPedidos>0)?(valorTotal/totalPedidos):0;
        txtValorMedio.setText(""+valorMedio);
        TextView txtValorTotal = (TextView)findViewById(R.id.valor_total);
        txtValorTotal.setText(""+valorTotal);
    }
}
