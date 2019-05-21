package app.ciclismo.services;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


import java.util.HashMap;
import java.util.Map;

import app.ciclismo.R;
import app.ciclismo.models.Comentario;

public class RecorridoService {

    private Context context;
    private Queue queue;

    public RecorridoService(Context context) {
        this.context = context;
        queue = Queue.getInstance(context);
    }

    public void getListaRecorridos(
        Response.Listener<String> responseListener,
        Response.ErrorListener errorListener
    ) {
        StringRequest request = new StringRequest(
            Request.Method.GET,
            context.getString(R.string.url) + "recorridos",
            responseListener,
            errorListener
        );

        queue.addToQueue(request);
    }

    public void getRecorrido(
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

    public void cargarComentarios(
            String idRecorrido,
            Response.Listener<String> responseListener,
            Response.ErrorListener errorListener
    ) {
        StringRequest request = new StringRequest(
            Request.Method.GET,
            context.getString(R.string.url) + "recorridos/" + idRecorrido + "/comentarios",
            responseListener,
            errorListener
        );

        queue.addToQueue(request);
    }

    public void comentarRecorrido(
        final Comentario comentario,
        Response.Listener<String> responseListener,
        Response.ErrorListener errorListener
    ) {
        String url = context.getString(R.string.url) + "comentariosRecorrido";
        StringRequest request = new StringRequest(
            Request.Method.POST,
            url,
            responseListener,
            errorListener
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("idUsuario", comentario.idUsuario);
                params.put("idRecorrido", comentario.idRecorrido);
                params.put("detalles", comentario.detalles);

                return params;
            }
        };

        queue.addToQueue(request);
    }
}
