package app.ciclismo.layout.activities;

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

public class FeedbackActivity extends AppCompatActivity {

    private Queue queue;
    private EditText comentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_feedback));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        comentario = findViewById(R.id.txt_feedback_comentario);
        queue = Queue.getInstance(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void enviarFeedback(View view) {
        StringRequest request = new StringRequest(
            Request.Method.POST,
            getString(R.string.url) + "feedbacks",
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
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("comentario", comentario.getText().toString());

                return params;
            }
        };

        queue.addToQueue(request);
    }

    private void responseHandler(String response) {
        comentario.setText("");
        Toast.makeText(this, "Gracias por tus comentarios", Toast.LENGTH_LONG).show();
    }

    private  void errorHandler(VolleyError error) {
        Toast.makeText(this, "No se ha podido enviar tu comentario", Toast.LENGTH_LONG).show();
    }
}
