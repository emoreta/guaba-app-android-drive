package media.extreme.com.brothercorpappmovil;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
/*import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;*/

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class Drive extends AppCompatActivity  implements GoogleApiClient.OnConnectionFailedListener {
    private final static String LOGTAG = "EDISON";

    protected static final int REQ_CREATE_FILE = 1001;
    protected static final int REQ_OPEN_FILE = 1002;
    /*agregados 23092020*/
    private static final int REQUEST_CODE_SIGN_IN = 1;
    private static final int REQUEST_CODE_OPEN_DOCUMENT = 2;
    private DriveServiceHelper mDriveServiceHelper;

    /*----fin modificacion 23092020*/

    //private GoogleApiClient apiClient;
    private Button btnLeerFichero,btnSalidaFichero;
    private TextView lblRegistros,lblArchivoSalida,lblGrupo,lblGrupoSalida,lblRegistrosSalida;
    private ProgressBar pbarProgreso,pbarProgresoSalida;

    private int numRegistros=0;

    ArrayList<String> listRegistrosFinal = new ArrayList<String> ();

    private MiTareaAsincrona tarea1,tarea2;

    private String periodo="";

    private String mOpenFileId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //obtencion de metadata
        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();*/

        //-----------------------
        /*apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(com.google.android.gms.drive.Drive.API)
                .addScope(com.google.android.gms.drive.Drive.SCOPE_FILE)
                //.addScope(Drive.SCOPE_APPFOLDER)
                .build();*/



        btnLeerFichero= (Button)findViewById(R.id.btnSyncIn);
        btnSalidaFichero= (Button)findViewById(R.id.btnSalida);
        lblRegistros=(TextView)findViewById(R.id.lblRegistros);
        lblGrupo=(TextView)findViewById(R.id.lblGrupo);
        lblGrupoSalida=(TextView)findViewById(R.id.lblGrupoSalida);

        lblRegistrosSalida=(TextView)findViewById(R.id.txtView4);

        pbarProgreso=(ProgressBar)findViewById(R.id.pbarProgreso);


        lblArchivoSalida=(TextView)findViewById(R.id.lblArchivoSalida);

        btnLeerFichero.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {
                        //comentado por nueva version 23/09/2020
                        //openFileWithActivity();
                        openFilePicker();

                    }
                }.start();
            }

        });



        btnSalidaFichero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        StringBuilder sbuilder = new StringBuilder();
                        sbuilder=readGroupActive();

                Log.d(LOGTAG, "Informacion a cargar"+sbuilder);

                        //comentado por nueva version 23/09/2020
                        //createFile(periodo+'_'+getNameGroup()+".txt",sbuilder);
                        /*createFile();
                        try {
                            Thread.sleep(4000);
                        } catch(InterruptedException e) {}

                        saveFile(periodo+'_'+getNameGroup()+".txt",sbuilder.toString());*/
                      createSaveFile(periodo+'_'+getNameGroup()+".txt",sbuilder.toString());

                            /*if (mDriveServiceHelper != null && mOpenFileId != null) {
                                    Log.d(LOGTAG, "Saving " + mOpenFileId);

                                    mDriveServiceHelper.saveFile(mOpenFileId, periodo+'_'+getNameGroup()+".txt", sbuilder.toString())
                                            .addOnFailureListener(exception ->
                                                    Log.e(LOGTAG, "Unable to save file via REST.", exception));
                                }*/



                        lblArchivoSalida.setText(periodo+"_"+getNameGroup()+".txt");
                        lblGrupoSalida.setText(getNameGroup());
                        lblRegistrosSalida.setText(String.valueOf(numRegistros));
                        tarea2 = new MiTareaAsincrona();
                        tarea2.execute();


            }
        });

        requestSignIn();



    }
    private void requestSignIn() {//agregado nuevo metodo de login con google 23/09/2020
        Log.d(LOGTAG, "Requesting sign-in");

        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                        .build();
        GoogleSignInClient client = GoogleSignIn.getClient(this, signInOptions);

        // The result of the sign-in Intent is handled in onActivityResult.
        startActivityForResult(client.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }
    /**
     * Handles the {@code result} of a completed sign-in activity initiated from {@link
     * #requestSignIn()}.
     */
    private void handleSignInResult(Intent result) {//agregado nuevo metodo de login con google 23/09/2020
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(googleAccount -> {
                    Log.d(LOGTAG, "Signed in as " + googleAccount.getEmail());

                    // Use the authenticated account to sign in to the Drive service.
                    GoogleAccountCredential credential =
                            GoogleAccountCredential.usingOAuth2(
                                    this, Collections.singleton(DriveScopes.DRIVE_FILE));
                    credential.setSelectedAccount(googleAccount.getAccount());
                    com.google.api.services.drive.Drive googleDriveService =
                            new com.google.api.services.drive.Drive.Builder(
                                    AndroidHttp.newCompatibleTransport(),
                                    new GsonFactory(),
                                    credential)
                                    .setApplicationName("Drive API Migration")
                                    .build();

                    // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                    // Its instantiation is required before handling any onClick actions.
                    mDriveServiceHelper = new DriveServiceHelper(googleDriveService);
                })
                .addOnFailureListener(exception -> Log.e(LOGTAG, "Unable to sign in.", exception));
    }
    /**
     * Opens the Storage Access Framework file picker using {@link #REQUEST_CODE_OPEN_DOCUMENT}.
     */
    private void openFilePicker() {
        if (mDriveServiceHelper != null) {
            Log.d(LOGTAG, "Opening file picker.");

            Intent pickerIntent = mDriveServiceHelper.createFilePickerIntent();

            // The result of the SAF Intent is handled in onActivityResult.
            startActivityForResult(pickerIntent, REQUEST_CODE_OPEN_DOCUMENT);
        }
    }
    /**
     * Opens a file from its {@code uri} returned from the Storage Access Framework file picker
     * initiated by {@link #openFilePicker()}.
     */
    private void openFileFromFilePicker(Uri uri) {
        if (mDriveServiceHelper != null) {
            Log.d(LOGTAG, "Opening " + uri.getPath());

            mDriveServiceHelper.openFileUsingStorageAccessFramework(getContentResolver(), uri)
                    .addOnSuccessListener(nameAndContent -> {
                        String name = nameAndContent.first;
                        String content = nameAndContent.second;

                        //readFile(driveId);

                        /*lectura e ingreso de informacion 23092020*/
                        Reader inputString = new StringReader(content);
                        BufferedReader reader =
                                new BufferedReader(inputString);
                        Log.d(LOGTAG, "Opening data :" + nameAndContent);

                        StringBuilder builder = new StringBuilder();

                        ArrayList<String> listRegistros = new ArrayList<String> ();

                        try {
                            String line;
                            int x=1;
                            while ((line = reader.readLine()) != null) {
                                lblRegistros.setText(String.valueOf(x));
                                listRegistros.add(line);
                                //Log.e(LOGTAG,"LINEA"+line);
                                //builder.append(line);
                                //Log.e(LOGTAG,builder.toString());
                                x++;
                            }
                        } catch (IOException e) {
                            //Log.e(LOGTAG,"Error al leer fichero");
                        }

                        int numeroRegistros=listRegistros.size()-1;//SE RESTA UNO PORQUE ES EL ENCABEZADO LA LAECTURA SE DEBERIA EMPEZAR DESDE LA POSICION 1,LA 0 ES EL ENCABEZADO
                        numRegistros=numeroRegistros;

                        listRegistrosFinal=listRegistros;

                        Log.i(LOGTAG, "NUMERO REGISTRPS: " + numeroRegistros);
                        Log.i(LOGTAG, "NUMERO REGISTRPS: " + listRegistros.get(1));//primer registro
                        Log.i(LOGTAG, "NUMERO REGISTRPS: " + listRegistros.get(numeroRegistros));//ultimo registro

                        Log.i(LOGTAG, "FINALIZANDO LECTURA DE ARCHIVO");//ultimo registro

                        //content.discard(apiClient);

                        lblRegistros.setText(String.valueOf(numeroRegistros));
                        Log.e(LOGTAG, "----INICIANDO INSERT DATOS----");
                        String nameGroup="";
                        try
                        {


                            nameGroup=insertBdGrupo();
                            lblGrupo.setText(nameGroup);
                            insertBdList(listRegistrosFinal,nameGroup);
                        }
                        catch(Exception e)
                        {
                            Log.e(LOGTAG, "ERROR EN INSERT"+e );
                        }

                        /*---fin lectura 23092020*/

                        //mFileTitleEditText.setText(name);
                        //mDocContentEditText.setText(content);

                        // Files opened through SAF cannot be modified.
                        setReadOnlyMode();
                    })
                    .addOnFailureListener(exception ->
                            Log.e(LOGTAG, "Unable to open file from picker.", exception));
        }
    }

    /**
     * Creates a new file via the Drive REST API.
     */
    private void createFile() {
        if (mDriveServiceHelper != null) {
            Log.d(LOGTAG, "Creating a file.");

            mDriveServiceHelper.createFile()
                    .addOnSuccessListener(fileId -> readFile(fileId))
                    .addOnFailureListener(exception ->
                            Log.e(LOGTAG, "Couldn't create file.", exception));
        }
    }

    /**
     * Retrieves the title and content of a file identified by {@code fileId} and populates the UI.
     */
    private void readFile(String fileId) {
        if (mDriveServiceHelper != null) {
            Log.d(LOGTAG, "Reading file " + fileId);

            mDriveServiceHelper.readFile(fileId)
                    .addOnSuccessListener(nameAndContent -> {
                        //String name = nameAndContent.first;
                        //String content = nameAndContent.second;

                        //mFileTitleEditText.setText(name);
                        //mDocContentEditText.setText(content);

                        setReadWriteMode(fileId);
                    })
                    .addOnFailureListener(exception ->
                            Log.e(LOGTAG, "Couldn't read file.", exception));
        }
    }


    /**
     * Saves the currently opened file created via {@link #createFile()} if one exists.
     */
    private void saveFile(String fileName,String fileContent) {
        Log.d(LOGTAG, "FUNCION GUARDAR " + mOpenFileId);

        if (mDriveServiceHelper != null && mOpenFileId != null) {
            Log.d(LOGTAG, "Saving " + mOpenFileId);

            //String fileName = mFileTitleEditText.getText().toString();
            //String fileContent = mDocContentEditText.getText().toString();

            mDriveServiceHelper.saveFile(mOpenFileId, fileName, fileContent)
                    .addOnFailureListener(exception ->
                            Log.e(LOGTAG, "Unable to save file via REST.", exception));
        }
    }

    private void createSaveFile(String fileName,String fileContent) {

            mDriveServiceHelper.createSaveFile( fileName, fileContent)
                    .addOnFailureListener(exception ->
                            Log.e(LOGTAG, "Unable to create save file via REST.", exception));

    }


    /**
     * Queries the Drive REST API for files visible to this app and lists them in the content view.
     */
    private void query() {
        if (mDriveServiceHelper != null) {
            Log.d(LOGTAG, "Querying for files.");

            mDriveServiceHelper.queryFiles()
                    .addOnSuccessListener(fileList -> {
                        StringBuilder builder = new StringBuilder();
                        for (com.google.api.services.drive.model.File file : fileList.getFiles()) {
                            builder.append(file.getName()).append("\n");
                        }
                        String fileNames = builder.toString();

                        //mFileTitleEditText.setText("File List");
                        //mDocContentEditText.setText(fileNames);

                        setReadOnlyMode();
                    })
                    .addOnFailureListener(exception -> Log.e(LOGTAG, "Unable to query files.", exception));
        }
    }

    /**
     * Updates the UI to read-only mode.
     */
    private void setReadOnlyMode() {
        //mFileTitleEditText.setEnabled(false);
        //mDocContentEditText.setEnabled(false);
        mOpenFileId = null;
    }

    /**
     * Updates the UI to read/write mode on the document identified by {@code fileId}.
     */
    private void setReadWriteMode(String fileId) {
        Log.i(LOGTAG, "setReadWriteMode "+fileId);

        //mFileTitleEditText.setEnabled(true);
        //mDocContentEditText.setEnabled(true);
        mOpenFileId = fileId;
    }

    private void tareaLarga()
    {
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {}
    }
    private class MiTareaAsincrona extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            for(int i=1; i<=10; i++) {
                tareaLarga();

                publishProgress(i*10);

                if(isCancelled())
                    break;
            }

            return true;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();

            pbarProgreso.setProgress(progreso);
        }

        @Override
        protected void onPreExecute() {
            pbarProgreso.setMax(100);
            pbarProgreso.setProgress(0);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
                Toast.makeText(Drive.this, "Tarea finalizada!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(Drive.this, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
        }
    }

    //ABRIR ARCHIVO CON ACTIVITY
