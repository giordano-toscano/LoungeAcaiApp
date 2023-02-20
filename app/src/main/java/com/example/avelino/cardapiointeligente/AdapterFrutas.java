package com.example.avelino.cardapiointeligente;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.avelino.cardapiointeligente.Model.Frutas;
import com.example.avelino.cardapiointeligente.Model.Tamanho;

import java.util.ArrayList;
import java.util.List;

public class AdapterFrutas extends ArrayAdapter<Frutas>{

    private final Context context;
    private final ArrayList<Frutas> frutasList;

    public AdapterFrutas(Context context, ArrayList<Frutas> frutasList) {
        super(context, R.layout.custom_layout_list_frutas,frutasList);
        this.context = context;
        this.frutasList = frutasList;
    }


    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.custom_layout_list_frutas, parent , false);

        final CheckBox rdbtn_listaTipos = (CheckBox) rowView.findViewById(R.id.chckbtn_listaFrutas);
        TextView preco_listaTipos = (TextView)rowView.findViewById(R.id.preco_listaFrutas);

        if( frutasList.get(position).isComprado() )
            rdbtn_listaTipos.setChecked(true);
        else
            rdbtn_listaTipos.setChecked(false);

        rdbtn_listaTipos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                float preco=0;
                if(b){
                    preco = frutasList.get(position).getValor();
                }else{
                    preco = (-1) * frutasList.get(position).getValor();
                }
                frutasList.get(position).setComprado(b);
                Preferences p = new Preferences(PreferenceManager.getDefaultSharedPreferences(context));
                p.salvarPreferencias("precoFrutas",preco);
            }
        });

        rdbtn_listaTipos.setText(frutasList.get(position).getNome());

        preco_listaTipos.setText(" R$" + String.format("%.2f",frutasList.get(position).getValor()) + "");


        return rowView;
    }

    public void restaurarLista(ArrayList<Frutas> frutas){
        this.frutasList.clear();
        for(Frutas f : frutas){
            f.setComprado(false);
        }
        this.frutasList.addAll(frutas);

        notifyDataSetChanged();
    }

}

