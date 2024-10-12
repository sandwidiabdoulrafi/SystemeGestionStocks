package donnees;

import modele.Passager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PassagerDonnees {
    // Ajoute un nouveau passager dans la base de données
    public void ajouterPassager(Passager passager) throws SQLException {
        String requete = "INSERT INTO passagers (nom, email, numero_passeport) VALUES (?, ?, ?)";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, passager.getNom());
            pstmt.setString(2, passager.getEmail());
            pstmt.setString(3, passager.getNumeroPasseport());

            int lignesAffectees = pstmt.executeUpdate();

            if (lignesAffectees > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        passager.setId(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }

    // Récupère un passager par son ID
    public Passager obtenirPassager(int id) throws SQLException {
        String requete = "SELECT * FROM passagers WHERE id = ?";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Passager(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("email"),
                            rs.getString("numero_passeport")
                    );
                }
            }
        }
        return null;
    }

    // Met à jour les informations d'un passager existant
    public void mettreAJourPassager(Passager passager) throws SQLException {
        String requete = "UPDATE passagers SET nom = ?, email = ?, numero_passeport = ? WHERE id = ?";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete)) {

            pstmt.setString(1, passager.getNom());
            pstmt.setString(2, passager.getEmail());
            pstmt.setString(3, passager.getNumeroPasseport());
            pstmt.setInt(4, passager.getId());

            pstmt.executeUpdate();
        }
    }

    // Supprime un passager de la base de données
    public void supprimerPassager(int id) throws SQLException {
        String requete = "DELETE FROM passagers WHERE id = ?";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    // Récupère tous les passagers de la base de données
    public List<Passager> obtenirTousPassagers() throws SQLException {
        List<Passager> passagers = new ArrayList<>();
        String requete = "SELECT * FROM passagers";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(requete)) {

            while (rs.next()) {
                Passager passager = new Passager(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("numero_passeport")
                );
                passagers.add(passager);
            }
        }
        return passagers;
    }
}
