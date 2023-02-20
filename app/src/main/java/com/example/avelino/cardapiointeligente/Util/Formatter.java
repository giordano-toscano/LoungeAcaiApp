package com.example.avelino.cardapiointeligente.Util;

public class Formatter {

    private byte[] format;

    public Formatter() { format = new byte[] {27, 33, 0}; }
    //public Formatter() {format = new byte[] {5 , 2 , 24};}

    public Formatter medium_text() {
        format[0] = ((byte) (0x1B|format[0]));
        format[1] = ((byte) (0x21|format[1]));
        format[2] = ((byte) (0x08|format[2]));
        return this;
    }

    public Formatter large_text() {
        format[0] = ((byte) (0x1B|format[0]));
        format[1] = ((byte) (0x21|format[1]));
        format[2] = ((byte) (0x10|format[2]));
        return this;
    }

    public Formatter bold() {

        format[2] = ((byte) (0x8| format[2]));
        return this;
    }

    public Formatter small() {
        format[2] = ((byte) (0x1 | format[2]));
        return this;
    }

    public Formatter height() {
        format[2] = ((byte) (0x10 | format[2]));
        return this;
    }

    public Formatter width() {
        format[2] = ((byte) (0x20 | format[2]));
        return this;
    }

    public Formatter underlined() {
        format[2] = ((byte) (0x80 | format[2]));
        return this;
    }

    public static byte[] rightAlign(){
        return new byte[]{0x1B, 'a', 0x02};
    }

    public static byte[] leftAlign(){
        return new byte[]{0x1B, 'a', 0x00};
    }

    public static byte[] centerAlign(){
        return new byte[]{0x1B, 'a', 0x01};
    }

    public byte[] getFormat() {
        return format;
    }
}
