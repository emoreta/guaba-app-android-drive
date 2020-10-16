package media.extreme.com.brothercorpappmovil;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
//import androidx.core.app.;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
//import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class ConfigList extends Fragment {

    ListView listView;

    ArrayList< String>arrayList; // list of the strings that should appear in ListView

    ArrayAdapter arrayAdapter; // a middle man to bind ListView and array list

    private Button btnDeleteGroup;
    private String grupo="";
    private String nameGroup="";



    public ConfigList() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_config_list, container, false);
        //return rootView;

        View rootView = inflater.inflate(R.layout.fragment_config_list, container, false);

        return rootView;
    }

    public void updateList(String nameGrupo)
    {
        try {
            Log.d("EDISON","ACTUALIZANDO"+nameGrupo);
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity(),
                    "administracion", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();

            ContentValues registroUpdate = new ContentValues();
            registroUpdate.put("estado", 1);

            int cant = bd.update("grupoLecturas", registroUpdate, "grupo='" + nameGrupo + "'", null);

            ContentValues registroUpdateAfter = new ContentValues();
            registroUpdateAfter.put("estado", 0);

            int cantBefore = bd.update("grupoLecturas", registroUpdateAfter, "grupo!='" + nameGrupo + "'", null);


            Cursor c=bd.rawQuery("SELECT grupo,fecha,estado FROM grupoLecturas order by fecha desc", null);

            if(c.moveToFirst())
            {
                do
                {

                    Log.d("EDISON","valores:"+c.getString(c.getColumnIndex("estado"))+"; "+c.getString(c.getColumnIndex("grupo")));


                }while(c.moveToNext());//Move the cursor to the next row.
            }


            bd.close();
        }
        catch (Exception e)
        {
            Log.e("EDISON",e.toString());
        }
    }
    public void deleteGrupo(String grupo)
    {

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity(),
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        bd.delete("grupoLecturas", "grupo='" + grupo+"'", null);
        bd.close();
    }
    public void deleteDataReadings(String grupo)
    {

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity(),
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        bd.delete("lecturas", "grupo='" + grupo+"'", null);
        bd.close();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        listView = (ListView)getView().findViewById(R.id.lstDemo);
        btnDeleteGroup=(Button) getView().findViewById(R.id.btnDeleteGroup);



        buildData();

        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_single_choice,arrayList);

        listView.setAdapter(arrayAdapter);
        Log.d("EDISON","GRUPO"+nameGroup);

        //listView.setItemChecked(0,true);
        String tag = "asdf";
        View v;
        EditText ee;
        StringBuilder b = new StringBuilder();
        int count = listView.getAdapter().getCount();
        for (int i = 0; i < count; i++) {
            //b.append(arrayAdapter.getItem(i));
            Log.d("EDISON","index"+i+"value"+arrayAdapter.getItem(i));
            if(arrayAdapter.getItem(i).equals(nameGroup)) {
                listView.setItemChecked(i, true);
            }

        }


        btnDeleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Put up the Yes/No message box

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder
                        .setTitle("ELIMINAR GRUPO "+grupo)
                        .setMessage("Esta seguro ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Yes button clicked, do something
                                //Toast.makeText(getActivity(), "Yes button pressed", Toast.LENGTH_SHORT).show();

                                if(grupo.equals(""))
                                {
                                    Toast.makeText(getActivity(), "No se selecciono ningun grupo", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    deleteGrupo(grupo);
                                    deleteDataReadings(grupo);
                                    buildData();

                                    arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_single_choice,arrayList);

                                    listView.setAdapter(arrayAdapter);
                                }


                            }
                        })
                        .setNegativeButton("No", null)						//Do nothing on no
                        .show();
                //--------------


            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int posicion = position;
                Log.d("EDISON","dentro de list"+posicion);
                try{
                    buildData();

                    String smsMessageStr = (String) arrayAdapter.getItem(posicion);
                    grupo=smsMessageStr;

                    Toast.makeText(getActivity(), smsMessageStr, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getActivity(), "No data found", Toast.LENGTH_LONG).show();

                    updateList(smsMessageStr);
                    //HashMap<String, String> data = (HashMap<String, String>) listView.getItemAtPosition(posicion);
                    //for(Map.Entry entry:data.entrySet()){
                    //    Log.d("EDISON",entry.getKey().toString()+":"+entry.getValue().toString());
                    //}
                    CheckedTextView textView = (CheckedTextView) view;
                    for (int i = 0; i < listView.getCount(); i++) {
                        textView= (CheckedTextView) listView.getChildAt(i);
                        if (textView != null) {
                            textView.setTextColor(Color.GREEN);
                        }

                    }
                    listView.invalidate();
                    textView = (CheckedTextView) view;
                    if (textView != null) {
                        textView.setTextColor(Color.BLUE);
                    }


                }
                catch(Exception e)
                {
                    Log.d("EDISON",e.toString());
                }

            }
        });



    }


    private boolean buildData() {

        boolean val=false;
        arrayList = new ArrayList();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity(),
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor c=bd.rawQuery("SELECT grupo,fecha,estado FROM grupoLecturas order by fecha desc", null);

        if(c.moveToFirst())
        {
            do
            {
                arrayList.add(c.getString(c.getColumnIndex("grupo")));

                Log.d("EDISON","ESTADO"+c.getString(c.getColumnIndex("estado")));
                if(c.getString(c.getColumnIndex("estado")).equals("1"))
                {
                    nameGroup=c.getString(c.getColumnIndex("grupo"));
                }
                //list.add(putData(c.getString(c.getColumnIndex("cliente")),c.getString(c.getColumnIndex("detail"))));
                 val=true;
            }while(c.moveToNext());//Move the cursor to the next row.
        }
        else
        {
            Toast.makeText(getActivity(), "No existe registros", Toast.LENGTH_LONG).show();

        }
        bd.close();
        return true;
    }





}
