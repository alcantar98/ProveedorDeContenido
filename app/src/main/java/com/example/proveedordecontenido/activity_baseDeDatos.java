package com.example.proveedordecontenido;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proveedordecontenido.Dao.BDProductos;
import com.example.proveedordecontenido.provider.Contrato;

public class activity_baseDeDatos extends AppCompatActivity {

    EditText txtCodigo, txtNombre, txtDescripcion;
    Button btnInsertar, btnEditar, btnEliminar, btnBuscar, btnVer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_de_datos);
        btnInsertar=findViewById(R.id.btnInsertar);
        btnEditar=findViewById(R.id.btnEditar);
        btnEliminar=findViewById(R.id.btnEliminar);
        btnBuscar=findViewById(R.id.btnBuscar);
        txtCodigo=findViewById(R.id.txtCodigo);
        txtNombre=findViewById(R.id.txtNombre);
        txtDescripcion=findViewById(R.id.txtDescripcion);
        btnVer=findViewById(R.id.btnVer);
        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(),activity_lista.class);
                startActivity(intent);

            }
        });
        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarContrato();
            }
        });
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar();
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar();
            }
        });
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar();
            }
        });
    }
    public void insertarContrato(){
        ContentValues contentValues=new ContentValues();
        contentValues.put(Contrato.Producto.CODIGO,txtCodigo.getText().toString());
        contentValues.put(Contrato.Producto.NOMBRE,txtNombre.getText().toString());
        contentValues.put(Contrato.Producto.DESCRIPCION,txtDescripcion.getText().toString());
        Uri uri=getContentResolver().insert(
                Contrato.Producto.CONTENT_URI,
                contentValues
        );
    }
    public void registrar(){
        BDProductos bdProductos=new BDProductos(getBaseContext(),"bdProductos",null,1);
        SQLiteDatabase baseDeDatos=bdProductos.getWritableDatabase();

        String codigo=txtCodigo.getText().toString();
        String nombre=txtNombre.getText().toString();
        String descripcion=txtDescripcion.getText().toString();

        if(!codigo.isEmpty() && !nombre.isEmpty() && !descripcion.isEmpty()){
            ContentValues contentValues=new ContentValues();
            contentValues.put("codigo",codigo);
            contentValues.put("nombre",nombre);
            contentValues.put("descripcion",descripcion);
            baseDeDatos.insert("productos",null,contentValues);
            baseDeDatos.close();
            txtCodigo.setText("");
            txtNombre.setText("");
            txtDescripcion.setText("");
            Toast.makeText(getBaseContext(),"Registro realizado",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getBaseContext(),"Llene todos los campos",Toast.LENGTH_SHORT).show();
        }
    }
    public void buscar(){
        BDProductos bdProductos=new BDProductos(getBaseContext(),"bdProductos",null, 1);
        SQLiteDatabase bd=bdProductos.getWritableDatabase();

        String codigo=txtCodigo.getText().toString();
        if(!codigo.isEmpty()){
            String consulta="SELECT codigo, nombre, descripcion from productos where codigo = ?;";
            String[] argumentos={codigo};
            Cursor cursor=bd.rawQuery(consulta,argumentos);
            if(cursor.moveToFirst()){

                txtCodigo.setText(cursor.getString(0));
                txtDescripcion.setText(cursor.getString(2));
                txtNombre.setText(cursor.getString(1));

            }else{
                Toast.makeText(getBaseContext(), "El producto no existe",Toast.LENGTH_SHORT).show();
            }
            bd.close();
        }else{
            Toast.makeText(getBaseContext(),"Debes introducir el código del producto",Toast.LENGTH_LONG).show();;
        }
    }
    public void editar(){
        BDProductos bdProductos=new BDProductos(getBaseContext(),"bdProductos",null,1);
        SQLiteDatabase bd=bdProductos.getWritableDatabase();
        String codigo=txtCodigo.getText().toString();
        String nombre=txtNombre.getText().toString();
        String descripcion=txtDescripcion.getText().toString();

        if(!codigo.isEmpty() && !nombre.isEmpty() && !descripcion.isEmpty()){
            ContentValues contentValues=new ContentValues();
            contentValues.put("codigo",codigo);
            contentValues.put("nombre",nombre);
            contentValues.put("descripcion",descripcion);
            String condicion="codigo=?";
            String[] condicionArgs={codigo};
            int actualizados=bd.update("productos",contentValues, condicion,condicionArgs);
            Toast.makeText(getBaseContext(),"Se ha editado: "+actualizados,Toast.LENGTH_SHORT).show();
            bd.close();
        }else{
            Toast.makeText(getBaseContext(), "Es necesario llenar todos los campos",Toast.LENGTH_SHORT).show();
        }
    }
    public void eliminar(){
        BDProductos bdProductos=new BDProductos(getBaseContext(),"bdProductos",null,1);
        SQLiteDatabase bd=bdProductos.getWritableDatabase();
        String codigo=txtCodigo.getText().toString();
        if(!codigo.isEmpty()){
            String sql="codigo = ?";
            String[] sqlArgumentos={codigo};
            int cantidad=bd.delete("productos", sql, sqlArgumentos);
            bd.close();
            Toast.makeText(getBaseContext(),"Se han eliminado: "+cantidad,Toast.LENGTH_SHORT).show();
            txtCodigo.setText("");
            txtNombre.setText("");
            txtDescripcion.setText("");
        }else {
            Toast.makeText(getBaseContext(),"Es necesario el coidgo para eliminar", Toast.LENGTH_SHORT).show();
        }
    }
}