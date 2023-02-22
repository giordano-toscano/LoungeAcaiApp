package com.example.giordano.cardapiointeligente.Model;

import java.util.Date;

public class Pedido {
	
	private String nome;
	private Date data;
	private Acai acai;
	private Local local;
	private double total;

	public Pedido (Acai acai) {
		this.acai = acai;
	}
	
	
	public Acai getAcai() {
		return acai;
	}
	
	public double getTotal() {		
		return this.total;
	}
	
	public String getNome() {
		return nome;
	}

	public Date getData() {
		return data;
	}	
	
	public Local getLocal() {
		return local;
	}	
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setAcai(Acai acai) {
		this.acai = acai;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public void CalcularPedido() {
		double total = 0;

		total += this.local.equals(Local.VIAGEM)?1:0;

		if(this.acai.getAcomp()!= null)
			for(int i = 0; i<this.acai.getAcomp().size(); i++){
				if(i>=4){
					total += 1.5;
				}
			}

		if(this.acai.getFrutas()!=null)
			for(Frutas fruta: this.acai.getFrutas())
				total += fruta.getValor();

		if(this.acai.getAdicional()!=null)
			total += this.acai.adicional.getValor();

		if(this.acai.tam != null)
			total += this.acai.getTam().getValor();
		if(this.acai.sabor != null)
			total +=this.acai.sabor.getValor();

			this.total = total;
	}


}
