package donnees;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class RapportDonnees {
    // Récupère les destinations les plus populaires
    public Map<String, Integer> obtenirDestinationsPopulaires() throws SQLException {
        Map<String, Integer> destinationsPopulaires = new HashMap<>();
        String requete = "SELECT v.destination, COUNT(r.id) as nombre_reservations " +
                "FROM vols v LEFT JOIN reservations r ON v.numero_vol = r.vol_id " +
                "GROUP BY v.destination " +
                "ORDER BY nombre_reservations DESC " +
                "LIMIT 10";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(requete)) {

            while (rs.next()) {
                destinationsPopulaires.put(rs.getString("destination"), rs.getInt("nombre_reservations"));
            }
        }
        return destinationsPopulaires;
    }

    // Récupère le nombre total de passagers uniques
    public int obtenirNombreTotalPassagers() throws SQLException {
        String requete = "SELECT COUNT(DISTINCT passager_id) as nombre_total_passagers FROM reservations";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(requete)) {

            if (rs.next()) {
                return rs.getInt("nombre_total_passagers");
            }
        }
        return 0;
    }

    // Récupère les statistiques des réservations par statut
    public Map<String, Integer> obtenirStatistiquesReservationsParStatut() throws SQLException {
        Map<String, Integer> statistiques = new HashMap<>();
        String requete = "SELECT statut, COUNT(*) as nombre FROM reservations GROUP BY statut";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(requete)) {

            while (rs.next()) {
                statistiques.put(rs.getString("statut"), rs.getInt("nombre"));
            }
        }
        return statistiques;
    }
}
