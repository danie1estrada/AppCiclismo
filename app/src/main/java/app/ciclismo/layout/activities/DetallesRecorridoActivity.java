package app.ciclismo.layout.activities;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import app.ciclismo.models.Coordenada;
import app.ciclismo.models.Recorrido;
import app.ciclismo.services.RecorridoService;

public class DetallesRecorridoActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap map;
    private MapView mapView;
    private RecorridoService recorridoService;
    private ConstraintLayout loadingScreen;

    private TextView titulo, descripcion;
    private ImageView foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_recorrido);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_nuevo_recorrido));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingScreen = findViewById(R.id.loading_screen_detalle_recorrido);
        titulo = findViewById(R.id.lbl_titulo_recorrido);
        descripcion = findViewById(R.id.lbl_detalle_descripcion);
        //loadingScreen.setVisibility(View.GONE);

        recorridoService = new RecorridoService(this);
        foto = findViewById(R.id.foto_recorrido);


        mapView = findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        cargarRecorrido();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //LatLng sydney = new LatLng(-34, 151);
        //map.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng tec = new LatLng(21.108932, -101.626734);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(tec, 13.8f));

    }

    private void cargarRecorrido() {
        loadingScreen.setVisibility(View.VISIBLE);

        recorridoService.getRecorrido(
            "5cdf48d258b6db65cce18d37",
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
        descripcion.setText(recorrido.descripcion);
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
