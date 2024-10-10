
public class GestionErreur extends Exception {

    // Exception pour une saisie vide
    public GestionErreur(String message) {
        super(message);
    }

    // Méthode pour une erreur de norme de prix de vente
    public static void normeVente() {
        System.out.println("Le prix de vente doit être supérieur au prix d'achat et non nul.\n");
    }

    // Méthode pour une mise à jour réussie
    public  static void update(String message) {
        System.out.println(message + " a été mis(e) à jour avec succès.\n");
    }

    // Méthode pour une saisie vide
    public static void saisiVide() {
        System.out.println("Votre saisie est vide, veuillez ressaisir à nouveau.\n");
    }
    public static void normeQuantite() {
        System.out.println("la quantité doit etre supperieur à 0.\n");
    }
    public static void  ruptureStock(String nomProduit) {
        System.out.println("Nous sommes en rupture de stock au niveau du produit "+nomProduit+".\n");
    }
    public static void ruptureStockProposition(String nomProduit, int quantiteRest) {
        System.out.println("Nous sommes en rupture de stock au niveau du produit "+nomProduit+" mais la quantité que vous pouvez avoire est : "+quantiteRest+".\n");
    }
}
