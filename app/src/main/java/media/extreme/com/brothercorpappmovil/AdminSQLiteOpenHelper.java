package media.extreme.com.brothercorpappmovil;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by edison on 25/04/2017.
 */

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
       super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        //creacion de tabla lecuras empresa brothercorp
        db.execSQL("create table lecturas(codigo INTEGER primary key AUTOINCREMENT,periodo text,cliente text,lecturaInicial int,lecturaFinal int,equipo text,fecha DATETIME DEFAULT CURRENT_TIMESTAMP,fechaModificacion DATETIME,grupo text,numero text,zona text)");
        db.execSQL("create table grupoLecturas(grupo text,fecha DATETIME DEFAULT CURRENT_TIMESTAMP,estado int)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
