package com.example.avelino.cardapiointeligente;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.preference.PreferenceManager;

//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avelino.cardapiointeligente.Database.BancoController;
import com.example.avelino.cardapiointeligente.Model.Acai;
import com.example.avelino.cardapiointeligente.Model.Acompanhamento;
import com.example.avelino.cardapiointeligente.Model.Adicional;
import com.example.avelino.cardapiointeligente.Model.Frutas;
import com.example.avelino.cardapiointeligente.Model.Local;
import com.example.avelino.cardapiointeligente.Model.Pedido;
import com.example.avelino.cardapiointeligente.Model.PrecoFinal;
import com.example.avelino.cardapiointeligente.Model.Sabor;
import com.example.avelino.cardapiointeligente.Model.Tamanho;
import com.example.avelino.cardapiointeligente.Service.CardapioService;
import com.example.avelino.cardapiointeligente.Util.BluetoothPrinter;
import com.example.avelino.cardapiointeligente.Util.Comparador;
import com.example.avelino.cardapiointeligente.Util.ParserOrder;
import com.example.avelino.cardapiointeligente.View.ListaDinamica;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Cardapio extends AppCompatActivity {

    static int NUMERO_MINIMO_ACOMPANHAMENTO = 4;

    View decorView;
    ArrayList<Frutas> arrayFrutas;
    ArrayList<Sabor> arraySabores;
    ArrayList<Tamanho> arrayTamanhos;
    ArrayList<Adicional> arrayFormasAdocar;
    ArrayList<Acompanhamento> arrayAcompanhamentos1;
    ArrayList<Acompanhamento> arrayAcompanhamentos2;
    ListView listViewSabores;
    ListView listViewAcompanhamentos1;
    ListView listViewAcompanhamentos2;

    Button btn_confirmarPedido;

    //Componentes Local/Viagem
    RadioButton rdbtn_inLoco;
    RadioButton rdbtn_viagem;

    //Componentes Estaticos//
    RadioButton rdbtn_tamanhoP;
    RadioButton rdbtn_tamanhoM;
    RadioButton rdbtn_tamanhoG;
    RadioButton rdbtn_tamanhoGG;

    //Frutas
    CheckBox checkBox_Manga;
    CheckBox checkBox_Morango;
    CheckBox checkBox_Kiwi;
    //Formas de Adoçar
    CheckBox checkBox_Xarope;
    CheckBox checkBox_SemAcucar;
    CheckBox checkBox_Mel;
    CheckBox checkBox_Mascavo;
    CheckBox checkBox_Demerara;
    CheckBox checkBox_Cristal;
    CheckBox checkBox_Xilitol;
    CheckBox checkBox_Stevia;

    //Marcar todos os Acompanhamentos
    CheckBox rdbtn_MarcarTodos;

    Pedido pedido;
    Acai acai;
    PrecoFinal precoFinal = new PrecoFinal(0,0,0,0,0,0,0);
    int qtdClickHistorico;

    AdapterSabores adapterSabores;
    AdapterAcompanhamentos adapterAcompanhamentos1;
    AdapterAcompanhamentos adapterAcompanhamentos2;

    BluetoothPrinter btPrinter = new BluetoothPrinter();
    ParserOrder po = new ParserOrder(0);


    Intent startIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startIntent = getIntent();
        setContentView(R.layout.activity_cardapio);

        final BancoController bancoController = new BancoController(getApplicationContext());
        bancoController.deletaRegistroDoDiaAnterior();

        final CardapioService cardapioService = new CardapioService();
        final Preferences preferences = new Preferences(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));


        inicializarComponentes();

        qtdClickHistorico = 0;

       //Deixar o app fullscreen
        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        //Alterações na Action Bar//
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3A0152")));
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(5);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(Html.fromHtml("<b><big>Lounge do Açaí</big></b>"));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);

        //Impressora//
        conectarImpressora(getResources().getString(R.string.nome_impressora));


        montarCardapio();

        rdbtn_MarcarTodos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                for(Acompanhamento acompanhamento : arrayAcompanhamentos1){
                    acompanhamento.setComprado(b);
                }
                for(Acompanhamento acompanhamento : arrayAcompanhamentos2){
                    acompanhamento.setComprado(b);
                }
                adapterAcompanhamentos1.notifyDataSetChanged();
                adapterAcompanhamentos2.notifyDataSetChanged();
            }
        });

        btn_confirmarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                acai = cardapioService.gerarAcai(arrayTamanhos, arrayFormasAdocar, arraySabores, arrayAcompanhamentos1, arrayAcompanhamentos2, arrayFrutas);

                    RadioButton rdbtninLocal = findViewById(R.id.rdbtn_InLoco);
                    RadioButton rdbtnViagem = findViewById(R.id.rdbtn_Viagem);

                    if (acai.getTam() != null && acai.getSabor() != null && !( (acai.getSabor() == Sabor.ACAI || acai.getSabor() == Sabor.METADE_METADE) && acai.getAdicional() == null) && (rdbtninLocal.isChecked() || rdbtnViagem.isChecked())) {



                        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
                        Date data = new Date();

                        pedido = new Pedido(acai);
                        pedido.setData(new Date());

                        if (rdbtninLocal.isChecked()) {
                            pedido.setLocal(Local.LOCAL);
                        } else if (rdbtnViagem.isChecked()) {
                            pedido.setLocal(Local.VIAGEM);
                        }


                        pedido.CalcularPedido();
                        bancoController.inserirPedidoNoBanco(pedido);

                        try {

                            final String pedidoStringfy = po.orderParser(pedido);

                            btPrinter.sendData(pedidoStringfy);
                            Log.d("IMPRIMINDO",pedidoStringfy);

                            new java.util.Timer().schedule(
                                    new java.util.TimerTask() {
                                        @Override
                                        public void run() {
                                            try {
                                                btPrinter.sendData(pedidoStringfy);
                                                Log.d("IMPRIMINDO",pedidoStringfy);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    5000
                            );
                            //Log.d("IMPRIMINDO", po.orderParser(p));
                            alertDialogCustom(pedido);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else if ((acai.getSabor() == Sabor.ACAI || acai.getSabor() == Sabor.METADE_METADE) && acai.getAdicional() == null){
                        Toast toast = Toast.makeText(getApplicationContext(), Html.fromHtml("<b><big>" + "Escolha uma Forma de Adoçar" + "</big></b>"), Toast.LENGTH_SHORT);
                        toast.setGravity(0, 0, 0);
                        toast.show();
                    }else if (acai.getTam() == null) {
                        Toast toast = Toast.makeText(getApplicationContext(), Html.fromHtml("<b><big>" + "Escolha um Tamanho" + "</big></b>"), Toast.LENGTH_SHORT);
                        toast.setGravity(0, 0, 0);
                        toast.show();
                    } else if (acai.getSabor() == null) {
                        Toast toast = Toast.makeText(getApplicationContext(), Html.fromHtml("<b><big>" + "Escolha um Tipo de Açaí" + "</big></b>"), Toast.LENGTH_SHORT);
                        toast.setGravity(0, 0, 0);
                        toast.show();
                    } else if (rdbtninLocal.isChecked() == false && rdbtnViagem.isChecked() == false) {
                        Toast toast = Toast.makeText(getApplicationContext(), Html.fromHtml("<b><big>" + "Escolha um Local para Consumo" + "</big></b>"), Toast.LENGTH_SHORT);
                        toast.setGravity(0, 0, 0);
                        toast.show();
                    }

            }
        });

        //Componentes Estaticos//
        rdbtn_tamanhoP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                montarTamanhoEstatico(view);
            }
        });
        rdbtn_tamanhoM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                montarTamanhoEstatico(view);
            }
        });
        rdbtn_tamanhoG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                montarTamanhoEstatico(view);
            }
        });
        rdbtn_tamanhoGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                montarTamanhoEstatico(view);
            }
        });
        checkBox_Manga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                montarFrutasEstatico(view);
            }
        });
        checkBox_Morango.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                montarFrutasEstatico(view);
            }
        });
        checkBox_Kiwi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                montarFrutasEstatico(view);
            }
        });

        rdbtn_inLoco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rdbtn_viagem.setChecked(false);
                preferences.salvarPreferencias("precoEmbalagem", (float)(0));
                precoFinal.setPrecoEmbalagem(preferences.carregarPreferencias("precoEmbalagem"));
                precoFinal.notifyChange();
                btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));
