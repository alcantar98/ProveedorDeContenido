package com.example.proveedordecontenido;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proveedordecontenido.Dao.BDProductos;

public class activity_lista extends AppCompatActivity {

    TextView txtLista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        txtLista=findViewById(R.id.txtLista);
        obtenerTodos();
    }
    public void obtenerTodos(){
        BDProductos bdProductos=new BDProductos(getBaseContext(),"bdProductos",null, 1);
        SQLiteDatabase bd=bdProductos.getWritableDatabase();
        String consulta="SELECT codigo, nombre, descripcion from productos;";
        String resultado="";
        Cursor cursor=bd.rawQuery(consulta,null);

        if(cursor.moveToFirst()){
            while (cursor.moveToNext()){
                resultado+=cursor.getString(0)+" "+cursor.getString(1)+" "+cursor.getString(2)+"\n";
            }
        }else{
            Toast.makeText(getBaseContext(), "El producto no existe",Toast.LENGTH_SHORT).show();
        }
        bd.close();
        txtLista.setText(resultado);
    }
}