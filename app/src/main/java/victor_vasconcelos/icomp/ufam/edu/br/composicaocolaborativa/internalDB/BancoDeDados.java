package victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.internalDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Usuario on 28/04/2016.
 */
public class BancoDeDados extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ComposicaoColobarativa.db";

    private static final String SQL_CREATE_TABLES = "CREATE TABLE Usuarios("+
            "login TEXT PRIMARY KEY, nome TEXT, senha TEXT)";

    private static final String SQL_POPULATE_TABLES = "INSERT INTO Usuarios "+
            "VALUES ('teste', 'Testando', 'teste123')";

    private static final String SQL_DELETE_TABLES = "DROP TABLE IF EXISTS Usuarios";


    public BancoDeDados(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLES);
        db.execSQL(SQL_POPULATE_TABLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLES);
        onCreate(db);
    }
}
