package com.example.proveedordecontenido.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contrato {
    //Autoridad para indicar la ruta de nuestro Content Provider
    public static final String AUTHORITY="com.example.proveedordecontenido.provider.MiContentProvider";

    public static final class Producto implements BaseColumns{
        //Uri para nuestra tabla
        public static final Uri CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/productos");

        //Columnas de nuestra tabla
        public static final String CODIGO="codigo";
        public static final String NOMBRE="nombre";
        public static final String DESCRIPCION="descripcion";
    }
}
