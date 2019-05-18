package app.ciclismo.models;

public class Coordenada {

    private String lat;
    private String lng;

    public Coordenada(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return Double.parseDouble(lat);
    }

    public double getLng() {
        return Double.parseDouble(lng);
    }
}
