package media.extreme.com.brothercorpappmovil;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//cambio 24072020

import android.widget.ArrayAdapter;


public class FindList extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    ListView lv;

    SearchView sv;

    Spinner sp;

    String[] teams={"Man Utd","Man City","Chelsea","Arsenal","Liverpool","Totenham"};
    ArrayAdapter<String> adapterArray;
    ArrayList<Map<String, String>> listItem;

    SimpleAdapter adapter;

    String[] country = { "India", "USA", "China", "Japan", "Other"};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv=(ListView)findViewById(R.id.miLista);
        sv=(SearchView) findViewById(R.id.searchView1);
        sp=(Spinner) findViewById(R.id.spinner1);

        //list=new ArrayList<String>();


        //lv.setTextFilterEnabled(true);



        //adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,teams);
        //lv.setAdapter(adapter);

        //agregado emoreta 24072020
        Spinner spin1=(Spinner) findViewById(R.id.spinner1);
        spin1.setOnItemSelectedListener(this);
        ArrayAdapter adapterSp=new ArrayAdapter<>(FindList.this, android.R.layout.simple_spinner_item, getParroquias());
        adapterSp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adapterSp);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String text) {
                // TODO Auto-generated method stub
                return false;
            }
            @Override
            public boolean onQueryTextChange(String text) {

                adapter.getFilter().filter(text);

                return false;
            }

        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("EDISON","SELECCIONANDO");
                try
                {
                    int posicion = position;


                    HashMap<String, String> list;


                    HashMap<String, String> data = (HashMap<String, String>) lv.getItemAtPosition(position);
                    Intent intent = new Intent(FindList.this,DetailReadings.class);

                    for(Map.Entry entry:data.entrySet()){
                        System.out.print(entry.getKey() + " : " + entry.getValue());
                        intent.putExtra(entry.getKey().toString(),entry.getValue().toString());

                        Log.d("EDISON",entry.getKey().toString()+":"+entry.getValue().toString());

                    }

                    // Aquí pasaremos el parámetro de la intención creada previamente
                    startActivity(intent);

                }
                catch(Exception ex)
                {
                    Log.d("EDISON",ex.toString());
                }
            }
        });


        }

    private HashMap<String, String> putData(String name, String detail) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("name", name);
        item.put("detail", detail);
        return item;
    }
    private String getNameGroup(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String nameGroup="";
        Cursor c=bd.rawQuery("SELECT grupo,estado FROM grupoLecturas where estado=1", null);

        if(c.moveToFirst())
        {
            do
            {
                nameGroup=c.getString(c.getColumnIndex("grupo"));
            }while(c.moveToNext());//Move the cursor to the next row.
        }
        else
        {
            Toast.makeText(this, "No existe registros", Toast.LENGTH_LONG).show();

        }
      return nameGroup;

    }
    private ArrayList<String> getParroquias(){
        ArrayList<String> listZona = new ArrayList<String>();
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor czona=bd.rawQuery("SELECT  DISTINCT zona  as zona FROM lecturas where grupo='"+ getNameGroup()+"'", null);
        listZona.add("--TODOS--");
        if(czona.moveToFirst())
        {
            do
            {
                //Log.d("EDISON","columnas"+c.getString(c.getColumnIndex("detail")));
                listZona.add(czona.getString(czona.getColumnIndex("zona")));
            }while(czona.moveToNext());//Move the cursor to the next row.
        }
        else
        {
            Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show();

        }


        return listZona;
    }

    private ArrayList<Map<String, String>> buildData() {
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

        //ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

        Log.d("EDISON","NOMBRE GRUPO"+getNameGroup());

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor c=bd.rawQuery("SELECT  cliente,( numero || '-' || periodo || '-' || lecturaInicial || '-' || lecturaFinal || '-' || codigo || '-' ||  zona ) as detail FROM lecturas where grupo='"+ getNameGroup()+"'", null);

        if(c.moveToFirst())
        {
            do
            {
                Log.d("EDISON","columnas"+c.getString(c.getColumnIndex("detail")));
                list.add(putData(c.getString(c.getColumnIndex("cliente")),c.getString(c.getColumnIndex("detail"))));
            }while(c.moveToNext());//Move the cursor to the next row.
        }
        else
        {
            Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show();

        }

        return list;
    }
    private ArrayList<Map<String, String>> buildDataZone(String zone) {
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

        //ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

        Log.d("EDISON","NOMBRE GRUPO"+getNameGroup());

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor c=null;

        if(zone!="--TODOS--") {

            c = bd.rawQuery("SELECT  cliente,( numero || '-' || periodo || '-' || lecturaInicial || '-' || lecturaFinal || '-' || codigo || '-' ||  zona ) as detail FROM lecturas where grupo='" + getNameGroup() + "' and zona='" + zone + "'", null);
        }
        else
        {
            c = bd.rawQuery("SELECT  cliente,( numero || '-' || periodo || '-' || lecturaInicial || '-' || lecturaFinal || '-' || codigo || '-' ||  zona ) as detail FROM lecturas where grupo='" + getNameGroup() + "'", null);
        }
        if(c.moveToFirst())
        {
            do
            {
                Log.d("EDISON","columnas"+c.getString(c.getColumnIndex("detail")));
                list.add(putData(c.getString(c.getColumnIndex("cliente")),c.getString(c.getColumnIndex("detail"))));
            }while(c.moveToNext());//Move the cursor to the next row.
        }
        else
        {
            Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show();

        }

        return list;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(),getParroquias().get(position) , Toast.LENGTH_LONG).show();

        listItem = buildDataZone(getParroquias().get(position));
        String[] from = { "name", "detail" };
        int[] to = { android.R.id.text1, android.R.id.text2 };

        adapter = new SimpleAdapter(this, listItem,
                android.R.layout.simple_list_item_activated_2, from, to);

        lv.setAdapter(adapter);

        //adapter.getFilter().filter(getParroquias().get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
