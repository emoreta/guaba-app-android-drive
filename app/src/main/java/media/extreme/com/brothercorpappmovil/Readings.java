package media.extreme.com.brothercorpappmovil;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
//import androidx.core.app.Fragment;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Readings extends Fragment {

    private EditText textCodigo,textCliente,textLecturaInicial,textLecturaFinal,textPeriodo;

    public Readings() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_readings, container, false);

        return rootView;
    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date=new Date();
        return dateFormat.format(date);
    }
    private String geHostName() {

        //String str = SystemProperties.get("net.hostname");
        //String deviceName = Settings.System.getString(getContentResolver(), "device_name");
        String deviceName="PRUEBA";
        return deviceName;
    }

    public void alta(View v) {

        String codigo,cliente,lecturaInicial,lecturaFinal,periodo;

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity(), "administracion", null, 1);


        SQLiteDatabase bd = admin.getWritableDatabase();

        codigo=textCodigo.getText().toString();
        cliente=textCliente.getText().toString();
        lecturaInicial=textLecturaInicial.getText().toString();
        lecturaFinal=textLecturaFinal.getText().toString();
        periodo=textPeriodo.getText().toString();

        ContentValues registro = new ContentValues();

        registro.put("codigo", codigo);
        registro.put("periodo", periodo);
        registro.put("cliente", cliente);
        registro.put("lecturaInicial", lecturaInicial);
        registro.put("lecturaFinal", lecturaFinal);
        registro.put("equipo", geHostName());
        registro.put("fecha", getDateTime());

        bd.insert("lecturas", null, registro);
        bd.close();
        //seteo de textbox
        textCodigo.setText("");
        textCliente.setText("");
        textLecturaInicial.setText("");
        textLecturaFinal.setText("");
        textPeriodo.setText("");

        Toast.makeText(getActivity(), "Se guardo lectura correctamente", Toast.LENGTH_SHORT).show();
    }

    public void consultaporcodigo(View v) {
        String codigo;
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity(),
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        codigo = this.textCodigo.getText().toString();
        Cursor fila = bd.rawQuery(
                "select codigo,cliente,lecturaInicial,LecturaFinal from lecturas where codigo=" + codigo, null);
        if (fila.moveToFirst()) {
            this.textCodigo.setText(fila.getString(0));
            this.textCliente.setText(fila.getString(1));
            this.textLecturaInicial.setText(fila.getString(2));
            this.textLecturaFinal.setText(fila.getString(3));
            this.textPeriodo.setText(fila.getString(4));

        } else
            //Toast.makeText(this, "No existe un lectura con dicho código",
            //        Toast.LENGTH_SHORT).show();
        bd.close();
    }
    public void sendMessage(View view) {

        //Intent intent = new Intent(Readings.this,grid.class);
        //startActivity(intent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        Log.d("EDISON","INICIANDO READINGS");

        textCodigo=(EditText)getActivity().findViewById(R.id.textCodigo);
        textCliente=(EditText)getActivity().findViewById(R.id.textCliente);
        textLecturaInicial=(EditText)getActivity().findViewById(R.id.textLecturaInicial);
        textLecturaFinal=(EditText)getActivity().findViewById(R.id.textLecturaFinal);
        textPeriodo=(EditText)getActivity().findViewById(R.id.textPeriodo);

        Button bDone = (Button) getActivity().findViewById(R.id.button);

        Button btnBuscarId=(Button)getActivity().findViewById(R.id.button2) ;

        btnBuscarId.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String codigo;
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity(),
                        "administracion", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();
                codigo = textCodigo.getText().toString();
                Cursor fila = bd.rawQuery(
                        "select codigo,cliente,lecturaInicial,LecturaFinal,periodo from lecturas where codigo=" + codigo, null);
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
        });

        bDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String codigo,cliente,lecturaInicial,lecturaFinal,periodo;

                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity(), "administracion", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();

                codigo=textCodigo.getText().toString();
                cliente=textCliente.getText().toString();
                lecturaInicial=textLecturaInicial.getText().toString();
                lecturaFinal=textLecturaFinal.getText().toString();
                periodo=textPeriodo.getText().toString();

                ContentValues registro = new ContentValues();

                registro.put("codigo", codigo);
                registro.put("periodo", periodo);
                registro.put("cliente", cliente);
                registro.put("lecturaInicial", lecturaInicial);
                registro.put("lecturaFinal", lecturaFinal);
                registro.put("equipo", geHostName());
                registro.put("fecha", getDateTime());

                bd.insert("lecturas", null, registro);
                bd.close();
                //seteo de textbox
                textCodigo.setText("");
                textCliente.setText("");
                textLecturaInicial.setText("");
                textLecturaFinal.setText("");
                textPeriodo.setText("");

                Toast.makeText(getActivity(), "Se guardo lectura correctamente", Toast.LENGTH_SHORT).show();



            }
        });

    }


}
