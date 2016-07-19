package victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.Ambiente;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.R;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.Som;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.Usuarios;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.cdp.CustomJsonObjectRequest;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.helper.RequestMethod;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.services.ServiceLog;

public class SonsActivity extends AppCompatActivity implements com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /*
    * New things from google play service API
    * */

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    //****************

    private TextView tvTeste, tvInicio, tvFinal, tvDist;
    private MediaPlayer mp;
    private boolean tocando = false, inside = false, entrou = false;
    private RequestQueue rq;
    private Ambiente ambiente;
    private Intent intent;
    private Usuarios usuario;
    private String ip;

    private HashMap<String, Som> audioList;

    private FileOutputStream fosExt;
    private File fileExt;

    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sons);

        //Call conection - Google Play Services API
        callConnection();

        //Get IP for JSONs Requests
        ip = getResources().getString(R.string.ip);

        //Recupera as informações da Intent
        intent = getIntent();
        usuario = (Usuarios) intent.getSerializableExtra("usuario");

        //Initialize the RequestQueue
        rq = Volley.newRequestQueue(SonsActivity.this);

        //Get info from Ambient
        ambiente = getAmbiente();

        //initialize a TextView
        tvTeste = (TextView) findViewById(R.id.tvTeste);
        tvInicio = (TextView) findViewById(R.id.tvInicio);
        tvFinal = (TextView) findViewById(R.id.tvFinal);
        tvDist = (TextView) findViewById(R.id.tvDist);

        //Make a HasMap of audios
        audioList = Som.populateKeyAudios();

        //Create and Open a txt file
        fileExt = new File(Environment.getExternalStorageDirectory(), "Compomus-LOG.txt");
        try {
            fosExt = new FileOutputStream(fileExt);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        i = 0;

        Log.i("Cycle of Life", "Created");

    }

    @Override
    public void onLocationChanged(Location location) {

        if (i >= 500) {
            i = 0;
            String aux = readLog(fileExt);
            Log.i("String", aux);
            inserirLog(aux, usuario.getIdUsuario(), usuario.getNome());
        }

        float vetor[] = new float[3];

        //Função para saber a distancia
        Location.distanceBetween(ambiente.getLatitude(), ambiente.getLongitude(),
                location.getLatitude(), location.getLongitude(), vetor);

        tvInicio.setText(String.format("(%s,%s)", ambiente.getLongitude(), ambiente.getLatitude()));
        tvFinal.setText(String.format("(%s,%s)", location.getLongitude(), location.getLatitude()));
        tvDist.setText(String.format("Distância : %s", vetor[0]));

        if (vetor[0] <= ambiente.getRaio()) {
            if (!entrou) {
                updatePessoas("1");
            }
            //inserirLog(usuario.getIdUsuario(), 1, usuario.getSons());
            writeLog(usuario.getIdUsuario(), 1, usuario.getSons());
            inside = true;
            entrou = true;
            playMusic(audioList.get(usuario.getSons()).getSourceRaw());
        } else {
            if (inside) {
                updatePessoas("-1");
            }
            //inserirLog(usuario.getIdUsuario(), 0, usuario.getSons());
            writeLog(usuario.getIdUsuario(), 0, usuario.getSons());
            inside = false;
            entrou = false;
            stopMusic();
        }

        ambiente = getAmbiente();

        if (inside) {
            tvTeste.setText(R.string.isInside);
        } else {
            tvTeste.setText(R.string.isOut);
        }

        i++;

    }

    private void updatePessoas(String num) {
        Map<String, String> params = new HashMap<>();
        params.put("pessoa", num);
        String url = ip + "/composicaomusical/app.php/updatePessoas";
        RequestMethod.makePut(params, url, rq);
    }

    private Ambiente getAmbiente() {

        String url = ip + "/composicaomusical/app.php/getAmbienteAll";
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            float raio = (float) response.getDouble("Raio");
                            ambiente = new Ambiente(response.getInt("Id"), response.getString("Descricao"),
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

    public void inserirLog(String arquivo, int idUsuario, String nomeUsuario) {

        String url = ip + "/composicaomusical/app.php/insertLog";

        Map<String, String> params = new HashMap<>();
        params.put("log", arquivo);
        params.put("id_usuario", "" + idUsuario);
        params.put("nomeUsuario", nomeUsuario);

        RequestMethod.makePost(params, url, rq);
    }

    public void writeLog(int idUsuario, int status, String audio) {
        //Gerando txt:

        String log = idUsuario + " " + status + " " + audio + "\n";
        try {
            fosExt.write(log.getBytes());
            fosExt.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Writer", e.getMessage());
        }
        //***************************************
    }

    public String readLog(File file) {
        String log = "", line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                log += line + "\n";
            }
            fosExt.close();
            fileExt = new File(Environment.getExternalStorageDirectory(), "Compomus-LOG.txt");
            fosExt = new FileOutputStream(fileExt);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return log;
    }

    public void playMusic(int audioRaw) {
        if (!tocando) {
            mp = MediaPlayer.create(SonsActivity.this, audioRaw);
            mp.start();
            mp.setLooping(true);
            tocando = true;
        }
    }

    public void stopMusic() {
        if (tocando) {
            mp.pause();
            mp.release();
            mp = null;
            tocando = false;
        }
    }

    public void btTrocarSom(View view) {
        closeAll();
        intent = new Intent(SonsActivity.this, CadastroSomActivity.class);
        intent.putExtra("user", usuario);
        intent.putExtra("theLast", "SonsActivity");
        startActivity(intent);
    }

    private void closeAll() {
        if (inside) {
            updatePessoas("-1");
            inside = false;
        }
        stopMusic();

        try {
            fosExt.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Stop the Location Update
        if (mGoogleApiClient != null) {
            stopLocationUpdate();
        }
    }

    public void onPause() {
        super.onPause();

        Log.i("Cycle of Life", "Pause");
    }

    public void onDestroy() {
        super.onDestroy();
        String archive = readLog(fileExt);
        if (!archive.equals("")) {
            inserirLog(archive, usuario.getIdUsuario(), usuario.getNome());
            intent = new Intent(this, ServiceLog.class);
            startService(intent);
        }
        closeAll();
        Log.i("Cycle of Life", "Destroyed");
    }

    protected void onResume() {
        super.onResume();

        //Google Play Services API
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            startLocationUpdate();
        }

        Log.i("Cycle of Life", "Resumed");
    }

    /*
    * New things from google play service API
    * */
    private void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdate() {
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
        initLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, SonsActivity.this);
    }

    private void stopLocationUpdate() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, SonsActivity.this);
    }

    private synchronized void callConnection() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

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
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if( location != null){
            Log.i("Google Play Services", location.toString());

        }
        startLocationUpdate();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    //*****************************
}
