package com.example.avelino.cardapiointeligente.Model;

public enum Sabor {
	
	ACAI("Açaí",0),
	METADE_METADE("Metade Cupuaçu e Metade Açaí",0),
	CUPUACU("Cupuacu",0);
	
	private String descricao;
	private float valor;
	private boolean checado;

	Sabor (String descricao, float valor) {
		this.setDescricao(descricao);
		this.setValor(valor);
		this.checado=false;
	}

	public boolean isChecado() {
		return checado;
	}

	public void setChecado(boolean checado) {
		this.checado = checado;
	}

	public String getDescricao() {
		return descricao;
	}

	private void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

}
