package com.example.proveedordecontenido;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Resultado extends AppCompatActivity {

    TextView txtResultado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        txtResultado=findViewById(R.id.txtResultado);
        String nombre=getIntent().getStringExtra("usuario");
        txtResultado.setText(nombre);
    }
}