import GestionErreur;

public class Produit {

    // Attributs
    private String nom;
    private double prixVente;
    private double prixAchat;
    private double benefice;
    private int quantite;
    private int quantiteVendu = 0;

    // Constructeur
    public Produit(String nom, double prixVente, double prixAchat, int quantite) {
        this.nom = nom;
        this.prixVente = prixVente;
        this.prixAchat = prixAchat;
        this.quantite = quantite;
    }


    //section des methode

    public void achat(int quantiteDemande){
        if (quantiteDemande <= 0) {
            System.out.println("La quantité demandée doit être positive.");
            return;
        }
        if(this.quantite == 0){
            GestionErreur.ruptureStock(this.nom);

        }
        else if (this.quantite < quantiteDemande){
            GestionErreur.ruptureStockProposition(this.nom,this.quantite);
        }
        else{
            this.quantite -= quantiteDemande;
            this.quantiteVendu += quantiteDemande;
            this.benefice  = (this.prixVente-this.prixAchat)*quantiteVendu;

        }
    }














    // Getters
    public String getNomProduit() {
        return this.nom;
    }

    public double getPrixVente() {
        return this.prixVente;
    }

    public double getPrixAchat() {
        return this.prixAchat;
    }

    public double getBenefice() {
        return this.benefice;
    }

    public int getQuantite() {
        return this.quantite;
    }

    public int getQuantiteVendu() {
        return this.quantiteVendu;
    }

    // Setters avec gestion des erreurs
    public void setNomProduit(String nom) throws GestionErreur {
        if (nom.isEmpty()) {
            GestionErreur.saisiVide();
        } else {
            this.nom = nom;
            GestionErreur.update("Le nom ");
        }
    }

    public void setPrixVente(double prixVente) throws GestionErreur {
        if (prixVente <= 0 || prixVente <= this.prixAchat) {
            GestionErreur.normeVente();
        }
        else {
            this.prixVente = prixVente;
            GestionErreur.update("Le prix de vente ");
        }
    }

    public void setPrixAchat(double prixAchat) throws GestionErreur {
        if (prixAchat <= 0) {
            GestionErreur.saisiVide();
        } else {
            this.prixAchat = prixAchat;
            GestionErreur.update("Le prix d'achat ");
        }
    }

    public void setQuantite(int quantite)throws GestionErreur {
        if (quantite <= 0) {
            GestionErreur.normeQuantite();

        }
        else{
            this.quantite = quantite;
            GestionErreur.update("La quantité ");
        }

    }
}
