package app.ciclismo.layout.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import app.ciclismo.R;
import app.ciclismo.models.Coordenada;
import app.ciclismo.services.UsuarioService;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private MapView mapView;
    private UsuarioService usuarioService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_mapa));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usuarioService = UsuarioService.getInstance(this);

        mapView = findViewById(R.id.map_view);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        clear(null);
        finish();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng tec = new LatLng(21.108932, -101.626734);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(tec, 13.8f));


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                usuarioService.coordenadas.add(new Coordenada(
                    String.valueOf(latLng.latitude),
                    String.valueOf(latLng.longitude)
                ));

                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.RED);
                polylineOptions.width(5);

                int i = 0;
                map.clear();
                for (Coordenada c : usuarioService.coordenadas) {
                    polylineOptions.add(new LatLng(c.getLat(), c.getLng()));
                    if (i == 0)
                        map.addMarker(new MarkerOptions().position(new LatLng(c.getLat(), c.getLng())).title("Inicio"));
                    if (i != 0 && i == usuarioService.coordenadas.size() - 1)
                        map.addMarker(new MarkerOptions().position(latLng).title("Destino"));

                    i++;
                }
                Polyline polyline = map.addPolyline(polylineOptions);
            }
        });
    }

    public void clear(View view) {
        usuarioService.coordenadas.clear();
        map.clear();
    }

    public void aceptar(View view) {
        finish();
    }
}
