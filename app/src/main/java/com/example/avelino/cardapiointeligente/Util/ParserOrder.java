package com.example.avelino.cardapiointeligente.Util;

import com.example.avelino.cardapiointeligente.Model.Acompanhamento;
import com.example.avelino.cardapiointeligente.Model.Frutas;
import com.example.avelino.cardapiointeligente.Model.Pedido;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ParserOrder {

    private int numero;
    private String str;

    public ParserOrder(int numero) {
        this.numero = numero;
    }

    public String orderParser(Pedido pedido) {

        numero = 1 + numero;
        str += "\n";
        str += "LOUNGE DO ACAI";
        str += "\n";
        str = "PEDIDO: " + numero + "\n";
        str += "Data:" + dataParser(pedido) + "\n";
        str += "Consumo:" + pedido.getLocal().name() + "\n";
        //str += "Nome do Cliente:" + pedido.getNome().toString() + "\n";
        str += "Tamanho:" + pedido.getAcai().getTam().toString() + "\n";
        str += "Tipo do Acai:\n" + removerAcentos(pedido.getAcai().getSabor().getDescricao())+"\n";

        if(pedido.getAcai().getAdicional()!= null)
        str += "Formas de Adocar:\n" + removerAcentos(pedido.getAcai().getAdicional().getDescricao())+"\n";

        if(pedido.getAcai().getFrutas().size()!=0)
        str += "Frutas Adicionais: \n" + frutasParser(pedido);

        if(pedido.getAcai().getAcomp().size()!=0)
        str += "Acompanhamentos:\n" + removerAcentos(acompanhamentosParser(pedido));

//        str += "\nTotal: " + "R$" + String.format("%.2f",pedido.getTotal()) + "\n\n\n"+" "+"\n";
        str += "\n\n\n\n";
        return str;
    }

    public String acompanhamentosParser(Pedido pedido){
        String string="";
        ArrayList<Acompanhamento> acompanhamentoArrayList = pedido.getAcai().getAcomp();
        int acompanhamentos=0;

        for (int i = 0; i < acompanhamentoArrayList.size(); i++) {
            if(acompanhamentoArrayList.get(i).isComprado()){
                string+="-"+acompanhamentoArrayList.get(i).getDescricao()+";";
                string+="\n";
                acompanhamentos++;
            }

        }
        if(acompanhamentos==14)
            return "- Todos\n";
        else
            return string;

    }

    public String frutasParser(Pedido pedido){
        String string="";
        ArrayList<Frutas> frutasArrayList = pedido.getAcai().getFrutas();

        for (int i = 0; i < frutasArrayList.size(); i++) {
            if(frutasArrayList.get(i).isComprado()){
                string+="-"+frutasArrayList.get(i).getNome()+";";
                string+="\n";
            }
        }

        return string;

    }

    public String dataParser(Pedido pedido){
        Date data = pedido.getData();

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        return formato.format(data);

    }

    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }



}