//comentando por cambio en version 23/09/2020
    private void openFileWithActivity() {

        /*IntentSender intentSender = com.google.android.gms.drive.Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setMimeType(new String[] { "text/plain" })
                .build(apiClient);

        try {
            startIntentSenderForResult(
                    intentSender,REQ_OPEN_FILE, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            Log.e(LOGTAG, "Error al iniciar actividad: Open File", e);
        }*/
    }

    //LEER ARCHIVO
    //comentado version anterios 23/09/2020
     /*private  void readFile(DriveId fileDriveId) {


       listRegistrosFinal=null;

        DriveFile file = fileDriveId.asDriveFile();


        file.open(apiClient, DriveFile.MODE_READ_ONLY, null)
                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                    @Override
                    public void onResult(DriveApi.DriveContentsResult result) {
                        if (!result.getStatus().isSuccess()) {
                            Log.e(LOGTAG,"Error al abrir fichero (readFile)");
                            return;
                        }



                        //showMessage("Metadata succesfully fetched. Title: " + metadata.getTitle());
                        DriveContents contents = result.getDriveContents();

                        //InputStream is = contents.getInputStream();
                        //Log.i(LOGTAG, "NOMBRE ARCHIVO: " + is.);
                        tarea1 = new MiTareaAsincrona();
                        tarea1.execute();


                        BufferedReader reader =
                                new BufferedReader(

                                        new InputStreamReader(contents.getInputStream()));

                        StringBuilder builder = new StringBuilder();

                        ArrayList<String> listRegistros = new ArrayList<String> ();

                        try {
                            String line;
                            int x=1;
                            while ((line = reader.readLine()) != null) {
                                lblRegistros.setText(String.valueOf(x));
                                listRegistros.add(line);
                                //Log.e(LOGTAG,"LINEA"+line);
                                //builder.append(line);
                                //Log.e(LOGTAG,builder.toString());
                                x++;
                            }
                        } catch (IOException e) {
                            //Log.e(LOGTAG,"Error al leer fichero");
                        }

                        int numeroRegistros=listRegistros.size()-1;//SE RESTA UNO PORQUE ES EL ENCABEZADO LA LAECTURA SE DEBERIA EMPEZAR DESDE LA POSICION 1,LA 0 ES EL ENCABEZADO
                        numRegistros=numeroRegistros;

                        listRegistrosFinal=listRegistros;

                        Log.i(LOGTAG, "NUMERO REGISTRPS: " + numeroRegistros);
                        Log.i(LOGTAG, "NUMERO REGISTRPS: " + listRegistros.get(1));//primer registro
                        Log.i(LOGTAG, "NUMERO REGISTRPS: " + listRegistros.get(numeroRegistros));//ultimo registro

                        Log.i(LOGTAG, "FINALIZANDO LECTURA DE ARCHIVO");//ultimo registro

                        contents.discard(apiClient);

                        lblRegistros.setText(String.valueOf(numeroRegistros));
                        Log.e(LOGTAG, "----INICIANDO INSERT DATOS----");
                        String nameGroup="";
                        try
                        {


                            nameGroup=insertBdGrupo();
                            lblGrupo.setText(nameGroup);
                            insertBdList(listRegistrosFinal,nameGroup);
                        }
                        catch(Exception e)
                        {
                            Log.e(LOGTAG, "ERROR EN INSERT"+e );
                        }

                        //Log.i(LOGTAG, "Fichero leido: " + builder.toString());
                    }
                });

    }*/

    public void alta(View v) {
        String nameGroup="";
        try
        {
            nameGroup=insertBdGrupo();
            insertBdList(listRegistrosFinal,nameGroup);
        }
        catch(Exception e)
        {
            Log.i(LOGTAG, "ERROR EN INSERT"+e );
        }

    }
    public String insertBdGrupo()
    {
        Log.i(LOGTAG, "INSERTANDO GRUPO" );
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Date date=new Date();

        String nameGrupo="GRUPO_"+dateFormat.format(date);
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("grupo", nameGrupo);
        registro.put("fecha",getDateTime());
        registro.put("estado",1);

        bd.insert("grupoLecturas", null, registro);

        //---update grupos

        ContentValues registroUpdate = new ContentValues();
        registroUpdate.put("estado", 0);


        int cant = bd.update("grupoLecturas", registroUpdate, "grupo!='" + nameGrupo+"'", null);



        bd.close();

        return nameGrupo;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date=new Date();
        return dateFormat.format(date);
    }

    private String geHostName() {

        //String str = SystemProperties.get("net.hostname");
        String deviceName = Settings.System.getString(getContentResolver(), "device_name");
        //String deviceName="PRUEBA";
        return deviceName;
    }

    public void insertBdList(ArrayList<String> listBd,String nameGroup){
        //listo para inserta en bd

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        int tamano=0;
        Log.i(LOGTAG, "INICIANDO INSERT EN BD" );
        tamano=listBd.size()-1;
        String[] parts;
               // = string.split("-");

        for(int i=1;i<=tamano;i++){
            //lblInsercion.setText(String.valueOf(i));

            Log.i(LOGTAG, "REGISTROS A INSERTAR"+ listBd.get(i));
            parts=listBd.get(i).split(";");
            ContentValues registro = new ContentValues();

            //registro.put("codigo", parts[0]);
            registro.put("periodo", parts[4]);
            registro.put("cliente", parts[1]);
            registro.put("lecturaInicial", parts[2]);
            registro.put("lecturaFinal", parts[3]);
            registro.put("equipo", geHostName());
            registro.put("fecha", getDateTime());
            registro.put("grupo", nameGroup);
            registro.put("numero", parts[0]);
            registro.put("zona", parts[5]);

            bd.insert("lecturas", null, registro);
        }
        bd.close();


    }
    private void createFolder(){
        File nuevaCarpeta = new File(getFilesDir(), "miCarpetaEdison");
        nuevaCarpeta.mkdirs();

        Log.d("EDISON",getFilesDir().toString());

        //File f = new File(Environment.getExternalStorageDirectory() + "/cualquierCarpeta");

        //Log.d("EDISON",Environment.getExternalStorageDirectory().toString());
        // Comprobamos si la carpeta está ya creada

        // Si la carpeta no está creada, la creamos.

        //if(!f.isDirectory()) {
        //    Log.d("EDISON","CREANDO CARPETA");
        //    String newFolder = "/BrotherCorp"; //cualquierCarpeta es el nombre de la Carpeta que vamos a crear
        //    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        //    File myNewFolder = new File(extStorageDirectory + newFolder);
        //    myNewFolder.mkdir(); //creamos la carpeta
        //}else{
        //    Log.d("EDISON","La carpeta ya estaba creada");
        //}

    }
    private StringBuilder readGroupActive(){

        StringBuilder sbuilder = new StringBuilder();
        String grupo="";
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor d=bd.rawQuery("SELECT grupo,estado FROM grupoLecturas where estado=1 LIMIT 1", null);
        if(d.moveToFirst())
        {
            do
            {
                grupo=d.getString(d.getColumnIndex("grupo"));

        }while(d.moveToNext());//Move the cursor to the next row.
        }
        else
        {
            Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show();

        }


        Cursor c=bd.rawQuery("SELECT codigo,periodo,cliente,lecturaInicial,lecturaFinal,equipo," +
                "fecha,fechaModificacion,grupo,numero FROM lecturas where grupo='"+getNameGroup()+"'order by codigo asc", null);

        Log.i(LOGTAG, "numero de registros EN BUFFER :"+c.getCount());
        numRegistros=c.getCount();

        if(c.moveToFirst())
        {
            do
            {
                periodo=c.getString(c.getColumnIndex("periodo")).toString();

                sbuilder.append(c.getString(c.getColumnIndex("periodo"))+";"+c.getString(c.getColumnIndex("numero"))+";"+c.getString(c.getColumnIndex("cliente"))
                        +";"+c.getString(c.getColumnIndex("lecturaInicial"))+";"+c.getString(c.getColumnIndex("lecturaFinal"))
                        +";"+c.getString(c.getColumnIndex("equipo"))+";"+c.getString(c.getColumnIndex("fecha"))
                        +";"+c.getString(c.getColumnIndex("fechaModificacion"))+";"+c.getString(c.getColumnIndex("grupo"))+";"+c.getString(c.getColumnIndex("codigo"))+";\n");
                //sbuilder.append(System.getProperty("line.separator"));
                //arrayList.add(c.getString(c.getColumnIndex("grupo")));
                //list.add(putData(c.getString(c.getColumnIndex("cliente")),c.getString(c.getColumnIndex("detail"))));
                //val=true;
            }while(c.moveToNext());//Move the cursor to the next row.
        }
        else
        {
            Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show();

        }

        Log.i(LOGTAG, "LECTURA EN BUFFER :"+sbuilder.toString());
        return sbuilder;

    }


    private void createFileRespaldo()//que coloca respaldo ya que se crea nuevo metodo con el mismo nombre en version 23/09/2020
    {

        try
        {

            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            openFileOutput("prueba_int.txt", Context.MODE_PRIVATE));

            fout.write("Texto de prueba.");
            fout.close();
            Log.i(LOGTAG, "CREANDO ARCHIVO" );
        }
        catch (Exception ex)
        {
            Log.e("EDISON", "Error al escribir fichero a memoria interna");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //comentado por nueva version 236/09/2020
        /* ArrayList<String> listRegistrosResultado = new ArrayList<String> ();
        Drive driveService;
        switch (requestCode) {
            case REQ_CREATE_FILE:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    Log.i(LOGTAG, "Fichero creado con ID = " + driveId);
                }
                break;
            case REQ_OPEN_FILE:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    Log.i(LOGTAG, "Fichero seleccionado ID = " + driveId);

                    readFile(driveId);

                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }*/
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    handleSignInResult(data);
                }
                break;

            case REQUEST_CODE_OPEN_DOCUMENT:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        openFileFromFilePicker(uri);
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    //comentado por cambio api 23/09/2020
    /*private void writeSampleText(DriveContents driveContents,StringBuilder sbu) {

        OutputStream outputStream = driveContents.getOutputStream();

        Writer writer = new OutputStreamWriter(outputStream);

        try {
            writer.write(sbu.toString());
            writer.close();
        } catch (IOException e) {
            Log.e(LOGTAG, "Error al escribir en el fichero: " + e.getMessage());
        }
    }*/
    //comentado por cambio api 23/09/2020
    /*private void createFile(final String filename,final StringBuilder sb) {
        com.google.android.gms.drive.Drive.DriveApi.newDriveContents(apiClient)
                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {

                    @Override
                    public void onResult(DriveApi.DriveContentsResult result) {
                        if (result.getStatus().isSuccess()) {

                            writeSampleText(result.getDriveContents(),sb);

                            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                    .setTitle(filename)
                                    .setMimeType("text/plain")
                                    .build();


                            DriveFolder folder = com.google.android.gms.drive.Drive.DriveApi.getRootFolder(apiClient);


                            folder.createFile(apiClient, changeSet, result.getDriveContents())
                                    .setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {
                                        @Override
                                        public void onResult(DriveFolder.DriveFileResult result) {
                                            if (result.getStatus().isSuccess()) {
                                                Log.i(LOGTAG, "Fichero creado con ID = " + result.getDriveFile().getDriveId());
                                            } else {
                                                Log.e(LOGTAG, "Error al crear el fichero");
                                            }
                                        }
                                    });
                        } else {
                            Log.e(LOGTAG, "Error al crear DriveContents");
                        }
                    }
                });
    }*/
    @Override
    public void onConnectionFailed( ConnectionResult connectionResult) {
        Toast.makeText(this, "Error de conexion!" + connectionResult, Toast.LENGTH_SHORT).show();
        Log.e(LOGTAG, "OnConnectionFailed: " + connectionResult);

    }
    private String getNameGroup(){
        String nameGroup="";
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(Drive.this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor c=bd.rawQuery("SELECT grupo,fecha,estado FROM grupoLecturas order by fecha desc", null);

        if(c.moveToFirst())
        {
            do
            {
                if(c.getString(c.getColumnIndex("estado")).equals("1"))
                {
                    nameGroup=c.getString(c.getColumnIndex("grupo"));
                }

            }while(c.moveToNext());//Move the cursor to the next row.
        }
        else
        {
            Toast.makeText(Drive.this, "No existen registros", Toast.LENGTH_LONG).show();

        }
        bd.close();
        return nameGroup;

    }



}
