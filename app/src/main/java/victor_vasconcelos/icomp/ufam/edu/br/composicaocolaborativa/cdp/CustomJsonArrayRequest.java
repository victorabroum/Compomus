package victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.cdp;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomJsonArrayRequest extends Request<JSONArray> {
    private Listener<JSONArray> response;
    private Map<String, String> params;
    private ArrayList<String> par;


    public CustomJsonArrayRequest(int method, String url, Map<String, String> params, Listener<JSONArray> response, ErrorListener listener) {
        super(method, url, listener);
        this.params = params;
        this.response = response;
        // TODO Auto-generated constructor stub
    }

    public CustomJsonArrayRequest(int method, String url, ArrayList<String> par, Listener<JSONArray> response, ErrorListener listener) {
        super(method, url, listener);
        this.par = par;
        this.response = response;
        // TODO Auto-generated constructor stub
    }

    public CustomJsonArrayRequest(String url, Map<String, String> params, Listener<JSONArray> response, ErrorListener listener) {
        super(Method.GET, url, listener);
        this.params = params;
        this.response = response;
        // TODO Auto-generated constructor stub
    }

    public Map<String, String> getParams() throws AuthFailureError{
        return params;
    }

    public Map<String, String> getHeaders() throws AuthFailureError{
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("apiKey", "Essa e minha API KEY: json array");

        return(header);
    }

    public Priority getPriority(){
        return(Priority.NORMAL);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String js = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return(Response.success(new JSONArray(js), HttpHeaderParser.parseCacheHeaders(response)));
        }
        catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void deliverResponse(JSONArray response) {
        this.response.onResponse(response);
    }

}