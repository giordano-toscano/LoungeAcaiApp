package com.example.avelino.cardapiointeligente.Model;

public enum Adicional {
    XAROPE("Xarope",0),
    SEM_ACUCAR("Sem Açúcar",1),
    MEL("Mel",3),
    MASCAVO("Mascavo",4),
    CRISTAL("Cristal",2),
    DEMERARA("Demerara",2),
    XILITOL("Xilitol",5),
    STEVIA("Stevia",5);



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

    private Adicional( String descricao, float valor) {
        this.valor = valor;
        this.descricao = descricao;
        this.comprado = false;
    }

    @Override
    public String toString() {
        return "Adicional{" +
                "valor=" + valor +
                ", descricao='" + descricao + '\'' +
                ", comprado=" + comprado +
                '}';
    }
}
