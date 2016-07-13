package victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.Ambiente;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.R;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.Som;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.Usuarios;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.cdp.CustomJsonObjectRequest;

public class SonsActivity extends AppCompatActivity implements LocationListener {

    private static final int DELAY = 1000 * 2;
    private LocationManager locationManager;
    private Location melhorPosicao;
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

    private ProgressDialog pDialog;

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
        tvInicio = (TextView) findViewById(R.id.tvInicio);
        tvFinal = (TextView) findViewById(R.id.tvFinal);
        tvDist = (TextView) findViewById(R.id.tvDist);

        //Make a HasMap of audios
        audioList = Som.populateKeyAudios();

        //Get the Area of Environment
        ambiente = getAmbiente();
        File fileExt = new File(Environment.getExternalStorageDirectory(), "Compomus-LOG.txt");
        try {
            fosExt = new FileOutputStream(fileExt);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


    }

    private float media(float vet[]){
        int j;
        float media = 0;
        for (j = 0; j < vet.length; j++){
            media += vet[j];
        }
        media = media / vet.length;
        return media;
    }

    @Override
    public void onLocationChanged(Location location) {

        //Procurar a localização mais precisa
        if (melhorLocalidade(location,melhorPosicao)){
            melhorPosicao = location;
        }

        float vetor[] = new float[3];

        //Função para saber a distancia
        Location.distanceBetween(ambiente.getLatitude(),ambiente.getLongitude(),
                melhorPosicao.getLatitude(),melhorPosicao.getLongitude(),vetor);

        //Logs para o teste de variaveis
        Log.i("TESTE", "LAT INICIAL: " + melhorPosicao.getLatitude());
        Log.i("TESTE", "Longi INICIAL: " + melhorPosicao.getLongitude());
        Log.i("TESTE", "Lat Final: " + ambiente.getLatitude());
        Log.i("TESTE", "Longi Final: " + ambiente.getLatitude());
        Log.i("TESTE", "DISTANCIA: " + vetor[0]);

        tvInicio.setText(String.format("(%s,%s)", ambiente.getLongitude(), ambiente.getLatitude()));
        tvFinal.setText(String.format("(%s,%s)", melhorPosicao.getLongitude(), melhorPosicao.getLatitude()));
        tvDist.setText(String.format("Distância : %s", vetor[0]));

        if (vetor[0] <= ambiente.getRaio()){
            if(!entrou){
                updatePessoas("1");
            }
            //inserirLog(usuario.getIdUsuario(), 1, usuario.getSons());
            writeLog(usuario.getIdUsuario(), 1, usuario.getSons());
            inside = true;
            entrou = true;
            playMusic(audioList.get(usuario.getSons()).getSourceRaw());
        }else{
            if (inside){
                updatePessoas("-1");
            }
            //inserirLog(usuario.getIdUsuario(), 0, usuario.getSons());
            writeLog(usuario.getIdUsuario(), 0, usuario.getSons());
            inside = false;
            entrou = false;
            stopMusic();
        }

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

        String url = ip + "/composicaomusical/app.php/updatePessoas";
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

        String url = ip + "/composicaomusical/app.php/getAmbienteAll";
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
    public void inserirLog(String nomeArq){

        String url = ip + "/composicaomusical/app.php/insertLog";

        Map<String, String> params = new HashMap<>();
        params.put("nomeArq", nomeArq);

        showpDialog();

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("LOG", "SUCCESS: "+response);
                        hidepDialog();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidepDialog();
                    }
                });
        request.setTag("tag");
        rq.add(request);
    }
    private boolean melhorLocalidade(Location location, Location melhorLocalidadeAtual){
        if (melhorLocalidadeAtual == null){
            //Uma nova localização é melhor que nada
            return true;
        }

        //Checando qual é a posição mais recente
        long deltaTempo = location.getTime() - melhorLocalidadeAtual.getTime();
        boolean significanteNovo = deltaTempo > DELAY;
        boolean significanteVelho = deltaTempo < DELAY;
        boolean ehNovo =  deltaTempo > 0;

        if (significanteNovo){
            return true;
        }else if (significanteVelho){
            return false;
        }

        int accuracyDelta = (int) (location.getAccuracy() - melhorLocalidadeAtual.getAccuracy());
        boolean menosPreciso = accuracyDelta > 0;
        boolean maisPreciso = accuracyDelta < 0;
        boolean significanteMenosPreciso = accuracyDelta > 200;

        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                melhorLocalidadeAtual.getProvider());

        if (maisPreciso){
            return true;
        }else if (ehNovo && !menosPreciso){
            return true;
        }else if (ehNovo && !significanteMenosPreciso && isFromSameProvider){
            return true;
        }
        return false;
    }
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
    public void writeLog(int idUsuario, int status, String audio){
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
        //Pega localização com delay de 5 segundos
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }
    public void btTrocarSom(View view){
        closeAll();
        intent = new Intent (SonsActivity.this, CadastroSomActivity.class);
        intent.putExtra("user", usuario);
        intent.putExtra("theLast", "SonsActivity");
        startActivity(intent);
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void closeAll(){
        if (inside) {
            updatePessoas("-1");
            inside = false;
        }
        stopMusic();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);
        try {
            fosExt.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onDestroy() {
        super.onDestroy();
        closeAll();

    }
    protected void onResume(){
        super.onResume();
        ambiente = getAmbiente();
    }


}
