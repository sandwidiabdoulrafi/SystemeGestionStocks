package donnees;

import modele.Vol;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class VolDonnees {

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

   
    public void mettreAJourPlacesDisponibles(String numeroVol, int nouvellesPlacesDisponibles) throws SQLException {
        String requete = "UPDATE vols SET places_disponibles = ? WHERE numero_vol = ?";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete)) {

            pstmt.setInt(1, nouvellesPlacesDisponibles);
            pstmt.setString(2, numeroVol);

            pstmt.executeUpdate();
        }
    }

   
    public void annulerVol(String numeroVol) throws SQLException {
        String requete = "DELETE FROM vols WHERE numero_vol = ?";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete)) {

            pstmt.setString(1, numeroVol);
            pstmt.executeUpdate();
        }
    }

    
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
