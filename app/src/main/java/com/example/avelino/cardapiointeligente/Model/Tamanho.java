package com.example.avelino.cardapiointeligente.Model;

public enum Tamanho {

	PP("PP",7.5),
	P("P",10.5),
	M("M",13.5),
	G("G",16.5),
	GG("GG",22.5);

	private double valor;
	private boolean checado;
	private String descricao;
		
	Tamanho( String descricao, double valor){
		this.descricao = descricao;
		this.valor = valor;
		this.checado = false;
	}

	public String getDescricao() {
		return descricao;
	}

	public double getValor(){
		return valor; 
	}

	public void setChecado(boolean checado) {
		this.checado = checado;
	}

	public boolean isChecado() {
		return checado;
	}
	
}
