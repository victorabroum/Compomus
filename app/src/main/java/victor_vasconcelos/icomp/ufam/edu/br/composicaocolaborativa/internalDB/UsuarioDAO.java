package victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.internalDB;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.Usuarios;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.internalDB.BancoDeDados;

/**
 * Created by Usuario on 28/04/2016.
 */
public class UsuarioDAO {
    private SQLiteDatabase bancoDeDados;

    public UsuarioDAO(Context context){
        this.bancoDeDados = (new BancoDeDados(context)).getWritableDatabase();
    }

    public Usuarios getUsuario(String login, String senha){
        Usuarios usuario = null;
        String sqlQuery = "SELECT * FROM Usuarios WHERE "+
                "login = '" + login + "' AND senha = '" + senha +"'";
        Cursor cursor = this.bancoDeDados.rawQuery(sqlQuery, null);
        if (cursor.moveToNext()){
            usuario = new Usuarios(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2));
        }
        cursor.close();
        return usuario;
    }

    public boolean addUsuario(Usuarios u){
        try{
            String sqlCmd = "INSERT INTO Usuarios VALUES ('"+
                    u.getLogin() + "', '" + u.getNome() + "', "+
                    "'" + u.getSenha() + "')";
            this.bancoDeDados.execSQL(sqlCmd);
            return true;
        }catch (SQLException e){
            Log.e("BDdebug", e.getMessage());
            return false;
        }
    }
}
