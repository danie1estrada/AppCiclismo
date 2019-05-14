package app.ciclismo.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import app.ciclismo.R;

public class UsuarioService {

    private Context context;
    private Queue queue;

    public UsuarioService(Context context) {
        queue = Queue.getInstance(context);
        this.context = context;
    }

    public void login(
        final String email,
        final String password,
        Response.Listener<JSONObject> responseListener,
        Response.ErrorListener errorListener
    ) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", email);
            requestBody.put("password", password);
        } catch (JSONException e) { }

        JsonObjectRequest request = new JsonObjectRequest(
            Request.Method.POST,
            context.getString(R.string.url) + "usuarios/login",
            requestBody,
            responseListener,
            errorListener
        );

        queue.addToQueue(request);
    }

    public void logout() {

    }

}
