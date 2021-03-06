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

public class RecuperarPasswordActivity extends AppCompatActivity {

    private Queue queue;
    private EditText email;
    private ConstraintLayout loadingScreen;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_password_recovery));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingScreen = findViewById(R.id.loading_screen_pr);
        loadingScreen.setVisibility(View.GONE);
        email = findViewById(R.id.txt_email_pr);
        queue = Queue.getInstance(this);
        context = this;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void enviarCodigo(View view) {
        if (!validar())
            return;

        loadingScreen.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(
            Request.Method.POST,
            getString(R.string.url) + "usuarios/enviar-correo-password",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    startActivity(new Intent(context, RecuperarPasswordPt2Activity.class));
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loadingScreen.setVisibility(View.GONE);
                    Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_LONG).show();
                }
            }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email.getText().toString());

                return params;
            }
        };

        queue.addToQueue(request);
    }

    private boolean validar() {
        int validaciones = 0;

        if (InputValidator.validateEmail(email))
            validaciones++;

        if (InputValidator.validateFieldNotEmpty(email)) {
            validaciones++;
        }

        return validaciones == 2;
    }
}
