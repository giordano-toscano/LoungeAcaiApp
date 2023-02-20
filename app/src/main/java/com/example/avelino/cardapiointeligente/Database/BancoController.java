package com.example.avelino.cardapiointeligente.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.avelino.cardapiointeligente.Model.Acompanhamento;
import com.example.avelino.cardapiointeligente.Model.Adicional;
import com.example.avelino.cardapiointeligente.Model.Frutas;
import com.example.avelino.cardapiointeligente.Model.Local;
import com.example.avelino.cardapiointeligente.Model.Pedido;
import com.example.avelino.cardapiointeligente.Model.Sabor;
import com.example.avelino.cardapiointeligente.Model.Tamanho;
import com.example.avelino.cardapiointeligente.Util.DateConvert;
import com.example.avelino.cardapiointeligente.Util.ParserOrder;

import java.util.Date;

public class BancoController {

    private SQLiteDatabase db;
    private CreateDb banco;

    public BancoController(Context context){
        banco = new CreateDb(context);
    }

    /**Parametro data deve ser no tipo: dd/MM/aaaa */
    @Deprecated
    public Cursor carregaDadoByData(String data){
        Cursor cursor;
        String where = "data_pedido" + "=" + data;
        db = banco.getReadableDatabase();
        cursor = db.query("historico",null,null, null, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public boolean inserirPedidoNoBanco(Pedido p){
        ContentValues valores = new ContentValues();
        /**Salvando data**/
        valores.put("data_pedido",DateConvert.toPtBr(p.getData()));
        /**Pedido local ou viagem*/
        valores.put(p.getLocal().toString().toLowerCase(),1);
        /**Tamanho + Formas de Adoçar*/
        if(p.getAcai().getAdicional()!=null){
            valores.put(ParserOrder.removerAcentos(p.getAcai().getTam().getDescricao().replaceAll(" ","").replaceAll("ç","c").toLowerCase()+"_"+p.getAcai().getAdicional().getDescricao().replaceAll(" ","").replaceAll("ç","c").toLowerCase()),1);
        }
        /**Tamanho + Sabor*/
        valores.put(ParserOrder.removerAcentos(p.getAcai().getTam().getDescricao().replaceAll(" ","").replaceAll("ç","c").toLowerCase()+"_"+p.getAcai().getSabor().getDescricao().replaceAll(" ","").replaceAll("ç","c").toLowerCase()),1);
        /**Acompanhamentos*/
        for (Acompanhamento a: p.getAcai().getAcomp()) {
            valores.put(ParserOrder.removerAcentos(a.getDescricao().replaceAll(" ","").replaceAll("ç","c").toLowerCase()),1);
        }
        /**Frutas adicionais*/
        for (Frutas f: p.getAcai().getFrutas()) {
            valores.put(ParserOrder.removerAcentos(f.getNome().replaceAll(" ","").replaceAll("ç","c").toLowerCase()),1);
        }
        /**Valor Total*/
        p.CalcularPedido();
        valores.put("valor_pedido",p.getTotal());

        db = banco.getWritableDatabase();
        long resultado = db.insert("historico", null, valores);
        db.close();
        if (resultado ==-1) {
            return false;
        }else {
            return true;
        }
    }

    /**Parametro "data" deve estar em formato pt-br: dd/MM/aaaa (encontrado em classe: @Class: DateConvert)*/
    public int getSomaPorColunaPorData(String column, String data){
        int resultado = 0;
        db = banco.getReadableDatabase();
        Cursor c = db.rawQuery("select sum("+column+") from historico WHERE data_pedido LIKE '"+data+"';", null);
        if(c.moveToFirst())
            resultado = c.getInt(0);
        c.close();
        return resultado;
    }

    /**Parametro "data" deve estar em formato pt-br: dd/MM/aaaa (encontrado em classe: @Class: DateConvert)*/
    public int getCountPorData(String data){
        int resultado = 0;
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM historico WHERE data_pedido LIKE '"+data+"';", null);
        if(cur.moveToFirst()){
            resultado = cur.getInt(0);
        }
        return resultado;
    }

    public void deletaRegistroDoDiaAnterior(){
        String dataHoje = DateConvert.toPtBr(new Date());
        String where = "data_pedido" + " NOT LIKE '" + dataHoje+"';";
        db = banco.getWritableDatabase();
        boolean deleted = db.delete("historico",where,null) > 0;
        db.close();
    }
}