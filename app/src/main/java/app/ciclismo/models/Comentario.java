package app.ciclismo.models;

public class Comentario {

    public String id;
    public String detalles;
    public String idUsuario;
    public String idRecorrido;

    public Comentario(String id, String detalles, String idUsuario, String idRecorrido) {
        this.id = id;
        this.detalles = detalles;
        this.idUsuario = idUsuario;
        this.idRecorrido = idRecorrido;
    }
}
