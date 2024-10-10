package Admin;

class Admin{
    // sections des attributs
    private String nom;
    private String prenom;
    private String password;


    // sections des contructueurs
    public Admin(String nom, String prenom, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
    }


    //les getters

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }



    //les stters

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public void setPassword(String password) {
        this.password = password;
    }





    //  authentification de l'utilisateur

    public boolean authenticate(String password, String nom) {
        if(this.password.equals(password) && this.nom.equals(nom)){
            System.out.println("Accès accordé , bon arriver monsieur"+this.nom+" "+this.prenom+" .");
            return true;
        }
        else{
            System.out.println("Accès refuser information incorrecte .");
            return false;
        }

    }





}