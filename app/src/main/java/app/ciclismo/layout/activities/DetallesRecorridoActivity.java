package app.ciclismo.layout.activities;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import app.ciclismo.R;
import app.ciclismo.layout.adapters.ComentariosAdapter;
import app.ciclismo.models.Comentario;
import app.ciclismo.models.Coordenada;
import app.ciclismo.models.Recorrido;
import app.ciclismo.services.RecorridoService;
import app.ciclismo.services.UsuarioService;

public class DetallesRecorridoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private MapView mapView;
    private RecorridoService recorridoService;
    private UsuarioService usuarioService;
    private ConstraintLayout loadingScreen;

    private TextView titulo, descripcion, lblComentarios, fecha, hora, asistentes;
    private ImageView foto;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private RecyclerView rvComentarios;
    private ComentariosAdapter comentariosAdapter;

    private EditText txtComentario;
    private String idRecorrido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_recorrido);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_nuevo_recorrido));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idRecorrido = getIntent().getStringExtra("idRecorrido");
        if (idRecorrido == "" || idRecorrido == null) {
            Toast.makeText(this, "No se ha podido cargar el recorrido", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadingScreen = findViewById(R.id.loading_screen_detalle_recorrido);
        titulo = findViewById(R.id.lbl_titulo_recorrido);
        descripcion = findViewById(R.id.lbl_detalle_descripcion);
        //loadingScreen.setVisibility(View.GONE);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);

        comentariosAdapter = new ComentariosAdapter(this);
        rvComentarios = findViewById(R.id.rv_comentarios);
        rvComentarios.setLayoutManager(new LinearLayoutManager(this));
        rvComentarios.setAdapter(comentariosAdapter);

        recorridoService = new RecorridoService(this);
        usuarioService = UsuarioService.getInstance(this);
        foto = findViewById(R.id.foto_recorrido);

        txtComentario = findViewById(R.id.txt_detalle_comentario);
        lblComentarios = findViewById(R.id.lbl_comentarios);
        asistentes = findViewById(R.id.lbl_detalles_asistentes);
        fecha = findViewById(R.id.lbl_detalle_fecha);
        hora = findViewById(R.id.lbl_detalle_hora);

        mapView = findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        cargarRecorrido();
        cargarComentarios();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng tec = new LatLng(21.108932, -101.626734);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(tec, 13.8f));
    }

    private void cargarComentarios() {
        recorridoService.cargarComentarios(
            idRecorrido,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    cargarComentariosResponseHandler(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    cargarComentariosErrorHandler(error);
                }
            }
        );
    }

    private void cargarComentariosResponseHandler(String response) {
        Comentario[] comentarios = new Gson().fromJson(response, Comentario[].class);
        for (Comentario comentario : comentarios) {
            comentariosAdapter.agregar(comentario);
        }
        lblComentarios.setText("Comentarios (" + comentariosAdapter.listaComentarios.size() + ")");
    }

    private void cargarComentariosErrorHandler(VolleyError error) {
        Toast.makeText(this, "Error al cargar los comentarios", Toast.LENGTH_SHORT).show();
    }

    public void enviarComentario(View view) {
        Comentario comentario = new Comentario(
            null,
            txtComentario.getText().toString(),
            usuarioService.getUserId(),
            idRecorrido
        );

        recorridoService.comentarRecorrido(
            comentario,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    comentarioResponseHandler(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    comentarioErrorHandler(error);
                }
            }
        );
    }

    private void comentarioResponseHandler(String response) {
        Comentario comentario = new Gson().fromJson(response, Comentario.class);
        comentario.usuario = usuarioService.usuario;
        comentariosAdapter.agregar(comentario);
        txtComentario.setText("");
        lblComentarios.setText("Comentarios (" + comentariosAdapter.listaComentarios.size() + ")");
    }

    private void comentarioErrorHandler(VolleyError error) {
        Toast.makeText(this, "Error al enviar el comentario", Toast.LENGTH_LONG).show();
    }

    private void cargarRecorrido() {
        loadingScreen.setVisibility(View.VISIBLE);

        recorridoService.getRecorrido(
            idRecorrido,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
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
    }

    private void responseHandler(String response) {
        loadingScreen.setVisibility(View.GONE);

        Recorrido recorrido = new Gson().fromJson(response, Recorrido.class);
        titulo.setText(recorrido.titulo);
        collapsingToolbarLayout.setTitle(recorrido.titulo);
        descripcion.setText(recorrido.descripcion);
        asistentes.setText(String.valueOf(recorrido.asistentes));
        fecha.setText(recorrido.fecha);
        hora.setText(recorrido.hora);
        Picasso.get().load(recorrido.urlImagenPrevia).into(foto);

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(getResources().getColor(R.color.colorAccent));

        for (Coordenada coordenada : recorrido.listaCoordenadas) {
            polylineOptions.add(new LatLng(coordenada.getLat(), coordenada.getLng()));
        }

        polylineOptions.width(5);
        map.addPolyline(polylineOptions);
        map.addMarker(new MarkerOptions().position(new LatLng(recorrido.listaCoordenadas[0].getLat(), recorrido.listaCoordenadas[0].getLng())).title("Inicio del recorrido"));
        map.addMarker(new MarkerOptions().position(new LatLng(recorrido.listaCoordenadas[recorrido.listaCoordenadas.length - 1].getLat(), recorrido.listaCoordenadas[recorrido.listaCoordenadas.length - 1].getLng())).title("Fin del recorrido"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(recorrido.listaCoordenadas[0].getLat(), recorrido.listaCoordenadas[0].getLng()), 13.8f));
    }

    private void errorHandler(VolleyError error) {
        loadingScreen.setVisibility(View.GONE);
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }
}
