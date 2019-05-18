package app.ciclismo.models;

public class Recorrido {

    public String titulo;
    public String urlImagenPrevia;
    public String descripcion;
    public String lugarInicio;
    public String lugarDestino;
    public Coordenada[] listaCoordenadas;
    
    public Recorrido(
        String titulo, String urlImagenPrevia, String descripcion, String lugarInicio,
        String lugarDestino, Coordenada[] listaCoordenadas
    ) {
        this.titulo = titulo;
        this.urlImagenPrevia = urlImagenPrevia;
        this.descripcion = descripcion;
        this.lugarInicio = lugarInicio;
        this.lugarDestino = lugarDestino;
        this.listaCoordenadas = listaCoordenadas;
    }

    public String toString() {
        return String.format("titulo: %s\ndescripcio: %s\nCoordenadas: lat: %f, lng: %f",
                titulo, descripcion, listaCoordenadas[0].getLat(), listaCoordenadas[0].getLng());
    }
}
