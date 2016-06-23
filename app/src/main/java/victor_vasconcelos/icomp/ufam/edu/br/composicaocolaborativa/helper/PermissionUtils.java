package victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 02/05/2016.
 */
public class PermissionUtils {

    public static boolean validate(Activity activity, int requestCode, String... permissions){

        List<String> list = new ArrayList<String>();
        for (String permission : permissions){
            //Valida permissão
            boolean ok = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
            if (!ok){
                list.add(permission);
            }
        }
        if (list.isEmpty()){
            //Está tudo okay
            return true;
        }

        //Lista de permissões que falta permissões
        String[] newPermissions = new String[list.size()];
        list.toArray(newPermissions);

        //Solicita permissão
        ActivityCompat.requestPermissions(activity, newPermissions, 1);

        return false;
    }
}
