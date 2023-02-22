package com.example.giordano.cardapiointeligente;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.giordano.cardapiointeligente.Model.Adicional;

import java.util.ArrayList;

public class AdapterAdicionais extends ArrayAdapter<Adicional> {

    private final Context context;
    private final ArrayList<Adicional> adicionaisList;

    float preco = 0;

    public AdapterAdicionais(Context context, ArrayList<Adicional> adicionaisList) {
        super(context, R.layout.custom_layout_list_adicional,adicionaisList);
        this.context = context;
        this.adicionaisList = adicionaisList;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.custom_layout_list_adicional, parent , false);

        final CheckBox rdbtn_listaAdicionais = (CheckBox) rowView.findViewById(R.id.rdbtn_listaAdicional);
        TextView preco_listaAdicionais = (TextView)rowView.findViewById(R.id.preco_listaAdicional);


        rdbtn_listaAdicionais.setText(adicionaisList.get(position).getDescricao());

        if(adicionaisList.get(position).isComprado() )
            rdbtn_listaAdicionais.setChecked(true);
        else
            rdbtn_listaAdicionais.setChecked(false);

        if(adicionaisList.get(position).getValor()==0){
            preco_listaAdicionais.setText("");
        }else {
            preco_listaAdicionais.setText(" R$" + String.format("%.2f",adicionaisList.get(position).getValor()) + "");
        }

        rdbtn_listaAdicionais.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                Preferences p = new Preferences(PreferenceManager.getDefaultSharedPreferences(context));

                setarLista(adicionaisList);
                adicionaisList.get(position).setComprado(b);

                p.salvarPreferencias("precoAdicional",adicionaisList.get(position).getValor());

            }
        });

        return rowView;

    }

    public void restaurarLista(ArrayList<Adicional> adicionals){
        this.adicionaisList.clear();
        for(Adicional s : adicionals){
            s.setComprado(false);
        }
        this.adicionaisList.addAll(adicionals);

        notifyDataSetChanged();
    }

    public void restaurarLista(){

        for(Adicional s : adicionaisList){
            s.setComprado(false);
        }

        notifyDataSetChanged();
    }

    public void setarLista(ArrayList<Adicional> saboresList){

        for (int i = 0; i <saboresList.size() ; i++) {
            saboresList.get(i).setComprado(false);
        }

    }

}
