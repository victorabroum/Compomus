package victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.helper;

import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.cdp.CustomJsonObjectRequest;

/**
 * Created by Victor Freitas Vasconcelos on 03/06/2016.
 */
public class RequestLogHelper {

    private ProgressDialog pDialog;
    
    public static void insertLog(String id, String idUsuario, RequestQueue rq, String url){

        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("id_usuario", idUsuario);

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("Script", "SUCCESS: "+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        request.setTag("tag");
        rq.add(request);
    }

}
