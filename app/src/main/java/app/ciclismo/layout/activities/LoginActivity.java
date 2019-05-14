package app.ciclismo.layout.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import app.ciclismo.R;
import app.ciclismo.services.UsuarioService;

public class LoginActivity extends AppCompatActivity {

    private UsuarioService usuarioService;
    private EditText txtUsername;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuarioService = new UsuarioService(this);
        txtUsername = findViewById(R.id.txt_username);
        txtPassword = findViewById(R.id.txt_password);
    }

    public void onClick(View view) {
        startActivity(new Intent(this, MapsActivity.class));
    }

    public void login(View view) {
        Toast.makeText(this, "login", Toast.LENGTH_SHORT).show();

        usuarioService.login(
            txtUsername.getText().toString(),
            txtPassword.getText().toString(),
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    responseHandler(response.toString());
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
        startActivity(new Intent(this, RecorridosActivity.class));
    }

    private void errorHandler(VolleyError error) {
        Toast.makeText(this, error.networkResponse.toString(), Toast.LENGTH_LONG).show();
    }
}
