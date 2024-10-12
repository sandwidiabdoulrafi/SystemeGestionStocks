package modele;

public class Reservation {
    private int id;
    private Passager passager;
    private Vol vol;
    private String statut;

    public Reservation(int id, Passager passager, Vol vol, String statut) {
        this.id = id;
        this.passager = passager;
        this.vol = vol;
        this.statut = statut;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Passager getPassager() {
        return passager;
    }

    public void setPassager(Passager passager) {
        this.passager = passager;
    }

    public Vol getVol() {
        return vol;
    }

    public void setVol(Vol vol) {
        this.vol = vol;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", passager=" + passager +
                ", vol=" + vol +
                ", statut='" + statut + '\'' +
                '}';
    }
}
