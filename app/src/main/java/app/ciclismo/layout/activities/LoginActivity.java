package app.ciclismo.layout.activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import app.ciclismo.R;
import app.ciclismo.models.Login;
import app.ciclismo.services.UsuarioService;
import app.ciclismo.util.InputValidator;

public class LoginActivity extends AppCompatActivity {

    private ConstraintLayout loadingScreen;
    private UsuarioService usuarioService;
    private EditText txtEmail;
    private EditText txtPassword;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuarioService = UsuarioService.getInstance(this);
        if (usuarioService.isAuthenticated()) {
            startActivity(new Intent(this, RecorridosActivity.class));
            finish();
        }

        loadingScreen = findViewById(R.id.loading_screen_login);
        txtEmail = findViewById(R.id.txt_email);
        txtPassword = findViewById(R.id.txt_password);
        image = findViewById(R.id.image_activity_login);
        Picasso.get().load("https://cdn.pixabay.com/photo/2017/08/11/11/08/professional-road-bicycle-racer-2630333_960_720.jpg").into(image);
    }

    public void registrarse(View view) {
        startActivity(new Intent(this, RegistroActivity.class));
    }

    public void login(View view) {
        if (!validarLogin())
            return;

        loadingScreen.setVisibility(View.VISIBLE);
        usuarioService.login(
            txtEmail.getText().toString(),
            txtPassword.getText().toString(),
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
        Login login = new Gson().fromJson(response, Login.class);
        usuarioService.guardarCredenciales(login);

        startActivity(new Intent(this, RecorridosActivity.class));
        finish();
    }

    private void errorHandler(VolleyError error) {
        loadingScreen.setVisibility(View.GONE);

        if (error.networkResponse == null)
            Toast.makeText(this, "Sin conexión", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_LONG).show();
    }

    private boolean validarLogin() {
        int noValidaciones = 0;

        if (InputValidator.validateEmail(txtEmail))
            noValidaciones++;

        if (InputValidator.validateFieldNotEmpty(txtEmail))
            noValidaciones++;

        if (InputValidator.validateFieldNotEmpty(txtPassword))
            noValidaciones++;

        return noValidaciones == 3;
    }
}
