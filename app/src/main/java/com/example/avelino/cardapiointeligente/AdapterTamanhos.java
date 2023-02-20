package com.example.avelino.cardapiointeligente;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avelino.cardapiointeligente.Model.Frutas;
import com.example.avelino.cardapiointeligente.Model.Sabor;
import com.example.avelino.cardapiointeligente.Model.Tamanho;

import java.util.ArrayList;

public class AdapterTamanhos extends ArrayAdapter<Tamanho> {

    private final Context context;
    private  ArrayList<Tamanho> tamanhosList;

    float preco = 0;

    public AdapterTamanhos(Context context, ArrayList<Tamanho> tamanhosList) {
        super(context, R.layout.custom_layout_list_tamanho,tamanhosList);
        this.context = context;
        this.tamanhosList = tamanhosList;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.custom_layout_list_tamanho, parent , false);

        final RadioButton rdbtn_listaTamanhos = (RadioButton) rowView.findViewById(R.id.rdbtn_listaTamanhos);
        TextView preco_listaTamanhos = (TextView)rowView.findViewById(R.id.preco_listaTamanhos);

        rdbtn_listaTamanhos.setText(tamanhosList.get(position).getDescricao());

        rdbtn_listaTamanhos.setChecked(tamanhosList.get(position).isChecado());

        if(tamanhosList.get(position).getValor()==0){
            preco_listaTamanhos.setText("");
        }else {
            preco_listaTamanhos.setText(" R$" + String.format("%.2f",tamanhosList.get(position).getValor()) + "");
        }

        rdbtn_listaTamanhos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Preferences p = new Preferences(PreferenceManager.getDefaultSharedPreferences(context));

                setarLista(tamanhosList);
                tamanhosList.get(position).setChecado(b);

                p.salvarPreferencias("precoTamanhos",(float)(tamanhosList.get(position).getValor()));

            }
        });

        return rowView;
    }

    public void setarLista(ArrayList<Tamanho> tamanhosList){

        for (int i = 0; i <tamanhosList.size() ; i++) {
            tamanhosList.get(i).setChecado(false);
        }

    }

    public void restaurarLista(ArrayList<Tamanho> tamanhos){
        this.tamanhosList.clear();
        for(Tamanho t : tamanhos){
            t.setChecado(false);
        }
        this.tamanhosList.addAll(tamanhos);

        notifyDataSetChanged();
    }

}