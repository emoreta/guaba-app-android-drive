package media.extreme.com.brothercorpappmovil;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
//import androidx.core.app.Fragment;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;



public class List extends Fragment {

    private Button btnFind;
    private EditText txtBuscar;

    private android.widget.ListView listView;
    private ArrayList<String> list;
    ArrayList<Map<String, String>> listItem;

    SimpleAdapter adapter;

    ArrayList<Map<String, String>> listViewDetail = new ArrayList<Map<String, String>>();

    public List() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        Log.d("EDISON","INICIANDO LIST ON ACTIVITY");

        //Conectamos miLista a mi ListView
        listView = (android.widget.ListView)getView().findViewById(R.id.miLista);
        txtBuscar = (EditText)getView().findViewById(R.id.inputSearch);

        list=new ArrayList<String>();

        listItem = buildData();
        String[] from = { "name", "detail" };
        int[] to = { android.R.id.text1, android.R.id.text2 };

         adapter = new SimpleAdapter(getActivity(), listItem,
                android.R.layout.simple_list_item_2, from, to);

        listView.setAdapter(adapter);


        txtBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("EDISON","SELECCIONANDO");
                try
                {
                int posicion = position;


                HashMap<String, String> list;


                HashMap<String, String> data = (HashMap<String, String>) listView.getItemAtPosition(position);
                    Intent intent = new Intent(getActivity(),DetailReadings.class);

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

    private void findClickData(String dataFind){

        Log.d("EDISON","CLICK EN BOTON");

        try {

            ArrayList<Map<String, String>> listFind = findData(dataFind);
            String[] from = {"name", "detail"};
            int[] to = {android.R.id.text1, android.R.id.text2};
            SimpleAdapter adapter = new SimpleAdapter(getActivity(), listFind,
                    android.R.layout.simple_list_item_2, from, to);

            listView.setAdapter(adapter);
        }
        catch (Exception e)
        {
            Log.d("EDISON",e.toString());
        }

    }

    private ArrayList<Map<String, String>> findData(String dataFind) {

        Log.d("EDISON","busqueda: "+dataFind);
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity(),
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor c=bd.rawQuery("SELECT cliente,(codigo || '-' || periodo || '-' || lecturaInicial || '-' || lecturaFinal || '-' || fecha) as detail " +
                "FROM lecturas WHERE cliente LIKE '%"+dataFind+"%' OR codigo LIKE '%"+dataFind+"%' ", null);
        if(c.moveToFirst())
        {
            do
            {
                list.add(putData(c.getString(c.getColumnIndex("cliente")),c.getString(c.getColumnIndex("detail"))));
            }while(c.moveToNext());//Move the cursor to the next row.
        }
        else
        {
            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_LONG).show();

        }



        return list;

    }

    private HashMap<String, String> putData(String name, String detail) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("name", name);
        item.put("detail", detail);
        return item;
    }
    private ArrayList<Map<String, String>> buildData() {
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

        //ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity(),
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor c=bd.rawQuery("SELECT cliente,(numero || '-' || periodo || '-' || lecturaInicial || '-' || lecturaFinal || '-' || fecha) as detail FROM lecturas", null);

        if(c.moveToFirst())
        {
            do
            {
                list.add(putData(c.getString(c.getColumnIndex("cliente")),c.getString(c.getColumnIndex("detail"))));
            }while(c.moveToNext());//Move the cursor to the next row.
        }
        else
        {
            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_LONG).show();

        }

        return list;
    }

    public ArrayList<String> DisplayData()
    {


        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity(),
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor c=bd.rawQuery("SELECT cliente,(numero || '-' || periodo || '-' || lecturaInicial || '-' || lecturaFinal || '-' || fecha) as detail FROM lecturas", null);

        if(c.moveToFirst())
        {
            do
            {


                //Log.d("EDISON",c.getString(c.getColumnIndex("cliente")));

                list.add(c.getString(c.getColumnIndex("fila")));
                //listViewDetail.add(putData(c.getString(c.getColumnIndex("cliente")),c.getString(c.getColumnIndex("detail"))));
                //data();


            }while(c.moveToNext());//Move the cursor to the next row.
        }
        else
        {
            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_LONG).show();

        }
        //Select query
        return list;
        //return db.query(TABLE_NAME, new String[]{NAME, ROLL,COURSE}, null, null, null, null, null);
    }

}
