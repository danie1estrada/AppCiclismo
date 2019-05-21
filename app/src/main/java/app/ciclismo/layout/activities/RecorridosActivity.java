package app.ciclismo.layout.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.ciclismo.R;
import app.ciclismo.models.Recorrido;
import app.ciclismo.models.Usuario;
import app.ciclismo.services.RecorridoService;
import app.ciclismo.services.UsuarioService;

public class RecorridosActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton fab;
    private UsuarioService usuarioService;
    private TextView fullName, username;
    private ConstraintLayout loadingScreen;
    private Usuario usuario;
    private RecorridoService recorridoService;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorridos);


        initComponents();
    }

    private void initComponents() {
        create();
        settings();
        getUsuarioInfo();
        getRecorridos();
    }

    private void create() {
        adapter = new Adapter(this);
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);

        recorridoService = new RecorridoService(this);
        usuarioService = UsuarioService.getInstance(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                0,
                0
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        fullName = navigationView.getHeaderView(0).findViewById(R.id.label_full_name);
        username = navigationView.getHeaderView(0).findViewById(R.id.label_username);

        loadingScreen = findViewById(R.id.loading_screen_recorridos);
        fab = findViewById(R.id.fab_nuevo_recorrido);
    }

    private void settings() {
        fullName.setText("Jéssica");
    }

    public void startNuevoRecorrido(View view) {
        startActivity(new Intent(this, DetallesRecorridoActivity.class));
    }

    private void getUsuarioInfo() {
        loadingScreen.setVisibility(View.VISIBLE);

        usuarioService.getUsuario(
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    usuarioResponseHandler(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    usuarioErrorHandler(error);
                }
            }
        );
    }

    private void usuarioResponseHandler(String response) {
        loadingScreen.setVisibility(View.GONE);
        usuario = new Gson().fromJson(response, Usuario.class);

        fullName.setText(usuario.getNombreCompleto());
        username.setText(usuario.getUsername());
        usuarioService.usuario = usuario;
    }

    private void usuarioErrorHandler(VolleyError error) {
        loadingScreen.setVisibility(View.GONE);
    }

    private void getRecorridos() {
        loadingScreen.setVisibility(View.VISIBLE);

        recorridoService.getListaRecorridos(
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    listaRecorridosResponseHandler(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listaRecorridosErrorHandler(error);
                }
            }
        );
    }

    private void listaRecorridosResponseHandler(String response) {
        loadingScreen.setVisibility(View.GONE);
        Recorrido[] recorridos = new Gson().fromJson(response, Recorrido[].class);

        for (Recorrido recorrido : recorridos) {
            adapter.agregarRecorrido(recorrido);
        }
    }

    private void listaRecorridosErrorHandler(VolleyError error) {
        loadingScreen.setVisibility(View.GONE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_exit:
                usuarioService.removerCredenciales();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }

        return true;
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private Context context;
        ArrayList<Recorrido> listaRecorridos;

        public Adapter(Context context) {
            listaRecorridos = new ArrayList<>();
            this.context = context;
        }

        public void agregarRecorrido(Recorrido recorrido) {
            listaRecorridos.add(recorrido);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_item_recorridos, viewGroup, false)
            );
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            final Recorrido recorrido = listaRecorridos.get(i);
            viewHolder.inicioDest.setText(recorrido.lugarInicio + " - " + recorrido.lugarDestino);

            Picasso.get().load(recorrido.urlImagenPrevia).into(viewHolder.imagen);
            viewHolder.participantes.setText(String.valueOf(recorrido.asistentes));
            viewHolder.fecha.setText(recorrido.fecha);
            viewHolder.hora.setText(recorrido.hora);
            viewHolder.imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetallesRecorridoActivity.class);
                    intent.putExtra("idRecorrido", recorrido.id);
                    startActivity(intent);
                }
            });

            viewHolder.btnDetalles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetallesRecorridoActivity.class);
                    intent.putExtra("idRecorrido", recorrido.id);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listaRecorridos.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView imagen;
            MaterialButton btnDetalles, btnParticipar;
            TextView inicioDest, fecha, hora, participantes;

            public ViewHolder(View itemView) {
                super(itemView);
                imagen = itemView.findViewById(R.id.img_preview);
                btnDetalles = itemView.findViewById(R.id.btn_recorridos_detalles);
                btnParticipar = itemView.findViewById(R.id.btn_recorridos_participar);
                inicioDest = itemView.findViewById(R.id.lbl_item_inicio_dest);
                fecha = itemView.findViewById(R.id.label_date);
                hora = itemView.findViewById(R.id.label_hour);
                participantes = itemView.findViewById(R.id.lbl_item_participantes);
            }
        }
    }
}
