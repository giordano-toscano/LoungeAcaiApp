package com.example.avelino.cardapiointeligente.Model;

import java.util.ArrayList;

public class Acai {
	
	Sabor sabor;
	private ArrayList<Acompanhamento> acompanhamentos;
	private ArrayList<Frutas> frutas;
	Adicional adicional;
	Tamanho tam;
	
	public Acai () {
		
	}

	public Adicional getAdicional(){ return adicional; }

	public void setAdicional(Adicional adicional){ this.adicional = adicional; }
	
	public Sabor getSabor() {
		return sabor;
	}

	public void setSabor(Sabor sabor) {
		this.sabor = sabor;
	}
	
	public void setTam(Tamanho tam) {
		this.tam = tam;
	}

	public Tamanho getTam() {
		return tam;
	}

	public ArrayList<Acompanhamento> getAcomp() {
		return acompanhamentos;
	}

	public void setAcomp(ArrayList<Acompanhamento> acompanhamentos) {
		this.acompanhamentos = acompanhamentos;
	}

	public ArrayList<Frutas> getFrutas() {
		return frutas;
	}

	public void setFrutas(ArrayList<Frutas> frutas) {
		this.frutas = frutas;
	}

}
