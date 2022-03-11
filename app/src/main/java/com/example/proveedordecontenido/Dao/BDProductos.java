package com.example.proveedordecontenido.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.proveedordecontenido.provider.MiContentProvider;

public class BDProductos extends SQLiteOpenHelper {
    public BDProductos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //En este metodo se crea la base de datos con SQL
    @Override
    public void onCreate(SQLiteDatabase db) {

        //Creación de la tabla productos
        db.execSQL("CREATE TABLE productos(" +
                "codigo int primary key," +
                "nombre text not null," +
                "descripcion text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }
}
