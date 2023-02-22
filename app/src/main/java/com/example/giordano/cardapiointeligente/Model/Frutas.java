package com.example.giordano.cardapiointeligente.Model;

public enum Frutas {
    MORANGO("Morango",3),
    MANGA("Manga",3),
    KIWI("Kiwi",3);

    private float valor;
    private String nome;
    private boolean comprado;

    Frutas(String nome, float valor){
        this.valor=valor;
        this.nome=nome;
        this.comprado = false;
    }

    public String getNome(){
        return nome;
    }

    public float getValor(){
        return valor;
    }

    public boolean isComprado() {
        return comprado;
    }

    public void setComprado(boolean comprado) {
        this.comprado = comprado;
    }
}
