package com.example.giordano.cardapiointeligente.View;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListaDinamica{


    public static boolean setListViewHeighBasedOnItems(ListView listView){

        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter != null){

            int numeroItens = listAdapter.getCount();

            int totalItens = 0;
            for(int itemPos = 0; itemPos<numeroItens; itemPos++){

                float px = 400 * (listView.getResources().getDisplayMetrics().density);

                View item = listAdapter.getView(itemPos,null,listView);
                item.measure(View.MeasureSpec.makeMeasureSpec((int)px, View.MeasureSpec.AT_MOST),View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItens += item.getMeasuredHeight();
            }

            int mediaTamanho = listView.getDividerHeight() * (numeroItens - 1);

            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams parametros = listView.getLayoutParams();
            parametros.height = totalItens + mediaTamanho + totalPadding;
            listView.setLayoutParams(parametros);
            listView.requestLayout();

            return true;
        } else {
            return false;
        }
    }


}
