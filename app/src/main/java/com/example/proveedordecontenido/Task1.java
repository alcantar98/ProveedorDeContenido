package com.example.proveedordecontenido;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Task1 extends AsyncTask<String, Void, String> {

    private MainActivity activity;
    public Task1(MainActivity activity) {
        this.activity=activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.progressBar.setVisibility(View.VISIBLE);
        activity.btnIniciar.setEnabled(false);
        Toast.makeText(activity.getBaseContext(),"Tarea asincona iniciada",Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            Thread.sleep(5000);
        }catch (Exception e){
            e.printStackTrace();
        }
        return strings[0];
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        activity.progressBar.setVisibility(View.INVISIBLE);
        activity.btnIniciar.setEnabled(true);
        Intent intent=new Intent(activity.getBaseContext(), Resultado.class);
        intent.putExtra("usuario",s);
        activity.startActivity(intent);


    }
}
