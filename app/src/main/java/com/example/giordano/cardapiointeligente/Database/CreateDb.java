package com.example.giordano.cardapiointeligente.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.support.annotation.Nullable;
import com.example.giordano.cardapiointeligente.Model.Acompanhamento;
import com.example.giordano.cardapiointeligente.Model.Adicional;
import com.example.giordano.cardapiointeligente.Model.Frutas;
import com.example.giordano.cardapiointeligente.Model.Local;
import com.example.giordano.cardapiointeligente.Model.Sabor;
import com.example.giordano.cardapiointeligente.Model.Tamanho;
import com.example.giordano.cardapiointeligente.Util.ParserOrder;

public class CreateDb extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "lounge_acai.db";
    private static final String TABELA = "historico";
    private static final int VERSAO = 1;

    public CreateDb(Context context){
        super(context, NOME_BANCO,null,VERSAO);
    }

// ESTRUTURA TABELA
// ~PRIMARY OPTIONS~
// ID / DATA
// ~TAMANHO + FORMA DE ADOÇAR~
// P+XAROPE / P+SEM_ACUCAR / P+MEL / P+MASCAVO / P+DEMERARA / P+XILITOL / M+XAROPE / M+SEM_ACUCAR / M+MEL /
// M+MASCAVO / M+DEMERARA / M+XILITOL / G+XAROPE / G+SEM_ACUCAR / G+MEL / G+MASCAVO / G+DEMERARA / G+XILITOL / GG + XAROPE
// GG+SEM_ACUCAR / GG+MEL / GG+MASCAVO / GG+DEMERARA / GG+XILITOL
// ~TAMANHO + SABOR~
// P+AÇAI / P+CUPUACU / P+METADE-METADE / M+AÇAI / M+CUPUACU / M+METADE-METADE / G+ACAI / G+CUPUACU /
// G+METADE-METADE / GG+ACAI / GG+CUPUACU / GG+METADE-METADE
// ~QTD VIAGEM~
// LOCAL / VIAGEM
// ~ACOMPANHAMENTOS~
// LEITE_CONDENSADO / LEITE_PO / FARINHA_AMENDOIM / AMENDOIM_TRITURADO / BOLINHA_NESCAU / AMENDOIM / BANANA /
// MEL / FARINHA_LACTEA / FARINHA_CASTANHA / AVEIA / GRANOLA / SUCRILHOS
// VALOR MEDIO / VALOR TOTAL
    @Override
    public void onCreate(SQLiteDatabase db) {
        String auxSql = "CREATE TABLE "
                +TABELA
                +"("
                + "id" + " INTEGER primary key autoincrement,"
                + "data_pedido" + " TEXT,";
        for (Tamanho t: Tamanho.values()) {
            for (Adicional a: Adicional.values()) {
                auxSql += ParserOrder.removerAcentos(t.getDescricao().replaceAll(" ","").replaceAll("ç","c").toLowerCase()+"_"+a.getDescricao().replaceAll(" ","").replaceAll("ç","c").toLowerCase()) +" INTEGER DEFAULT 0,";
            }
        }
        for (Tamanho t: Tamanho.values()) {
            for (Sabor s: Sabor.values()) {
                auxSql += ParserOrder.removerAcentos(t.getDescricao().replaceAll(" ","").replaceAll("ç","c").toLowerCase()+"_"+s.getDescricao().replaceAll(" ","").replaceAll("ç","c").toLowerCase())+" INTEGER DEFAULT 0,";
            }
        }
        auxSql += Local.LOCAL.toString().toLowerCase()+ " INTEGER DEFAULT 0,";
        auxSql += Local.VIAGEM.toString().toLowerCase()+ " INTEGER DEFAULT 0,";
        for (Acompanhamento a: Acompanhamento.values()){
            auxSql += ParserOrder.removerAcentos(a.getDescricao().replaceAll(" ","").replaceAll("ç","c").toLowerCase())+" INTEGER DEFAULT 0,";
        }
        for (Frutas f: Frutas.values()){
            auxSql += ParserOrder.removerAcentos(f.getNome().replaceAll(" ","").replaceAll("ç","c").toLowerCase())+" INTEGER DEFAULT 0,";
        }
        String sql = auxSql
                + "valor_pedido" +" REAL"
                + ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA);
        onCreate(db);
    }
}
