package app.ciclismo.services;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


import java.util.HashMap;
import java.util.Map;

import app.ciclismo.R;

public class RecorridoService {

    private Context context;
    private Queue queue;

    public RecorridoService(Context context) {
        this.context = context;
        queue = Queue.getInstance(context);
    }

    public void getRecorrido
    (
        String id,
        Response.Listener<String> responseListener,
        Response.ErrorListener errorListener
    ) {
        String url = context.getString(R.string.url) + "recorridos/" + id;
        StringRequest request = new StringRequest(
            Request.Method.GET,
            url,
            responseListener,
            errorListener
        );

        queue.addToQueue(request);
    }

    public void comentarRecorrido
    (
            final String idRecorrido,
            final String idUsuario,
            final String detalles,
            Response.Listener<String> responseListener,
            Response.ErrorListener errorListener
    ) {
        String url = context.getString(R.string.url) + "comentariosRecorrido";
        StringRequest request = new StringRequest(
            Request.Method.GET,
            url,
            responseListener,
            errorListener
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("idUsuario", idUsuario);
                params.put("idRecorrido", idRecorrido);
                params.put("detalles", detalles);

                return params;
            }
        };

        queue.addToQueue(request);
    }
}
