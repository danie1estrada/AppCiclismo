package app.ciclismo.models;

public class Comentario {

    public String id;
    public String detalles;
    public String idUsuario;
    public String idRecorrido;
    public Usuario usuario;

    public Comentario(String id, String detalles, String idUsuario, String idRecorrido, Usuario usuario) {
        this.id = id;
        this.detalles = detalles;
        this.idUsuario = idUsuario;
        this.idRecorrido = idRecorrido;
        this.usuario = usuario;
    }

    public Comentario(String id, String detalles, String idUsuario, String idRecorrido) {
        this.id = id;
        this.detalles = detalles;
        this.idUsuario = idUsuario;
        this.idRecorrido = idRecorrido;
    }
}
