package victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.R;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.Som;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.Usuarios;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.cdp.CustomJsonObjectRequest;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.helper.SomListAdapter;

public class CadastroSomActivity extends AppCompatActivity implements SomListAdapter.OnDataSelected {

    private List<Som> somList = new ArrayList<>();
    private boolean tocando = false;
    private MediaPlayer mp;
    private Intent intent;
    private Usuarios usuario;
    private RequestQueue rq;
    private Som selectedItem;
    private String theLast;
    private TextView tvSomSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_som);

        tvSomSelected = (TextView) findViewById(R.id.tvSomSelected);

        somList = Som.populateSomList();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCad);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        RecyclerView.Adapter adapter = new SomListAdapter(this, this, somList);
        recyclerView.setAdapter(adapter);

        intent = getIntent();
        usuario = (Usuarios) intent.getSerializableExtra("user");
        theLast = intent.getStringExtra("theLast");

        rq = Volley.newRequestQueue(CadastroSomActivity.this);

        selectedItem = null;
    }

    protected void onPause(){
        super.onPause();
        if (tocando){
            mp.stop();
            mp.release();
            tocando = false;
        }
    }

    @Override
    public void onDataSelected(View view, int position) {
        selectedItem = somList.get(position);

        if (tocando){
            mp.stop();
            mp.release();
            tocando = false;
        }
        playMusic(selectedItem.getSourceRaw());
        tvSomSelected.setText(selectedItem.getNome());
        view.setSelected(true);

    }

    private void playMusic(int audioRaw){

        if (!tocando){
            mp = MediaPlayer.create(CadastroSomActivity.this, audioRaw);
            mp.start();
            mp.setLooping(true);
            tocando = true;
        }

    }

    public void btConfirmarCadSom(View view){
        if (selectedItem != null){
            updateAudio(usuario.getIdUsuario(), selectedItem.getNome());
            usuario.setSons(selectedItem.getNome());
            if (theLast.equals("SonsActivity")){
                intent = new Intent(CadastroSomActivity.this, SonsActivity.class);
                intent.putExtra("usuario", usuario);
                Toast.makeText(CadastroSomActivity.this,"Audio trocado com sucesso",Toast.LENGTH_LONG).show();
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }else{
                intent = new Intent(CadastroSomActivity.this, MainActivity.class);
                Toast.makeText(CadastroSomActivity.this,"Usuário criado com sucesso",Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        }else{
            Toast.makeText(CadastroSomActivity.this,"Escolha um audio",Toast.LENGTH_LONG).show();
        }

    }

    public void updateAudio(int idUsuario, String audio){

        String url = getResources().getString(R.string.ip) + "/composicaomusical/app.php/updateAudio";

        Map<String, String> params = new HashMap<>();
        params.put("id", ""+idUsuario);
        params.put("audio", "" + audio);

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.PUT,
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
