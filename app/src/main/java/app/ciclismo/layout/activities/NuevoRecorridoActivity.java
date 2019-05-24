package app.ciclismo.layout.activities;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import app.ciclismo.R;
import app.ciclismo.models.Coordenada;
import app.ciclismo.models.Recorri2;
import app.ciclismo.models.Recorrido;
import app.ciclismo.services.Queue;
import app.ciclismo.services.UsuarioService;

public class NuevoRecorridoActivity extends AppCompatActivity {

    private EditText titulo, descripcion, inicio, destino, urlImagen, fecha, hora;
    private ImageView imagen;
    private Queue queue;
    private Context context;
    UsuarioService usuarioService;
    ConstraintLayout loadingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_recorrido);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_nuevo_recorrido));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usuarioService = UsuarioService.getInstance(this);

        loadingScreen = findViewById(R.id.loading_screen_nr);
        loadingScreen.setVisibility(View.GONE);
        titulo = findViewById(R.id.nr_titulo);
        descripcion = findViewById(R.id.nr_descripcion);
        inicio = findViewById(R.id.nr_lugar_inicio);
        destino = findViewById(R.id.nr_destino);
        urlImagen = findViewById(R.id.nr_url_imagen);
        imagen = findViewById(R.id.imagen_recorrido);
        fecha = findViewById(R.id.nr_fecha);
        hora = findViewById(R.id.nr_hora);
        queue = Queue.getInstance(this);
        context = this;

        urlImagen.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    Picasso.get().load(urlImagen.getText().toString()).into(imagen);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void abrirMapa(View view) {
        startActivity(new Intent(this, MapaActivity.class));
    }

    public void agregar(View view) {
        loadingScreen.setVisibility(View.VISIBLE);
        Recorri2 recorrido = new Recorri2(
            titulo.getText().toString(),
            urlImagen.getText().toString(),
            descripcion.getText().toString(),
            inicio.getText().toString(),
            destino.getText().toString(),
            fecha.getText().toString(),
            hora.getText().toString(),
            0,
            usuarioService.getCoordenadas()
        );

        JSONObject requestBody = null;

        try {
            requestBody = new JSONObject(new Gson().toJson(recorrido));
        } catch (JSONException e) { }

        JsonObjectRequest request = new JsonObjectRequest(
            Request.Method.POST,
            getString(R.string.url) + "recorridos",
            requestBody,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    responseHandler(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    errorHandler(error);
                }
            }
        );

        queue.addToQueue(request);
    }

    private void responseHandler(JSONObject response) {
        Toast.makeText(this, "Recorrido creado exitosamente", Toast.LENGTH_LONG).show();
        finish();
    }

    private void errorHandler(VolleyError error) {
        loadingScreen.setVisibility(View.GONE);
        Toast.makeText(this, "Ha ocurrido un error al crear el recorrido", Toast.LENGTH_LONG).show();
    }
}
