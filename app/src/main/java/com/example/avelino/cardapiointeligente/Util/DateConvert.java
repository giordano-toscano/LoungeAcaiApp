package com.example.avelino.cardapiointeligente.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateConvert {

    public static String toPtBr(Date date){
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        String str = fmt.format(date);
        return str;
    }
}
