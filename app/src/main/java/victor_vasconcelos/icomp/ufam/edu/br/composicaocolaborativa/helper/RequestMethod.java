package victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.helper;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.Ambiente;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.cdp.CustomJsonObjectRequest;

/**
 * Created by Victor Freitas Vasconcelos on 14/07/2016.
 */
public class RequestMethod {
    private static Ambiente ambiente;
    public static void makePost(Map<String,String> params, String url, RequestQueue rq){

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("LOG", "SUCCESS: "+response);

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
    public static void makePut(Map<String,String> params, String url, RequestQueue rq){
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.PUT,
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("UpdateAmbiente", "Update das Pessoas do ambiente " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR UPdate", "ERROR no Update " + error.getMessage());
            }
        });
        request.setTag("tag");
        rq.add(request);
    }
    public static Ambiente makeGet(Ambiente am, String url, RequestQueue rq){

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            float raio = (float) response.getDouble("Raio");
                            ambiente = new Ambiente(response.getInt("Id"),response.getString("Descricao"),
                                    response.getDouble("Longitude"), response.getDouble("Latitude"),
                                    raio, response.getInt("Pessoas"));
                            Log.i("GetAmbienteAll", "Raio: " + ambiente.getRaio());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.setTag("tag");
        rq.add(request);
        return ambiente;
    }
}
