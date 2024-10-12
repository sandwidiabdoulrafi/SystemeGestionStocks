package donnees;

import modele.AgenceVoyage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgenceVoyageDonnees {
    public List<AgenceVoyage> obtenirToutesAgences() throws SQLException {
        List<AgenceVoyage> agences = new ArrayList<>();
        String requete = "SELECT * FROM agences_voyage";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(requete)) {

            while (rs.next()) {
                AgenceVoyage agence = new AgenceVoyage(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("telephone")
                );
                agences.add(agence);
            }
        }
        return agences;
    }

    public AgenceVoyage obtenirAgenceParNom(String nom) throws SQLException {
        String requete = "SELECT * FROM agences_voyage WHERE nom = ?";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete)) {

            pstmt.setString(1, nom);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new AgenceVoyage(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("adresse"),
                            rs.getString("telephone")
                    );
                }
            }
        }
        return null;
    }

    public String obtenirOffresSpeciales(int agenceId) throws SQLException {
        String requete = "SELECT offres_speciales FROM agences_voyage WHERE id = ?";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete)) {

            pstmt.setInt(1, agenceId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("offres_speciales");
                }
            }
        }
        return "Aucune offre spéciale disponible";
    }
}
