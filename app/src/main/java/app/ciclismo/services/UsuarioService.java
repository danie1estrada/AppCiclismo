package app.ciclismo.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.ciclismo.R;
import app.ciclismo.models.Coordenada;
import app.ciclismo.models.Login;
import app.ciclismo.models.Usuario;

public class UsuarioService {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private Queue queue;
    public Usuario usuario;
    private static UsuarioService instance;
    public ArrayList<Coordenada> coordenadas;

    public Coordenada[] getCoordenadas() {
        Coordenada[] c = new Coordenada[coordenadas.size()];
        for (int i = 0; i < c.length; i++) {
            c[i] = coordenadas.get(i);
        }

        return c;
    }

    public static UsuarioService getInstance(Context context) {
        if (instance == null) {
            instance = new UsuarioService(context);
        }
        return instance;
    }

    private UsuarioService(Context context) {
        coordenadas = new ArrayList<Coordenada>();
        queue = Queue.getInstance(context);
        this.context = context;

        sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.app_ciclismo_preference_file),
            Context.MODE_PRIVATE
        );
        editor = sharedPreferences.edit();
    }

    public void login(
        final String email,
        final String password,
        Response.Listener<String> responseListener,
        Response.ErrorListener errorListener
    ) {
        StringRequest request = new StringRequest(
            Request.Method.POST,
            context.getString(R.string.url) + "usuarios/login",
            responseListener,
            errorListener
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        queue.addToQueue(request);
    }

    public void getUsuario(
        Response.Listener<String> responseListener,
        Response.ErrorListener errorListener
    ) {
        StringRequest request = new StringRequest(
            Request.Method.GET,
            context.getString(R.string.url) + "usuarios/" + getUserId(),
            responseListener,
            errorListener
        );

        queue.addToQueue(request);
    }

    public void registrarUsuario(
        final String username,
        final String nombre,
        final String primerApellido,
        final String segundoApellido,
        final String email,
        final String password,
        Response.Listener<String> responseListener,
        Response.ErrorListener errorListener
    ) {
        StringRequest request = new StringRequest(
            Request.Method.POST,
            context.getString(R.string.url) + "usuarios",
            responseListener,
            errorListener
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("nombre", nombre);
                params.put("primerApellido", primerApellido);
                params.put("segundpApellido", segundoApellido);
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        queue.addToQueue(request);
    }

    public void removerCredenciales() {
        editor.clear();
        editor.apply();
    }

    public void guardarCredenciales(Login login) {
        editor.putString("accessToken", login.getId());
        editor.putString("userId", login.getUserId());
        editor.apply();
    }

    public boolean isAuthenticated() {
        return sharedPreferences.getString("accessToken", null) != null;
    }

    public String getUserId() {
        return sharedPreferences.getString("userId", null);
    }

    public String getAccessToken() {
        return sharedPreferences.getString("accessToken", null);
    }

}
