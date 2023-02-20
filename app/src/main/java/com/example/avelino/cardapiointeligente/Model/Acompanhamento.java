package com.example.avelino.cardapiointeligente.Model;

import java.util.Comparator;

public enum Acompanhamento {
	LEITE_CONDENSADO("Leite Condensado", (float)1.5 ),
	LEITE_PO("Leite em Pó",(float)1.5),
	FARINHA_AMENDOIM("Farinha de Amendoim",(float)1.5),
	//FLOCOS_ARROZ("Flocos de Arroz",(float)1.5),
	AMENDOIM_T("Amendoim Triturado",(float)1.5),
	BOLINHA_NESCAU("Bolinhas de Nescau",(float)1.5),
	AMENDOIM("Amendoim",(float)1.5),
	BANANA("Banana",(float)1.5),
	MEL("Mel",(float)1.5),
	FARINHA_LACTEA("Farinha Láctea",(float)1.5),
	FARINHA_CASTANHA("Farinha de Castanha",(float)1.5),
	AVEIA("Aveia",(float)1.5),
	GRANOLA("Granola",(float)1.5),
	SUCRILHOS("Sucrilhos",(float)1.5);

	private float valor;
	private String descricao;
	private boolean comprado;

	public boolean isComprado() {
		return comprado;
	}

	public void setComprado(boolean comprado) {
		this.comprado = comprado;
	}

	public String getDescricao() {
		return descricao;
	}

	public float getValor() {
		return valor;
	}

	private Acompanhamento( String descricao, float valor) {
		this.valor = valor;
		this.descricao = descricao;
		this.comprado = false;
	}
	
	
}
