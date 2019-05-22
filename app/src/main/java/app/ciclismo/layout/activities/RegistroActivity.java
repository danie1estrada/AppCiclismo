package app.ciclismo.layout.activities;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import app.ciclismo.R;
import app.ciclismo.services.UsuarioService;
import app.ciclismo.util.InputValidator;

public class RegistroActivity extends AppCompatActivity {

    private ImageView image;
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

        image = findViewById(R.id.image_acivity_registro);
        Picasso.get().load("https://cdn.pixabay.com/photo/2017/08/11/11/08/professional-road-bicycle-racer-2630333_960_720.jpg").into(image);

        loadingScreen = findViewById(R.id.loading_screen_registro);
        loadingScreen.setVisibility(View.GONE);

        usuarioService = UsuarioService.getInstance(this);
    }

    public void registar(View view) {
        if (!validar())
            return;

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
        Toast.makeText(this, "Te has registrado exitosamente", Toast.LENGTH_LONG).show();
        finish();
    }

    public void errorHandler(VolleyError error) {
        loadingScreen.setVisibility(View.GONE);
        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
    }

    private boolean validar() {
        int validaciones = 0;

        if (InputValidator.validateFieldNotEmpty(txtUsername))
            validaciones++;

        if (InputValidator.validateFieldNotEmpty(txtNombre))
            validaciones++;

        if (InputValidator.validateFieldNotEmpty(txtPrimerApellido))
            validaciones++;

        if (InputValidator.validateFieldNotEmpty(txtSegundoApellido))
            validaciones++;

        if (InputValidator.validateEmail(txtEmail))
            validaciones++;

        if (InputValidator.validateFieldNotEmpty(txtEmail))
            validaciones++;

        if (InputValidator.validateFieldNotEmpty(txtPassword))
            validaciones++;

        if (InputValidator.validateMinLength(txtPassword, 6))
            validaciones++;

        if (txtPassword.getText().toString().equals(txtConfirmarPassword.getText().toString()))
            validaciones++;
        else
            txtConfirmarPassword.setError("Las contraseñas deben coincidir");

        return validaciones == 9;
    }
}
