package com.example.avelino.cardapiointeligente;

import android.content.SharedPreferences;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

public class Preferences extends AppCompatActivity{

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public Preferences(SharedPreferences preferences){
        this.preferences = preferences;
        this.editor = this.preferences.edit();
    }

    //Metodo para salvar String
    public void salvarPreferencias(String chave, Float valor){
        this.editor.putFloat(chave,valor);
        this.editor.commit();
    }

    public Float carregarPreferencias(String chave){
        return this.preferences.getFloat(chave,0);
    }

}
