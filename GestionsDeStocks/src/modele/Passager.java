package modele;

public class Passager {
    private int id;
    private String nom;
    private String email;
    private String numeroPasseport;

    public Passager(int id, String nom, String email, String numeroPasseport) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.numeroPasseport = numeroPasseport;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getNumeroPasseport() {
        return numeroPasseport;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNumeroPasseport(String numeroPasseport) {
        this.numeroPasseport = numeroPasseport;
    }

    @Override
    public String toString() {
        return "Passager{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", numeroPasseport='" + numeroPasseport + '\'' +
                '}';
    }
}
