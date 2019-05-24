package app.ciclismo.layout.activities;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import app.ciclismo.R;
import app.ciclismo.services.Queue;
import app.ciclismo.util.InputValidator;

public class RecuperarPasswordPt2Activity extends AppCompatActivity {

    private EditText nip, password, cPassword;
    private ConstraintLayout loadingScreen;
    private Context context;
    private Queue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password_pt2);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_password_recovery_pt2));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nip = findViewById(R.id.txt_nip);
        loadingScreen = findViewById(R.id.loading_screen_pr2);
        loadingScreen.setVisibility(View.GONE);
        password = findViewById(R.id.txt_new_password);
        cPassword = findViewById(R.id.txt_npass_confirm);
        context = this;
        queue = Queue.getInstance(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void cambiarPassword(View view) {
        if (!validar())
            return;

        loadingScreen.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(
            Request.Method.POST,
            getString(R.string.url) + "usuarios/cambiar-password",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(context, "Contraseña reestablecida", Toast.LENGTH_LONG).show();
                    finish();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loadingScreen.setVisibility(View.GONE);
                    Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                }
            }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nip", nip.getText().toString());
                params.put("password", password.getText().toString());

                return params;
            }
        };

        queue.addToQueue(request);
    }

    public boolean validar() {
        int validaciones = 0;

        if (InputValidator.validateMinLength(password, 6))
            validaciones++;

        if (InputValidator.validateFieldNotEmpty(password))
            validaciones++;

        if (password.getText().toString().equals(cPassword.getText().toString()))
            validaciones++;
        else
            cPassword.setError("Las contraseñas deben coincidir");

        return validaciones == 3;
    }
}
