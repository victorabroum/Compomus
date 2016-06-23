package victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.R;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.Usuarios;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.cdp.CustomJsonObjectRequest;

public class CadastroActivity extends AppCompatActivity {

    private EditText edtEmail, edtNome, edtPassword;
    private String url;
    private RequestQueue rq;
    private ProgressDialog pDialog;
    private Usuarios user;
    private Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        rq = Volley.newRequestQueue(CadastroActivity.this);

        edtEmail = (EditText) findViewById(R.id.edtEmailCad);
        edtNome = (EditText) findViewById(R.id.edtNomeCad);
        edtPassword = (EditText) findViewById(R.id.edtSenhaCad);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
    }

    public void proxCadClicado(View view) {
        postTheNewUser();
    }

    private void postTheNewUser()  {

        url = getResources().getString(R.string.ip)+"/composicaomusical/teste.php/postUsuario";

        Map<String, String> params = new HashMap<>();

        params.put("senha",edtPassword.getText().toString());
        params.put("email",edtEmail.getText().toString());
        params.put("nome",edtNome.getText().toString());

        showpDialog();

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Script", "SUCCESS: "+response);
                        getUserFromDB();
                        hidepDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CadastroActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
                        hidepDialog();
                    }
                });

        request.setTag("tag");
        rq.add(request);
    }

    public void getUserFromDB(){


        url = getResources().getString(R.string.ip) + "/composicaomusical/teste.php/getThisUsuario";
        Map<String, String> params = new HashMap<>();

        params.put("senha",edtPassword.getText().toString());
        params.put("email",edtEmail.getText().toString());

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            user = new Usuarios(response.getInt("id"),response.getString("nome"),
                                    response.getString("email"), response.getString("senha"), "");
                            intent = new Intent(CadastroActivity.this, CadastroSomActivity.class);
                            intent.putExtra("user", user);
                            intent.putExtra("theLast", "CadastroActivity");
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        user = null;
                    }
                });

        request.setTag("tag");
        rq.add(request);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
