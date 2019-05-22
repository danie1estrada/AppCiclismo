package app.ciclismo.layout.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.ciclismo.R;
import app.ciclismo.models.Comentario;

public class ComentariosAdapter extends RecyclerView.Adapter<ComentariosAdapter.ViewHolder> {

    private Context context;
    public ArrayList<Comentario> listaComentarios;

    public ComentariosAdapter(Context context) {
        this.context = context;
        listaComentarios = new ArrayList<>();
    }

    public void agregar(Comentario comentario) {
        listaComentarios.add(0, comentario);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(
            LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.layout_comentario, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Comentario comentario = listaComentarios.get(i);

        viewHolder.username.setText(comentario.usuario.getUsername());
        viewHolder.comentario.setText(comentario.detalles);
    }

    @Override
    public int getItemCount() {
        return listaComentarios.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView username, comentario;
        ImageView foto;

        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.txt_comentario_username);
            comentario = itemView.findViewById(R.id.txt_comentario);
            foto = itemView.findViewById(R.id.foto_usuario);
        }
    }
}
