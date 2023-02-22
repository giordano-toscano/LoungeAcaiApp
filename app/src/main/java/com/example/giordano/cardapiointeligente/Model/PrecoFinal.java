package com.example.giordano.cardapiointeligente.Model;

public class PrecoFinal {
    private float precoEmbalagem;
    private float precoSabores;
    private float precoFrutas;
    private float precoAcompanhamentos;
    private float precoTamanho;
    private float precoLocal;
    private float precoAdicional;

    private float precoTotal;

    public PrecoFinal (float precoEmbalagem, float precoSabores, float precoFrutas, float precoLocal, float precoAcompanhamentos, float precoTamanho, float precoAdicional){
        this.precoEmbalagem = precoEmbalagem;
        this.precoAcompanhamentos = precoAcompanhamentos;
        this.precoFrutas = precoFrutas;
        this.precoLocal = precoLocal;
        this.precoSabores = precoSabores;
        this.precoTamanho = precoTamanho;
        this.precoAdicional = precoAdicional;

        this.precoTotal = precoAcompanhamentos+precoFrutas+precoLocal+precoSabores+precoTamanho+precoAdicional;
    }

    public void notifyChange(){
        this.precoTotal = precoEmbalagem+precoAcompanhamentos+precoFrutas+precoLocal+precoSabores+precoTamanho+precoAdicional;
    }

    public float getPrecoEmbalagem() {
        return precoEmbalagem;
    }

    public void setPrecoEmbalagem(float precoEmbalagem) {
        this.precoEmbalagem = precoEmbalagem;
    }

    public void setPrecoTotal(float precoTotal) {
        this.precoTotal = precoTotal;
    }

    public float getPrecoAdicional() {
        return precoAdicional;
    }

    public void setPrecoAdicional(float precoAdicional) {
        this.precoAdicional = precoAdicional;
    }

    public float getPrecoTotal(){
        return precoTotal;
    }

    public float getPrecoSabores() {
        return precoSabores;
    }

    public void setPrecoSabores(float precoSabores) {
        this.precoSabores = precoSabores;
    }

    public float getPrecoFrutas() {
        return precoFrutas;
    }

    public void setPrecoFrutas(float precoFrutas) {
        this.precoFrutas = precoFrutas;
    }

    public float getPrecoAcompanhamentos() {
        return precoAcompanhamentos;
    }

    public void setPrecoAcompanhamentos(float precoAcompanhamentos) { this.precoAcompanhamentos = precoAcompanhamentos; }

    public float getPrecoTamanho() {
        return precoTamanho;
    }

    public void setPrecoTamanho(float precoTamanho) {
        this.precoTamanho = precoTamanho;
    }

    public float getPrecoLocal() {
        return precoLocal;
    }

    public void setPrecoLocal(float precoLocal) {
        this.precoLocal = precoLocal;
    }

}
