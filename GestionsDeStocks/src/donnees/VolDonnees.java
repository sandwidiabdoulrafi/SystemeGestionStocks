package donnees;

import modele.Vol;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class VolDonnees {

    /**
     * Recherche des vols selon les critères spécifiés
     * @param destination La destination du vol
     * @param dateDepart La date de départ minimale
     * @param nombrePassagers Le nombre de passagers souhaité
     * @return Une liste de vols correspondant aux critères
     * @throws SQLException En cas d'erreur lors de l'accès à la base de données
     */
    public List<Vol> rechercherVols(String destination, LocalDateTime dateDepart, int nombrePassagers) throws SQLException {
        List<Vol> vols = new ArrayList<>();
        String requete = "SELECT * FROM vols WHERE destination = ? AND date_depart >= ? AND places_disponibles >= ?";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete)) {

            pstmt.setString(1, destination);
            pstmt.setTimestamp(2, Timestamp.valueOf(dateDepart));
            pstmt.setInt(3, nombrePassagers);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    vols.add(extraireVolDuResultSet(rs));
                }
            }
        }
        return vols;
    }

    /**
     * Récupère un vol spécifique par son numéro de vol
     * @param numeroVol Le numéro du vol à récupérer
     * @return Le vol correspondant au numéro, ou null si non trouvé
     * @throws SQLException En cas d'erreur lors de l'accès à la base de données
     */
    public Vol obtenirVol(String numeroVol) throws SQLException {
        String requete = "SELECT * FROM vols WHERE numero_vol = ?";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete)) {

            pstmt.setString(1, numeroVol);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extraireVolDuResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Ajoute un nouveau vol dans la base de données
     * @param vol Le vol à ajouter
     * @throws SQLException En cas d'erreur lors de l'accès à la base de données
     */
    public void ajouterVol(Vol vol) throws SQLException {
        String requete = "INSERT INTO vols (numero_vol, date_depart, date_arrivee, destination, places_disponibles, prix) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete)) {

            pstmt.setString(1, vol.getNumeroVol());
            pstmt.setTimestamp(2, Timestamp.valueOf(vol.getDateDepart()));
            pstmt.setTimestamp(3, Timestamp.valueOf(vol.getDateArrivee()));
            pstmt.setString(4, vol.getDestination());
            pstmt.setInt(5, vol.getPlacesDisponibles());
            pstmt.setDouble(6, vol.getPrix());

            pstmt.executeUpdate();
        }
    }

    /**
     * Met à jour les informations d'un vol existant
     * @param vol Le vol avec les informations mises à jour
     * @throws SQLException En cas d'erreur lors de l'accès à la base de données
     */
    public void mettreAJourVol(Vol vol) throws SQLException {
        String requete = "UPDATE vols SET date_depart = ?, date_arrivee = ?, destination = ?, places_disponibles = ?, prix = ? WHERE numero_vol = ?";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(vol.getDateDepart()));
            pstmt.setTimestamp(2, Timestamp.valueOf(vol.getDateArrivee()));
            pstmt.setString(3, vol.getDestination());
            pstmt.setInt(4, vol.getPlacesDisponibles());
            pstmt.setDouble(5, vol.getPrix());
            pstmt.setString(6, vol.getNumeroVol());

            pstmt.executeUpdate();
        }
    }

    /**
     * Met à jour le nombre de places disponibles pour un vol
     * @param numeroVol Le numéro du vol à mettre à jour
     * @param nouvellesPlacesDisponibles Le nouveau nombre de places disponibles
     * @throws SQLException En cas d'erreur lors de l'accès à la base de données
     */
    public void mettreAJourPlacesDisponibles(String numeroVol, int nouvellesPlacesDisponibles) throws SQLException {
        String requete = "UPDATE vols SET places_disponibles = ? WHERE numero_vol = ?";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete)) {

            pstmt.setInt(1, nouvellesPlacesDisponibles);
            pstmt.setString(2, numeroVol);

            pstmt.executeUpdate();
        }
    }

    /**
     * Supprime un vol de la base de données
     * @param numeroVol Le numéro du vol à supprimer
     * @throws SQLException En cas d'erreur lors de l'accès à la base de données
     */
    public void annulerVol(String numeroVol) throws SQLException {
        String requete = "DELETE FROM vols WHERE numero_vol = ?";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete)) {

            pstmt.setString(1, numeroVol);
            pstmt.executeUpdate();
        }
    }

    /**
     * Récupère la liste des vols les plus populaires
     * @return Une liste des 10 vols les plus réservés
     * @throws SQLException En cas d'erreur lors de l'accès à la base de données
     */
    public List<Vol> obtenirVolsPopulaires() throws SQLException {
        List<Vol> vols = new ArrayList<>();
        String requete = "SELECT v.*, COUNT(r.id) as nombre_reservations " +
                "FROM vols v LEFT JOIN reservations r ON v.numero_vol = r.vol_id " +
                "GROUP BY v.numero_vol " +
                "ORDER BY nombre_reservations DESC " +
                "LIMIT 10";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(requete)) {

            while (rs.next()) {
                vols.add(extraireVolDuResultSet(rs));
            }
        }
        return vols;
    }

    /**
     * Récupère tous les vols de la base de données
     * @return Une liste de tous les vols
     * @throws SQLException En cas d'erreur lors de l'accès à la base de données
     */
    public List<Vol> obtenirTousLesVols() throws SQLException {
        List<Vol> vols = new ArrayList<>();
        String requete = "SELECT * FROM vols";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(requete)) {

            while (rs.next()) {
                vols.add(extraireVolDuResultSet(rs));
            }
        }
        return vols;
    }

    /**
     * Extrait un objet Vol à partir d'un ResultSet
     * @param rs Le ResultSet contenant les données du vol
     * @return Un objet Vol
     * @throws SQLException En cas d'erreur lors de l'accès aux données du ResultSet
     */
    private Vol extraireVolDuResultSet(ResultSet rs) throws SQLException {
        return new Vol(
                rs.getString("numero_vol"),
                rs.getTimestamp("date_depart").toLocalDateTime(),
                rs.getTimestamp("date_arrivee").toLocalDateTime(),
                rs.getString("destination"),
                rs.getInt("places_disponibles"),
                rs.getDouble("prix")
        );
    }
}
