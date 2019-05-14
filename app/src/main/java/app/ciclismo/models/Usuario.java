package app.ciclismo.models;

public class Usuario {

    private String id;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String username;
    private String email;
    private String telefono;
    private String urlFotoPerfil;


    public Usuario
    (
        String id, String nombre, String primerApellido, String segundoApellido, String username,
        String email, String telefono, String urlFotoPerfil
    ) {
        this.id = id;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.username = username;
        this.email = email;
        this.telefono = telefono;
        this.urlFotoPerfil = urlFotoPerfil;
    }

    private String getNombreCompleto() {
        return String.format("%s %s %s", nombre, primerApellido, segundoApellido);
    }
}
