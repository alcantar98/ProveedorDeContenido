package com.example.proveedordecontenido;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.UserDictionary;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView txtContenido;
    EditText txtNombre;
    EditText txtPassword;
    Button btnIniciar;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtContenido=findViewById(R.id.txtContenido);
        txtNombre=findViewById(R.id.txtName);
        txtPassword=findViewById(R.id.txtPassword);
        btnIniciar=findViewById(R.id.btnIniciar);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new Task1(MainActivity.this).execute(txtNombre.getText().toString());
                permisos();
            }
        });


    }
    private final int REQUEST_ASK_CODE_PERMISSION = 307;
    private void permisos(){
        int permisoLeerContacto= ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_CONTACTS);
        int permisoEscribirContacto= ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_CONTACTS);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(permisoLeerContacto== PackageManager.PERMISSION_GRANTED && permisoEscribirContacto==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getBaseContext(),"Ya tienes permisos",Toast.LENGTH_LONG);
                eliminarContacto();
            }else{
                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)){
                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Para poder enviar un mensaje es necesario aceptar los permisos");
                    builder.setTitle("Permisos necesarios");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS},REQUEST_ASK_CODE_PERMISSION);
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }else{
                    requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS},REQUEST_ASK_CODE_PERMISSION);
                }
            }


        }

    }
    public void eliminarContacto(){
        //Realizamos nuestra condicion
        String seleccionClausule=ContactsContract.Contacts.DISPLAY_NAME+" = ?";
        String[] seleccionArgs={"Nuevo"};

        //Contador de eliminador
        int eliminados=0;

        eliminados=getContentResolver().delete(
                ContactsContract.Contacts.CONTENT_URI,
                seleccionClausule,
                seleccionArgs
        );
        Toast.makeText(getBaseContext(),"Eliminados: "+eliminados,Toast.LENGTH_LONG).show();
    }
    public void actualizarContacto(){
        //ContentValues con valores a actualizar
        ContentValues contentValues=new ContentValues();
        //SelecciónClausule Con las filas que vamoa a actualizar
        String seleccionClausule=ContactsContract.Contacts.DISPLAY_NAME+" = ?";
        String[] seleccionArgs ={"Novio De lupe"};
        //Variable que indicara las filas actualizadas
        int filasEditadas=0;

        contentValues.put(ContactsContract.Contacts.DISPLAY_NAME,"nuevito");
        filasEditadas=getContentResolver().update(
            ContactsContract.Contacts.CONTENT_URI,
                contentValues,
                seleccionClausule,
                seleccionArgs
                );
        Toast.makeText(getBaseContext(),"Se actualizaron: "+filasEditadas,Toast.LENGTH_LONG).show();
    }
    public void insertarContacto(){
        //Esta URI recibe el resultado de la insercción;
        Uri uri;
        //ContentValues que contendra los datos de la insercción
        ContentValues contentValues=new ContentValues();
        //Asignamos los valores para cada columna
        contentValues.put(ContactsContract.Contacts.DISPLAY_NAME, "contacto1");

        uri=getContentResolver().insert(
                ContactsContract.Contacts.CONTENT_URI,
                contentValues
        );
    }
    public void obtenerContactos(){
        String[] projection={
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts._ID
        };

        String selection=UserDictionary.Words.WORD+" like ?";
        String[] selectionArgs={"a"};
        Cursor cursor=getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
        int index=cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        if(cursor==null){
            Toast.makeText(getBaseContext(),"No se pudierón obtener las palabras",Toast.LENGTH_SHORT).show();
        }else if(cursor.getCount()<1){
            Toast.makeText(getBaseContext(),"No exixten palabras con eso parametros",Toast.LENGTH_SHORT).show();
        }else {
            String cadena="";
            while (cursor.moveToNext()){
                cadena+=cursor.getString(index);
            }
            Toast.makeText(getBaseContext(),cadena,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}