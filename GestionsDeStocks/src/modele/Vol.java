package modele;

import java.time.LocalDateTime;

public class Vol {
    private String numeroVol;
    private LocalDateTime dateDepart;
    private LocalDateTime dateArrivee;
    private String destination;
    private int placesDisponibles;
    private double prix;

    public Vol(String numeroVol, LocalDateTime dateDepart, LocalDateTime dateArrivee, String destination, int placesDisponibles, double prix) {
        this.numeroVol = numeroVol;
        this.dateDepart = dateDepart;
        this.dateArrivee = dateArrivee;
        this.destination = destination;
        this.placesDisponibles = placesDisponibles;
        this.prix = prix;
    }

    // Getters
    public String getNumeroVol() {
        return numeroVol;
    }

    public LocalDateTime getDateDepart() {
        return dateDepart;
    }

    public LocalDateTime getDateArrivee() {
        return dateArrivee;
    }

    public String getDestination() {
        return destination;
    }

    public int getPlacesDisponibles() {
        return placesDisponibles;
    }

    public double getPrix() {
        return prix;
    }

    // Setters
    public void setNumeroVol(String numeroVol) {
        this.numeroVol = numeroVol;
    }

    public void setDateDepart(LocalDateTime dateDepart) {
        this.dateDepart = dateDepart;
    }

    public void setDateArrivee(LocalDateTime dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setPlacesDisponibles(int placesDisponibles) {
        this.placesDisponibles = placesDisponibles;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Vol{" +
                "numeroVol='" + numeroVol + '\'' +
                ", dateDepart=" + dateDepart +
                ", dateArrivee=" + dateArrivee +
                ", destination='" + destination + '\'' +
                ", placesDisponibles=" + placesDisponibles +
                ", prix=" + prix +
                '}';
    }
}
