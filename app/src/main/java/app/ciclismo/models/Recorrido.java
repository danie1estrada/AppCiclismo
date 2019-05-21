package app.ciclismo.models;

public class Recorrido {

    public String id;
    public String titulo;
    public String urlImagenPrevia;
    public String descripcion;
    public String lugarInicio;
    public String lugarDestino;
    public String fecha;
    public String hora;
    public int asistentes;
    public Coordenada[] listaCoordenadas;
    
    public Recorrido(
        String id, String titulo, String urlImagenPrevia, String descripcion, String lugarInicio,
        String lugarDestino, String fecha, String hora, int asistentes, Coordenada[] listaCoordenadas
    ) {
        this.titulo = titulo;
        this.urlImagenPrevia = urlImagenPrevia;
        this.descripcion = descripcion;
        this.lugarInicio = lugarInicio;
        this.lugarDestino = lugarDestino;
        this.fecha = fecha;
        this.hora = hora;
        this.asistentes = asistentes;
        this.listaCoordenadas = listaCoordenadas;
    }

    public String toString() {
        return String.format("titulo: %s\ndescripcio: %s\nCoordenadas: lat: %f, lng: %f",
                titulo, descripcion, listaCoordenadas[0].getLat(), listaCoordenadas[0].getLng());
    }
}
