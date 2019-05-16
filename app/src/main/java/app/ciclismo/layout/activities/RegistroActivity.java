package app.ciclismo.layout.activities;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import app.ciclismo.R;
import app.ciclismo.services.UsuarioService;

public class RegistroActivity extends AppCompatActivity {

    private EditText txtUsername;
    private EditText txtNombre;
    private EditText txtPrimerApellido;
    private EditText txtSegundoApellido;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtConfirmarPassword;
    private ConstraintLayout loadingScreen;

    private UsuarioService usuarioService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtUsername = findViewById(R.id.txt_username);
        txtNombre = findViewById(R.id.txt_nombre);
        txtPrimerApellido = findViewById(R.id.txt_primer_apellido);
        txtSegundoApellido = findViewById(R.id.txt_segundo_apellido);
        txtEmail = findViewById(R.id.txt_email);
        txtPassword = findViewById(R.id.txt_password_registro);
        txtConfirmarPassword = findViewById(R.id.txt_confirm_password);

        loadingScreen = findViewById(R.id.loading_screen_registro);
        loadingScreen.setVisibility(View.GONE);

        usuarioService = new UsuarioService(this);
    }

    public void registar(View view) {
        loadingScreen.setVisibility(View.VISIBLE);

        this.usuarioService.registrarUsuario(
            txtUsername.getText().toString(),
            txtNombre.getText().toString(),
            txtPrimerApellido.getText().toString(),
            txtSegundoApellido.getText().toString(),
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

    public void volver(View view) {
        finish();
    }

    public void responseHandler(String response) {
        loadingScreen.setVisibility(View.GONE);
        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
    }

    public void errorHandler(VolleyError error) {
        loadingScreen.setVisibility(View.GONE);
        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
    }
}
