package com.example.avelino.cardapiointeligente.Util;

import com.example.avelino.cardapiointeligente.Model.Acompanhamento;

import java.util.Comparator;

public class Comparador implements Comparator<Acompanhamento> {

    @Override
    public int compare(Acompanhamento objeto1, Acompanhamento objeto2){
        int retorno = 0;
        retorno = objeto1.getDescricao().compareTo(objeto2.getDescricao());
        return retorno;
    }
}
