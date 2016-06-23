package victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.Ambiente;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.R;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.Som;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.Usuarios;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.cdp.CustomJsonObjectRequest;

public class SonsActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    private TextView tvTeste;
    private MediaPlayer mp;
    private boolean tocando = false, inside = false, entrou = false;
    private RequestQueue rq;
    private Ambiente ambiente;
    private Intent intent;
    private Usuarios usuario;
    private String ip;

    private HashMap<String, Som> audioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sons);

        ip = getResources().getString(R.string.ip);

        //Recupera as informações da Intent
        intent = getIntent();
        usuario = (Usuarios) intent.getSerializableExtra("usuario");

        //Initialize the RequestQueue
        rq = Volley.newRequestQueue(SonsActivity.this);

        //Initialize a locationManage and set a Provider
        initializeProvider();

        //initialize a TextView
        tvTeste = (TextView) findViewById(R.id.tvTeste);

        //Make a HasMap of audios
        audioList = Som.populateKeyAudios();

        //Get the Area of Environment
        ambiente = getAmbiente();

    }

    public void onDestroy() {
        super.onDestroy();
        stopMusic();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);

    }

    protected void onPause() {
        super.onPause();
        if (inside) {
            updatePessoas("-1");
            inside = false;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
        stopMusic();
    }

    protected void onResume(){
        super.onResume();
        ambiente = getAmbiente();
    }

    @Override
    public void onLocationChanged(Location location) {

        /*boolean area = GPSHelper.setArea(ambiente.getLongitude(),
                ambiente.getLatitude(), location.getLongitude(), location.getLatitude(), ambiente.getRaio());*/
        float vetor[] = new float[3];
        Location.distanceBetween(ambiente.getLatitude(),ambiente.getLongitude(),
                location.getLatitude(),location.getLongitude(),vetor);
        Log.i("TESTE", "LAT INICIAL: " + location.getLatitude());
        Log.i("TESTE", "Longi INICIAL: " + location.getLongitude());
        Log.i("TESTE", "Lat Final: " + ambiente.getLatitude());
        Log.i("TESTE", "Longi Final: " + ambiente.getLatitude());
        Log.i("TESTE", "DISTANCIA: " + vetor[0]);

        if (vetor[0] <= ambiente.getRaio()){
            if(!entrou){
                updatePessoas("1");
            }
            inserirLog(usuario.getIdUsuario(), 1, usuario.getSons());
            inside = true;
            entrou = true;
            playMusic(audioList.get(usuario.getSons()).getSourceRaw());
        }else{
            if (inside){
                updatePessoas("-1");
            }
            inserirLog(usuario.getIdUsuario(), 0, usuario.getSons());
            inside = false;
            entrou = false;
            stopMusic();
        }
        /*if (area){
            if(!entrou){
                updatePessoas("1");
            }
            inserirLog(usuario.getIdUsuario(), 1, usuario.getSons());
            inside = true;
            entrou = true;
            playMusic(audioList.get(usuario.getSons()).getSourceRaw());
        }else{
            if (inside){
                updatePessoas("-1");
            }
            inserirLog(usuario.getIdUsuario(), 0, usuario.getSons());
            inside = false;
            entrou = false;
            stopMusic();
        }*/

        ambiente = getAmbiente();

        if (inside){
            tvTeste.setText("Ta dentro");
        }else {
            tvTeste.setText("Ta fora");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void updatePessoas(String num){
        Map<String, String> params = new HashMap<>();
        params.put("pessoa", num);

        String url = ip + "/composicaomusical/teste.php/updatePessoas";
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

    private Ambiente getAmbiente(){

        String url = ip + "/composicaomusical/teste.php/getAmbienteAll";
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

    public void inserirLog(int idUsuario, int status, String audio){

        String url = ip + "/composicaomusical/teste.php/insertLog";

        Map<String, String> params = new HashMap<>();
        params.put("id_usuario", "" + idUsuario);
        params.put("status", "" + status);
        params.put("audio", audio);

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

    public void playMusic(int audioRaw){
        if (!tocando){
            mp = MediaPlayer.create(SonsActivity.this, audioRaw);
            mp.start();
            mp.setLooping(true);
            tocando = true;
        }
    }

    public void stopMusic(){
        if (tocando) {
            mp.pause();
            mp.release();
            mp = null;
            tocando = false;
        }
    }

    public void initializeProvider(){
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
    }

    public void btTrocarSom(View view){
        intent = new Intent (SonsActivity.this, CadastroSomActivity.class);
        intent.putExtra("user", usuario);
        intent.putExtra("theLast", "SonsActivity");
        startActivity(intent);
    }


}
