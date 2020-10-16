package media.extreme.com.brothercorpappmovil;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class DetailReadings extends AppCompatActivity {
    private TextView textIdDetailReadingss;

    private EditText textCodigo,textCliente,textLecturaInicial,textLecturaFinal,textPeriodo,textDiferencia;

    private int codigoSistema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_readings);

        textCodigo=(EditText)findViewById(R.id.textCodigoUno);
        textCliente=(EditText)findViewById(R.id.textClienteUno);
        textLecturaInicial=(EditText)findViewById(R.id.textLecturaInicialUno);
        textLecturaFinal=(EditText)findViewById(R.id.textLecturaFinalUno);
        textPeriodo=(EditText)findViewById(R.id.textPeriodoUno);

        textLecturaFinal.requestFocus();

        textDiferencia=(EditText)findViewById(R.id.textDiferencia);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<Map<String, String>> listDetail;
        Intent i = getIntent();
        String detail = i.getStringExtra("detail");
        String name=i.getStringExtra("name");

        //listDetail=i.getStringExtra("id");
        //String value=i.getStringExtra("value");

        //String string = "004-034556";
        String[] parts = detail.split("-");


        //textIdDetailReadingss=(TextView)findViewById(R.id.textView4);
        //textIdDetailReadingss.setText(parts[0]);
        //obtenemos id y consultamos
        Log.d("EDISON","ID "+parts[4]);
        //Log.d("EDISON","dos "+id);
        if(parts[4].isEmpty())
        {
            Log.d("EDISON","NO LLEGO CADENA CONTACTAR CON ADMINISTRADOR");
            Toast.makeText(this, "NO LLEGO CADENA CONTACTAR CON ADMINISTRADOR",
                    Toast.LENGTH_SHORT).show();
        }
        else
        {
            codigoSistema=Integer.parseInt(parts[4].trim().toString());
        }

        try
        {
        //consultaporcodigo(parts[0].trim().toString());
            consultaporcodigo(Integer.parseInt(parts[4].trim().toString()));
        }
        catch(Exception ex)
        {
            Log.d("EDISON",ex.toString());
        }

        textLecturaFinal.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                String cadena=editable.toString();
                int diferencia=0;

                Log.d("cadena",String.valueOf(cadena.length()));

                if(cadena.length() !=0 )
                {
                    //int i=editable.toString().length();
                    Log.d("diferente cero",String.valueOf(cadena.length()));

                        if (Integer.parseInt(cadena.toString()) >Integer.parseInt(textLecturaInicial.getText().toString())) {
                            diferencia=Integer.parseInt(cadena.toString())-Integer.parseInt(textLecturaInicial.getText().toString());
                            textDiferencia.setText(String.valueOf(diferencia));
                        }
                        else
                        {
                            textDiferencia.setText("0");
                        }


                    //Log.d("DIFERENTE DE CERO",);
                }

                /*if(editable.toString()!="") {
                    if (textLecturaInicial.getText().toString() != "" && textLecturaFinal.getText().toString() == "") {
                        if (Integer.parseInt(textLecturaInicial.getText().toString()) > Integer.parseInt(editable.toString())) {
                            textDiferencia.setText(editable.toString());
                        }

                    }
                }*/
            }
        });



    }
    public void consultaporcodigo(int id) {
        String codigo;
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        //codigo = this.textCodigo.getText().toString();
        Cursor fila = bd.rawQuery(
                "select numero,cliente,lecturaInicial,LecturaFinal,periodo from lecturas where codigo="+ id , null);
        if (fila.moveToFirst()) {

            textCodigo.setText(fila.getString(0));
            textCliente.setText(fila.getString(1));
            textLecturaInicial.setText(fila.getString(2));
            textLecturaFinal.setText(fila.getString(3));
            textPeriodo.setText(fila.getString(4));

        } else
            //Toast.makeText(this, "No existe un lectura con dicho código",
            //        Toast.LENGTH_SHORT).show();
            bd.close();
    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date=new Date();
        return dateFormat.format(date);
    }
    public void altaLecturaFinal(View v) {

        String codigo,lecturaFinal,fechaModificacion;
        int valInicial=0,valFinal=0;

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        lecturaFinal=textLecturaFinal.getText().toString();
        //codigo=textCodigo.getText().toString();
        fechaModificacion=getDateTime();
        if(textLecturaInicial.getText().toString().equals("")) {
            valInicial = 0;
        }
        else
        {
            try {
                valInicial = Integer.parseInt(textLecturaInicial.getText().toString());
            }
            catch (Exception e)
            {
                Toast.makeText(this, "LECTURA INICIAL INCORRECTA",
                        Toast.LENGTH_SHORT).show();
            }
        }

        if(textLecturaFinal.getText().toString().equals("")) {
            valFinal = 0;
            Toast.makeText(this, "DEBE INGRESAR LECTURA FINAL PARA PODER ACTUALIZAR EL REGISTRO",
                    Toast.LENGTH_SHORT).show();
        }
        else
        {
            try {
                valFinal = Integer.parseInt(textLecturaFinal.getText().toString());
            }
            catch (Exception e)
            {
                Toast.makeText(this, "LECTURA FINAL INCORRECTA",
                        Toast.LENGTH_SHORT).show();
            }
        }

            if(valFinal>valInicial)
            {
                ContentValues registro = new ContentValues();
                registro.put("lecturaFinal", lecturaFinal);
                registro.put("fechaModificacion", fechaModificacion);

                int cant = bd.update("lecturas", registro, "codigo='" + codigoSistema+"'", null);
                bd.close();
                if (cant == 1){
                    Toast.makeText(this, "SE INGRESO LECTURA FINAL EXITOSAMENTE", Toast.LENGTH_SHORT)
                            .show();
                    //try {
                    //    Thread.sleep(1000);
                    //} catch (InterruptedException e) {
                    //    e.printStackTrace();
                   // }
                    //regresando


                    //Intent intent1 = new Intent(DetailReadings.this, FindList.class);
                    //startActivity(intent1);
                    super.onBackPressed();
                }

                else {
                    Toast.makeText(this, "NO EXISTE LECTURAS CON EL CODIGO INGRESADO",
                            Toast.LENGTH_SHORT).show();
                }


            }
            else
            {
                Toast.makeText(this, "NO SE PUEDE INGRESAR UNA LECTURA MENOR A LA INICIAL",
                        Toast.LENGTH_SHORT).show();
            }
        }





}
