package com.example.avelino.cardapiointeligente.Service;

import com.example.avelino.cardapiointeligente.Model.Acai;
import com.example.avelino.cardapiointeligente.Model.Acompanhamento;
import com.example.avelino.cardapiointeligente.Model.Adicional;
import com.example.avelino.cardapiointeligente.Model.Frutas;
import com.example.avelino.cardapiointeligente.Model.Sabor;
import com.example.avelino.cardapiointeligente.Model.Tamanho;

import java.util.ArrayList;

/**
 * Created by mathe on 11/10/2018.
 */

public class CardapioService {



    public Acai gerarAcai(ArrayList<Tamanho> tamanhoArrayList, ArrayList<Adicional> formasAdocarArrayList,ArrayList<Sabor> saborArrayList, ArrayList<Acompanhamento> acompanhamentoLista1ArrayList,ArrayList<Acompanhamento> acompanhamentoLista2ArrayList,ArrayList<Frutas> frutasArrayList ){
        Acai acai = new Acai();
        acai.setSabor(montarSabor(saborArrayList));
        acai.setTam(montarTamanho(tamanhoArrayList));
        acai.setAdicional(montarFormasAdocar(formasAdocarArrayList));
        acai.setAcomp(montarAcompanhamentos(acompanhamentoLista1ArrayList,acompanhamentoLista2ArrayList));
        acai.setFrutas(montarArrayFrutas(frutasArrayList));
        return acai;
    }

    public Tamanho montarTamanho(ArrayList<Tamanho> tamanhoArrayList){
        Tamanho tamanho = null;
        for(Tamanho t : tamanhoArrayList){
            if(t.isChecado()){
                tamanho = t;
            }
        }
        return tamanho;
    }

    public Adicional montarFormasAdocar(ArrayList<Adicional> formasAdocarArrayList) {
        Adicional adicional = null;
        for(Adicional a : formasAdocarArrayList){
            if(a.isComprado()){
                adicional = a;
            }
        }
        return adicional;
    }

    public Sabor montarSabor(ArrayList<Sabor> saborArrayList){
        Sabor sabor = null;
        for(Sabor s : saborArrayList){
            if(s.isChecado()){
                sabor = s;
            }
        }
        return sabor;
    }

    public ArrayList<Acompanhamento> montarAcompanhamentos(ArrayList<Acompanhamento> acompanhamentoLista1ArrayList, ArrayList<Acompanhamento> acompanhamentoLista2ArrayList){

        ArrayList<Acompanhamento> comprados = new ArrayList<Acompanhamento>();
        for(Acompanhamento acompanhamento : acompanhamentoLista1ArrayList){
            if(acompanhamento.isComprado()){
                comprados.add(acompanhamento);
            }
        }
        for(Acompanhamento acompanhamento : acompanhamentoLista2ArrayList) {
            if (acompanhamento.isComprado()) {
                comprados.add(acompanhamento);
            }
        }
        return comprados;
    }

    public ArrayList<Frutas> montarArrayFrutas (ArrayList<Frutas> frutasArrayList){
        ArrayList<Frutas> comprados = new ArrayList<Frutas>();

       for(Frutas frutas : frutasArrayList){
           if(frutas.isComprado()){
               comprados.add(frutas);
           }
       }

        return comprados;
    }

}