//                + "<br />" +
//                        "<big>" + getString(R.string.preco) + String.format("%.2f", precoFinal.getPrecoTotal()) + "</big>" + "<br />"
            }
        });

        rdbtn_viagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rdbtn_inLoco.setChecked(false);
                preferences.salvarPreferencias("precoEmbalagem", (float)(1.0));
                precoFinal.setPrecoEmbalagem(preferences.carregarPreferencias("precoEmbalagem"));
                precoFinal.notifyChange();
                btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));

            }
        });

        checkBox_Mel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                limparLista();
                limparCheckAdicional();
                arrayFormasAdocar.get(2).setComprado(b);
                checkBox_Mel.setChecked(arrayFormasAdocar.get(2).isComprado());
                checkBox_Xilitol.setChecked(false);
                checkBox_Demerara.setChecked(false);
                checkBox_Mascavo.setChecked(false);
                checkBox_SemAcucar.setChecked(false);
                checkBox_Stevia.setChecked(false);
                checkBox_Xarope.setChecked(false);

                if(b)
                    preferences.salvarPreferencias("precoAdicional",(float)(arrayFormasAdocar.get(2).getValor()));
                else
                    preferences.salvarPreferencias("precoAdicional", (float)0);
                precoFinal.setPrecoAdicional(preferences.carregarPreferencias("precoAdicional"));
                precoFinal.notifyChange();
                btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));

            }
        });
        checkBox_Stevia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                limparLista();
                limparCheckAdicional();
                arrayFormasAdocar.get(6).setComprado(b);
                checkBox_Stevia.setChecked(arrayFormasAdocar.get(6).isComprado());
                checkBox_Xilitol.setChecked(false);
                checkBox_Demerara.setChecked(false);
                checkBox_Mascavo.setChecked(false);
                checkBox_Mel.setChecked(false);
                checkBox_SemAcucar.setChecked(false);
                checkBox_Xarope.setChecked(false);
                checkBox_Cristal.setChecked(false);

                if(b)
                    preferences.salvarPreferencias("precoAdicional",(float)(arrayFormasAdocar.get(6).getValor()));
                else
                    preferences.salvarPreferencias("precoAdicional", (float)0);
                precoFinal.setPrecoAdicional(preferences.carregarPreferencias("precoAdicional"));
                precoFinal.notifyChange();
                btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));


            }
        });
        checkBox_Cristal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                limparLista();
                limparCheckAdicional();
                arrayFormasAdocar.get(7).setComprado(b);
                checkBox_Cristal.setChecked(arrayFormasAdocar.get(7).isComprado());
                checkBox_SemAcucar.setChecked(false);
                checkBox_Demerara.setChecked(false);
                checkBox_Mascavo.setChecked(false);
                checkBox_Mel.setChecked(false);
                checkBox_Stevia.setChecked(false);
                checkBox_Xarope.setChecked(false);

                if(b)
                    preferences.salvarPreferencias("precoAdicional",(float)(arrayFormasAdocar.get(7).getValor()));
                else
                    preferences.salvarPreferencias("precoAdicional", (float)0);
                precoFinal.setPrecoAdicional(preferences.carregarPreferencias("precoAdicional"));
                precoFinal.notifyChange();
                btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));

            }
        });
        checkBox_Xilitol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                limparLista();
                limparCheckAdicional();
                arrayFormasAdocar.get(5).setComprado(b);
                checkBox_Xilitol.setChecked(arrayFormasAdocar.get(5).isComprado());
                checkBox_SemAcucar.setChecked(false);
                checkBox_Demerara.setChecked(false);
                checkBox_Mascavo.setChecked(false);
                checkBox_Mel.setChecked(false);
                checkBox_Stevia.setChecked(false);
                checkBox_Xarope.setChecked(false);
                checkBox_Cristal.setChecked(false);

                if(b)
                    preferences.salvarPreferencias("precoAdicional",(float)(arrayFormasAdocar.get(5).getValor()));
                else
                    preferences.salvarPreferencias("precoAdicional", (float)0);
                precoFinal.setPrecoAdicional(preferences.carregarPreferencias("precoAdicional"));
                precoFinal.notifyChange();
                btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));


            }
        });
        checkBox_Demerara.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                limparLista();
                limparCheckAdicional();
                arrayFormasAdocar.get(4).setComprado(b);
                checkBox_Demerara.setChecked(arrayFormasAdocar.get(4).isComprado());
                checkBox_Xilitol.setChecked(false);
                checkBox_SemAcucar.setChecked(false);
                checkBox_Mascavo.setChecked(false);
                checkBox_Mel.setChecked(false);
                checkBox_Stevia.setChecked(false);
                checkBox_Xarope.setChecked(false);
                checkBox_Cristal.setChecked(false);

                if(b)
                    preferences.salvarPreferencias("precoAdicional",(float)(arrayFormasAdocar.get(4).getValor()));
                else
                    preferences.salvarPreferencias("precoAdicional", (float)0);

                precoFinal.setPrecoAdicional(preferences.carregarPreferencias("precoAdicional"));
                precoFinal.notifyChange();
                btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));


            }
        });
        checkBox_Mascavo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                limparLista();
                limparCheckAdicional();
                arrayFormasAdocar.get(3).setComprado(b);
                checkBox_Mascavo.setChecked(arrayFormasAdocar.get(3).isComprado());
                checkBox_Xarope.setChecked(false);
                checkBox_Xilitol.setChecked(false);
                checkBox_Demerara.setChecked(false);
                checkBox_Stevia.setChecked(false);
                checkBox_Mel.setChecked(false);
                checkBox_SemAcucar.setChecked(false);
                checkBox_Cristal.setChecked(false);

                if(b)
                    preferences.salvarPreferencias("precoAdicional",(float)(arrayFormasAdocar.get(3).getValor()));
                else
                    preferences.salvarPreferencias("precoAdicional", (float)0);

                precoFinal.setPrecoAdicional(preferences.carregarPreferencias("precoAdicional"));
                precoFinal.notifyChange();
                btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));

            }
        });

        checkBox_Xarope.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                limparLista();
                limparCheckAdicional();
                arrayFormasAdocar.get(0).setComprado(b);
                checkBox_Xarope.setChecked(arrayFormasAdocar.get(0).isComprado());
                checkBox_SemAcucar.setChecked(false);
                checkBox_Xilitol.setChecked(false);
                checkBox_Demerara.setChecked(false);
                checkBox_Mascavo.setChecked(false);
                checkBox_Mel.setChecked(false);
                checkBox_Stevia.setChecked(false);
                checkBox_Cristal.setChecked(false);

                if(b)
                    preferences.salvarPreferencias("precoAdicional",(float)(arrayFormasAdocar.get(0).getValor()));
                else
                    preferences.salvarPreferencias("precoAdicional", (float)0);

                precoFinal.setPrecoAdicional(preferences.carregarPreferencias("precoAdicional"));
                precoFinal.notifyChange();
                btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));

            }
        });

        checkBox_SemAcucar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                limparLista();
                limparCheckAdicional();
                arrayFormasAdocar.get(1).setComprado(b);
                checkBox_SemAcucar.setChecked(arrayFormasAdocar.get(1).isComprado());
                checkBox_Xarope.setChecked(false);
                checkBox_Xilitol.setChecked(false);
                checkBox_Demerara.setChecked(false);
                checkBox_Mascavo.setChecked(false);
                checkBox_Mel.setChecked(false);
                checkBox_Stevia.setChecked(false);

                if(b)
                    preferences.salvarPreferencias("precoAdicional",(float)(arrayFormasAdocar.get(1).getValor()));
                else
                    preferences.salvarPreferencias("precoAdicional", (float)0);

                precoFinal.setPrecoAdicional(preferences.carregarPreferencias("precoAdicional"));
                precoFinal.notifyChange();
                btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                qtdClickHistorico ++;
                if(qtdClickHistorico>5){
                    Intent i = new Intent(getApplicationContext(), Historico.class);
                    startActivity(i);
                    qtdClickHistorico = 0;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean conectarImpressora(String nomeImpressora){

        try {
            btPrinter.setPrinterName(nomeImpressora);
            btPrinter.findBT();
            btPrinter.openBT();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }



    }

    public void limparCheckAdicional(){
        checkBox_Xarope.setChecked(false);
        checkBox_SemAcucar.setChecked(false);
        checkBox_Stevia.setChecked(false);
        checkBox_Xilitol.setChecked(false);
        checkBox_Demerara.setChecked(false);
        checkBox_Mascavo.setChecked(false);
        checkBox_Mel.setChecked(false);
        checkBox_Cristal.setChecked(false);
    }

    public void inicializarComponentes() {

        listViewSabores = findViewById(R.id.list_Sabores);
        listViewAcompanhamentos1 = findViewById(R.id.list1_acompanhamentos);
        listViewAcompanhamentos2 = findViewById(R.id.list2_acompanhamentos);
        rdbtn_MarcarTodos = findViewById(R.id.rdbtn_MarcarTodos);
        btn_confirmarPedido = findViewById(R.id.btn_confirmarPedido);
        btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));


        //Componentes Estaticos//
        rdbtn_tamanhoP = (RadioButton)findViewById(R.id.rdbtn_tamanhoP);
        rdbtn_tamanhoM = (RadioButton)findViewById(R.id.rdbtn_tamanhoM);
        rdbtn_tamanhoG = (RadioButton)findViewById(R.id.rdbtn_tamanhoG);
        rdbtn_tamanhoGG = (RadioButton)findViewById(R.id.rdbtn_tamanhoGG);
        checkBox_Kiwi = (CheckBox) findViewById(R.id.checkBox_Kiwi);
        checkBox_Morango = (CheckBox) findViewById(R.id.checkBox_Morango);
        checkBox_Manga = (CheckBox) findViewById(R.id.checkBox_Manga);

        checkBox_Mel = (CheckBox) findViewById(R.id.chckbtn_adicionalMel);
        checkBox_Xarope = (CheckBox) findViewById(R.id.chckbtn_adicionalXarope);
        checkBox_Demerara = (CheckBox) findViewById(R.id.chckbtn_adicionalDemerara);
        checkBox_Mascavo = (CheckBox) findViewById(R.id.chckbtn_adicionalMascavo);
        checkBox_SemAcucar = (CheckBox) findViewById(R.id.chckbtn_adicionalSemAcucar);
        checkBox_Stevia = (CheckBox) findViewById(R.id.chckbtn_adicionalStevia);
        checkBox_Xilitol = (CheckBox) findViewById(R.id.chckbtn_adicionalXilitol);
        checkBox_Cristal = (CheckBox) findViewById(R.id.chckbtn_adicionalCristal);

        rdbtn_inLoco = (RadioButton) findViewById(R.id.rdbtn_InLoco);
        rdbtn_viagem = (RadioButton) findViewById(R.id.rdbtn_Viagem);
        //radioGroupLocal = (RadioGroup) findViewById(R.id.radio_groupLocal);

    }

    public ArrayList adicionarFrutas(){
        ArrayList<Frutas> listaFrutas = new ArrayList<Frutas>();

        //Adicionar Frutas//
        listaFrutas.add(Frutas.MANGA);
        listaFrutas.add(Frutas.MORANGO);
        listaFrutas.add(Frutas.KIWI);

        return listaFrutas;
    }

    public ArrayList adicionarTamanhos(){
        ArrayList<Tamanho> listaTamanhos = new ArrayList<Tamanho>();

        //Adicionar Frutas//
        listaTamanhos.add(Tamanho.P);
        listaTamanhos.add(Tamanho.M);
        listaTamanhos.add(Tamanho.G);
        listaTamanhos.add(Tamanho.GG);

        return listaTamanhos;
    }

    public ArrayList adicionarSabores(){
        ArrayList<Sabor> listaSabores = new ArrayList<Sabor>();

        //Adicionar Sabores//
        listaSabores.add(Sabor.ACAI);
        listaSabores.add(Sabor.CUPUACU);
        listaSabores.add(Sabor.METADE_METADE);

        return listaSabores;
    }

    public ArrayList<Adicional> adicionarAdicional() {
        ArrayList<Adicional> listaAdicional = new ArrayList<Adicional>();

        //Adicionar Acompanhamentos//
        listaAdicional.add(Adicional.XAROPE);
        listaAdicional.add(Adicional.SEM_ACUCAR);
        listaAdicional.add(Adicional.MEL);
        listaAdicional.add(Adicional.MASCAVO);
        listaAdicional.add(Adicional.DEMERARA);
        listaAdicional.add(Adicional.XILITOL);
        listaAdicional.add(Adicional.STEVIA);
        listaAdicional.add(Adicional.CRISTAL);

        return listaAdicional;
    }

    public ArrayList<Acompanhamento> adicionarAcompanhamentos(ArrayList lista1, ArrayList lista2) {
        ArrayList<Acompanhamento> listaAcompanhamentos = new ArrayList<>();

        //Adicionar Acompanhamentos//
        listaAcompanhamentos.add(Acompanhamento.LEITE_CONDENSADO);
        listaAcompanhamentos.add(Acompanhamento.LEITE_PO);
        listaAcompanhamentos.add(Acompanhamento.FARINHA_AMENDOIM);
        listaAcompanhamentos.add(Acompanhamento.AMENDOIM_T);
        listaAcompanhamentos.add(Acompanhamento.BOLINHA_NESCAU);
        listaAcompanhamentos.add(Acompanhamento.AMENDOIM);
        listaAcompanhamentos.add(Acompanhamento.BANANA);
        listaAcompanhamentos.add(Acompanhamento.MEL);
        listaAcompanhamentos.add(Acompanhamento.FARINHA_LACTEA);
        listaAcompanhamentos.add(Acompanhamento.FARINHA_CASTANHA);
        listaAcompanhamentos.add(Acompanhamento.AVEIA);
        //listaAcompanhamentos.add(Acompanhamento.FLOCOS_ARROZ);
        listaAcompanhamentos.add(Acompanhamento.GRANOLA);
        listaAcompanhamentos.add(Acompanhamento.SUCRILHOS);

        Collections.sort(listaAcompanhamentos, new Comparador());

        for (int i = 0; i < listaAcompanhamentos.size(); i++) {
            if(i%2==0){
                lista1.add(listaAcompanhamentos.get(i));
            }else{
                lista2.add(listaAcompanhamentos.get(i));
            }
        }

        return listaAcompanhamentos;
    }

    public void buttonChecked(View v){

        v.getId();
        Preferences p = new Preferences(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        switch(v.getId()) {
            case R.id.rdbtn_listaAdicional:
                precoFinal.setPrecoAdicional(p.carregarPreferencias("precoAdicional"));
                precoFinal.notifyChange();
                break;

            case R.id.rdbtn_listaTamanhos:
                precoFinal.setPrecoTamanho(p.carregarPreferencias("precoTamanhos"));
                precoFinal.notifyChange();
                break;

            case R.id.chckbtn_listaFrutas:
                precoFinal.setPrecoFrutas(precoFinal.getPrecoFrutas() + p.carregarPreferencias("precoFrutas"));
                precoFinal.notifyChange();
                break;

            case R.id.rdbtn_listaSabores:
                precoFinal.setPrecoSabores(p.carregarPreferencias("precoSabores"));
                precoFinal.notifyChange();
                adapterSabores.notifyDataSetChanged();
                break;

            case R.id.chckbtn_listaAcompanhamentos:
                precoFinal.setPrecoAcompanhamentos(getPrecoAcompanhamentos());
                precoFinal.notifyChange();
                break;
        }

        btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));


    }

    public float getPrecoAcompanhamentos(){
        float precoAcompanhamentos = 0;
        int totalAcompanhamentos = 0;
        for(Acompanhamento a: arrayAcompanhamentos1){
            if(a.isComprado()){
                totalAcompanhamentos ++;
            }
        }
        for(Acompanhamento a: arrayAcompanhamentos2){
            if(a.isComprado()){
                totalAcompanhamentos ++;
            }
        }
        precoAcompanhamentos = totalAcompanhamentos > NUMERO_MINIMO_ACOMPANHAMENTO ? (float)( (totalAcompanhamentos-NUMERO_MINIMO_ACOMPANHAMENTO) * 1.5):0F;
        return precoAcompanhamentos;
    }

    public void alertDialogCustom(Pedido p) {

        final Pedido pedido = p;
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_layout_alert_dialog, null);
        TextView txtLista = alertLayout.findViewById(R.id.txtalert_pedido);

        txtLista.setText("Obrigado pela Preferência! \nRetire seu pedido na impressora e dirija-se ao caixa.");

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(alertLayout);
        alert.setCancelable(false);

        //Identificar enquanto está em Run - AlertDialog//
        final Dialog dialog = alert.show();

        dialog.getWindow().setBackgroundDrawableResource(R.color.paletaPrincipal);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // verificar se a caixa de diálogo está visível
                if (dialog.isShowing()) {
                    // fecha a caixa de diálogo
                    dialog.dismiss();
                }
            }
        };

        //Quando Dialog fechar//
        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });



        handler.postDelayed(runnable, 7000);
        montarCardapio();
        limparAdapter();



    }

    public void limparAdapter(){
        //Estatico
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        radioGroup.clearCheck();

        rdbtn_inLoco = (RadioButton) findViewById(R.id.rdbtn_InLoco);
        rdbtn_viagem = (RadioButton) findViewById(R.id.rdbtn_Viagem);
        if(rdbtn_inLoco.isChecked() == true){
            rdbtn_inLoco.toggle();
        }
        if(rdbtn_viagem.isChecked() == true){
            rdbtn_viagem.toggle();
        }

        for (int i = 0; i <arrayTamanhos.size() ; i++) {
            arrayTamanhos.get(i).setChecado(false);
        }

        for (int i = 0; i <arrayTamanhos.size() ; i++) {
            arrayFormasAdocar.get(i).setComprado(false);
        }

        limparCheckAdicional();

        checkBox_Kiwi.setChecked(false);
        checkBox_Manga.setChecked(false);
        checkBox_Morango.setChecked(false);

        for (int i = 0; i <arrayFrutas.size() ; i++) {
            arrayFrutas.get(i).setComprado(false);
        }

        adapterSabores.restaurarLista(adicionarSabores());

        ArrayList<Acompanhamento> listA = new ArrayList<Acompanhamento>();
        ArrayList<Acompanhamento> listB = new ArrayList<Acompanhamento>();
        adicionarAcompanhamentos(listA,listB);
        adapterAcompanhamentos1.restaurarLista(listA);
        adapterAcompanhamentos2.restaurarLista(listB);
        rdbtn_MarcarTodos.setChecked(false);

        Preferences preferences = new Preferences(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        preferences.salvarPreferencias("precoFrutas",(float)0);
        preferences.salvarPreferencias("precoAcompanhamentos", (float)0);
        preferences.salvarPreferencias("precosTamanhos",(float)0);
        preferences.salvarPreferencias("precoSabores", (float)0);
        preferences.salvarPreferencias("precoAdicional",(float)0);
        preferences.salvarPreferencias("precoEmbalagem", (float)(0));


        precoFinal = new PrecoFinal(0,0,0,0,0,0,0);
        btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));

    }

    public void montarCardapio(){

        arrayTamanhos = adicionarTamanhos();
        arrayFrutas = adicionarFrutas();
        arrayFormasAdocar = adicionarAdicional();

        //Montando Lista - Sabores//
        if(arraySabores!=null){
            arraySabores.clear();
        }
        arraySabores = adicionarSabores();
        adapterSabores = new AdapterSabores(this, arraySabores);
        listViewSabores.setAdapter(adapterSabores);
        adicionarSabores();
        ListaDinamica.setListViewHeighBasedOnItems(listViewSabores);


        //Montando Listas - Acompanhamento//
        if(arrayAcompanhamentos1 !=null){
            arrayAcompanhamentos1.clear();
        }if(arrayAcompanhamentos2 !=null){
            arrayAcompanhamentos2.clear();
        }

        arrayAcompanhamentos1 = new ArrayList<Acompanhamento>();
        arrayAcompanhamentos2 = new ArrayList<Acompanhamento>();
        adicionarAcompanhamentos(arrayAcompanhamentos1, arrayAcompanhamentos2);
        adapterAcompanhamentos1 = new AdapterAcompanhamentos(this, arrayAcompanhamentos1);
        listViewAcompanhamentos1.setAdapter(adapterAcompanhamentos1);
        ListaDinamica.setListViewHeighBasedOnItems(listViewAcompanhamentos1);

        adapterAcompanhamentos2 = new AdapterAcompanhamentos(this, arrayAcompanhamentos2);
        listViewAcompanhamentos2.setAdapter(adapterAcompanhamentos2);
        ListaDinamica.setListViewHeighBasedOnItems(listViewAcompanhamentos2);

    }

    public void montarTamanhoEstatico(View v) {

        Preferences p = new Preferences(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        for (int i = 0; i < arrayTamanhos.size(); i++) {
            arrayTamanhos.get(i).setChecado(false);
        }

            if(v.getId() == R.id.rdbtn_tamanhoP || v.getId()==R.id.txt_precoP){
            //Marcar Se clicar no txtView
                if(v.getId()==R.id.txt_precoP)
                    rdbtn_tamanhoP.setChecked(true);

                arrayTamanhos.get(0).setChecado(true);
                p.salvarPreferencias("precoTamanhos",(float)(arrayTamanhos.get(0).getValor()));
                precoFinal.setPrecoTamanho(p.carregarPreferencias("precoTamanhos"));
                precoFinal.notifyChange();
                btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));

             }
            else if(v.getId() == R.id.rdbtn_tamanhoM || v.getId()==R.id.txt_precoM) {
                //Marcar Se clicar no txtView
                if(v.getId()==R.id.txt_precoM)
                    rdbtn_tamanhoM.setChecked(true);

                arrayTamanhos.get(1).setChecado(true);
                p.salvarPreferencias("precoTamanhos", (float) (arrayTamanhos.get(1).getValor()));
                precoFinal.setPrecoTamanho(p.carregarPreferencias("precoTamanhos"));
                precoFinal.notifyChange();
                btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));

            }
            else if(v.getId() == R.id.rdbtn_tamanhoG || v.getId() == R.id.txt_precoG){
                //Marcar Se clicar no txtView
                if(v.getId()==R.id.txt_precoG)
                    rdbtn_tamanhoG.setChecked(true);

                arrayTamanhos.get(2).setChecado(true);
                p.salvarPreferencias("precoTamanhos",(float)(arrayTamanhos.get(2).getValor()));
                precoFinal.setPrecoTamanho(p.carregarPreferencias("precoTamanhos"));
                precoFinal.notifyChange();
                btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));

            }
            else if(v.getId() == R.id.rdbtn_tamanhoGG || v.getId() == R.id.txt_PrecoGG) {
                //Marcar Se clicar no txtView
                if(v.getId()==R.id.txt_PrecoGG)
                    rdbtn_tamanhoGG.setChecked(true);

                arrayTamanhos.get(3).setChecado(true);
                p.salvarPreferencias("precoTamanhos", (float) (arrayTamanhos.get(3).getValor()));
                precoFinal.setPrecoTamanho(p.carregarPreferencias("precoTamanhos"));
                precoFinal.notifyChange();
                btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));

            }

    }

    public void montarFrutasEstatico(View v) {

        Preferences p = new Preferences(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

            if(v.getId() == R.id.checkBox_Manga || v.getId() == R.id.txt_precoManga) {
                if(v.getId() == R.id.txt_precoManga){
                    if(checkBox_Manga.isChecked())
                        checkBox_Manga.setChecked(false);
                    else
                        checkBox_Manga.setChecked(true);
                }

                if (checkBox_Manga.isChecked()) {
                    arrayFrutas.get(0).setComprado(true);
                    p.salvarPreferencias("precoFrutas", p.carregarPreferencias("precoFrutas") + (float) (arrayFrutas.get(0).getValor()));
                    precoFinal.setPrecoFrutas(p.carregarPreferencias("precoFrutas"));
                    precoFinal.notifyChange();
                } else {
                    arrayFrutas.get(0).setComprado(false);
                    p.salvarPreferencias("precoFrutas", p.carregarPreferencias("precoFrutas") - (float) (arrayFrutas.get(0).getValor()));
                    precoFinal.setPrecoFrutas(p.carregarPreferencias("precoFrutas"));
                    precoFinal.notifyChange();
                }
            }
            if(v.getId() == R.id.checkBox_Morango || v.getId() == R.id.txt_precoMorango) {
                if(v.getId() == R.id.txt_precoMorango){
                    if(checkBox_Morango.isChecked())
                        checkBox_Morango.setChecked(false);
                    else
                        checkBox_Morango.setChecked(true);
                }

                if (checkBox_Morango.isChecked()) {
                    arrayFrutas.get(1).setComprado(true);
                    p.salvarPreferencias("precoFrutas", p.carregarPreferencias("precoFrutas") + (float) (arrayFrutas.get(0).getValor()));
                    precoFinal.setPrecoFrutas(p.carregarPreferencias("precoFrutas"));
                    precoFinal.notifyChange();
                } else {
                    arrayFrutas.get(1).setComprado(false);
                    p.salvarPreferencias("precoFrutas", p.carregarPreferencias("precoFrutas") - (float) (arrayFrutas.get(0).getValor()));
                    precoFinal.setPrecoFrutas(p.carregarPreferencias("precoFrutas"));
                    precoFinal.notifyChange();
                }

            }
            if(v.getId() == R.id.checkBox_Kiwi || v.getId() == R.id.txt_precoKiwi) {
                if(v.getId() == R.id.txt_precoKiwi){
                    if(checkBox_Kiwi.isChecked())
                        checkBox_Kiwi.setChecked(false);
                    else
                        checkBox_Kiwi.setChecked(true);
                }

                if (checkBox_Kiwi.isChecked()) {
                    arrayFrutas.get(2).setComprado(true);
                    p.salvarPreferencias("precoFrutas", p.carregarPreferencias("precoFrutas") + (float) (arrayFrutas.get(0).getValor()));
                    precoFinal.setPrecoFrutas(p.carregarPreferencias("precoFrutas"));
                    precoFinal.notifyChange();
                } else {
                    arrayFrutas.get(2).setComprado(false);
                    p.salvarPreferencias("precoFrutas", p.carregarPreferencias("precoFrutas") - (float) (arrayFrutas.get(0).getValor()));
                    precoFinal.setPrecoFrutas(p.carregarPreferencias("precoFrutas"));
                    precoFinal.notifyChange();
                }

            }
        btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));


    }

    public void limparLista(){
        for (int i = 0; i < arrayFormasAdocar.size() ; i++) {
            arrayFormasAdocar.get(i).setComprado(false);
        }
    }

    public void montarAdicionalEstatico(View v) {

        Preferences p = new Preferences(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        for (int i = 0; i < arrayFormasAdocar.size(); i++) {
            arrayFormasAdocar.get(i).setComprado(false);
        }

        if(v.getId() == R.id.chckbtn_adicionalXarope || v.getId()==R.id.txt_precoAdicionalXarope){
            //Marcar Se clicar no txtView
            if(v.getId()==R.id.txt_precoAdicionalXarope)
                checkBox_Xarope.callOnClick();

            arrayFormasAdocar.get(0).setComprado(true);
            p.salvarPreferencias("precoAdicional",(float)(arrayFormasAdocar.get(0).getValor()));
            precoFinal.setPrecoAdicional(p.carregarPreferencias("precoAdicional"));
            precoFinal.notifyChange();
            btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));


        }else if(v.getId() == R.id.chckbtn_adicionalSemAcucar || v.getId()==R.id.txt_precoAdicionalSemAcucar){
            //Marcar Se clicar no txtView
            if(v.getId()==R.id.txt_precoAdicionalSemAcucar)
                checkBox_SemAcucar.callOnClick();

            arrayFormasAdocar.get(1).setComprado(true);
            p.salvarPreferencias("precoAdicional",(float)(arrayFormasAdocar.get(1).getValor()));
            precoFinal.setPrecoAdicional(p.carregarPreferencias("precoAdicional"));
            precoFinal.notifyChange();
            btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));


        }
        else if(v.getId() == R.id.chckbtn_adicionalMel || v.getId()==R.id.txt_precoAdicionalMel) {
            //Marcar Se clicar no txtView
            if(v.getId()==R.id.txt_precoAdicionalMel)
                checkBox_Mel.callOnClick();

            arrayFormasAdocar.get(2).setComprado(true);
            p.salvarPreferencias("precoAdicional", (float) (arrayFormasAdocar.get(2).getValor()));
            precoFinal.setPrecoAdicional(p.carregarPreferencias("precoAdicional"));
            precoFinal.notifyChange();
            btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));


        }
        else if(v.getId() == R.id.chckbtn_adicionalMascavo || v.getId() == R.id.txt_precoAdicionalMascavo){
            //Marcar Se clicar no txtView
            if(v.getId()==R.id.txt_precoAdicionalMascavo)
                checkBox_Mascavo.callOnClick();

            arrayFormasAdocar.get(3).setComprado(true);
            p.salvarPreferencias("precoAdicional",(float)(arrayFormasAdocar.get(3).getValor()));
            precoFinal.setPrecoAdicional(p.carregarPreferencias("precoAdicional"));
            precoFinal.notifyChange();
            btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));


        }
        else if(v.getId() == R.id.chckbtn_adicionalDemerara || v.getId() == R.id.txt_precoAdicionalDemerara) {
            //Marcar Se clicar no txtView
            if(v.getId()==R.id.txt_precoAdicionalDemerara)
                checkBox_Demerara.callOnClick();

            arrayFormasAdocar.get(4).setComprado(true);
            p.salvarPreferencias("precoAdicional", (float) (arrayFormasAdocar.get(4).getValor()));
            precoFinal.setPrecoAdicional(p.carregarPreferencias("precoAdicional"));
            precoFinal.notifyChange();
            btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));


        }else if(v.getId() == R.id.chckbtn_adicionalXilitol || v.getId() == R.id.txt_precoAdicionalXilitol) {
            //Marcar Se clicar no txtView
            if(v.getId()==R.id.txt_precoAdicionalXilitol)
                checkBox_Xilitol.callOnClick();

            arrayFormasAdocar.get(5).setComprado(true);
            p.salvarPreferencias("precoAdicional", (float) (arrayFormasAdocar.get(5).getValor()));
            precoFinal.setPrecoAdicional(p.carregarPreferencias("precoAdicional"));
            precoFinal.notifyChange();
            btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));


        } else if(v.getId() == R.id.chckbtn_adicionalStevia || v.getId() == R.id.txt_precoAdicionalStevia) {
            //Marcar Se clicar no txtView
            if(v.getId()==R.id.txt_precoAdicionalStevia)
                checkBox_Stevia.callOnClick();

            arrayFormasAdocar.get(6).setComprado(true);
            p.salvarPreferencias("precoAdicional", (float) (arrayFormasAdocar.get(6).getValor()));
            precoFinal.setPrecoAdicional(p.carregarPreferencias("precoAdicional"));
            precoFinal.notifyChange();
            btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));


        } else if(v.getId() == R.id.chckbtn_adicionalCristal || v.getId() == R.id.txt_precoAdicionalCristal) {
            //Marcar Se clicar no txtView
            if(v.getId()==R.id.txt_precoAdicionalCristal)
                checkBox_Cristal.callOnClick();

            arrayFormasAdocar.get(7).setComprado(true);
            p.salvarPreferencias("precoAdicional", (float) (arrayFormasAdocar.get(7).getValor()));
            precoFinal.setPrecoAdicional(p.carregarPreferencias("precoAdicional"));
            precoFinal.notifyChange();
            btn_confirmarPedido.setText(Html.fromHtml("<b><big>" + getString(R.string.confirmar) + "</big></b>" ));

        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onDestroy(){
        try{
            btPrinter.closeBT();
        } catch(Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();

    }

}



