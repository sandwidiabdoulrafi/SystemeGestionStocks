package donnees;

import modele.Reservation;
import modele.Passager;
import modele.Vol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDonnees {

    // Créer une nouvelle réservation
    public Reservation creerReservation(String passagerId, Vol vol) throws SQLException {
        String requete = "INSERT INTO reservations (passager_id, vol_id, statut) VALUES (?, ?, ?)";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, Integer.parseInt(passagerId));
            pstmt.setString(2, vol.getNumeroVol());
            pstmt.setString(3, "EN_ATTENTE");

            int lignesAffectees = pstmt.executeUpdate();

            if (lignesAffectees > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int reservationId = generatedKeys.getInt(1);
                        return new Reservation(reservationId, new Passager(Integer.parseInt(passagerId), "", "", ""), vol, "EN_ATTENTE");
                    }
                }
            }
        }
        throw new SQLException("La création de la réservation a échoué, aucun ID généré.");
    }

    // Confirmer une réservation
    public void confirmerReservation(int reservationId) throws SQLException {
        String requete = "UPDATE reservations SET statut = 'CONFIRMEE' WHERE id = ?";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete)) {

            pstmt.setInt(1, reservationId);
            pstmt.executeUpdate();
        }
    }

    // Annuler une réservation
    public void annulerReservation(int reservationId) throws SQLException {
        String requete = "UPDATE reservations SET statut = 'ANNULEE' WHERE id = ?";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete)) {

            pstmt.setInt(1, reservationId);
            pstmt.executeUpdate();
        }
    }

    // Récupère toutes les réservations avec un statut spécifique
    public List<Reservation> obtenirReservationsParStatut(String statut) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String requete = "SELECT r.id AS reservation_id, p.id AS passager_id, p.nom, p.email, p.numero_passeport, " +
                "v.numero_vol, v.date_depart, v.date_arrivee, v.destination, v.places_disponibles, v.prix, r.statut " +
                "FROM reservations r " +
                "JOIN passagers p ON r.passager_id = p.id " +
                "JOIN vols v ON r.vol_id = v.numero_vol " +
                "WHERE r.statut = ?";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete)) {

            pstmt.setString(1, statut);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(extraireReservationDuResultSet(rs));
                }
            }
        }
        return reservations;
    }

    // Méthode pour obtenir une réservation par son ID
    public Reservation obtenirReservation(int id) throws SQLException {
        String requete = "SELECT r.id AS reservation_id, p.id AS passager_id, p.nom, p.email, p.numero_passeport, " +
                "v.numero_vol, v.date_depart, v.date_arrivee, v.destination, v.places_disponibles, v.prix, r.statut " +
                "FROM reservations r " +
                "JOIN passagers p ON r.passager_id = p.id " +
                "JOIN vols v ON r.vol_id = v.numero_vol " +
                "WHERE r.id = ?";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extraireReservationDuResultSet(rs);
                }
            }
        }
        return null; // Retourne null si aucune réservation n'est trouvée
    }

    // Méthode pour obtenir toutes les réservations d'un passager
    public List<Reservation> obtenirReservationsParPassager(int passagerId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String requete = "SELECT r.id AS reservation_id, p.id AS passager_id, p.nom, p.email, p.numero_passeport, " +
                "v.numero_vol, v.date_depart, v.date_arrivee, v.destination, v.places_disponibles, v.prix, r.statut " +
                "FROM reservations r " +
                "JOIN passagers p ON r.passager_id = p.id " +
                "JOIN vols v ON r.vol_id = v.numero_vol " +
                "WHERE p.id = ?";

        try (Connection connexion = ConnexionBaseDeDonnees.obtenirConnexion();
             PreparedStatement pstmt = connexion.prepareStatement(requete)) {

            pstmt.setInt(1, passagerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(extraireReservationDuResultSet(rs));
                }
            }
        }
        return reservations;
    }

    // Méthode utilitaire pour extraire une réservation d'un ResultSet
    private Reservation extraireReservationDuResultSet(ResultSet rs) throws SQLException {
        Passager passager = new Passager(
                rs.getInt("passager_id"),  // Correctement référencé sans alias
                rs.getString("nom"),
                rs.getString("email"),
                rs.getString("numero_passeport")
        );

        Vol vol = new Vol(
                rs.getString("numero_vol"),
                rs.getTimestamp("date_depart").toLocalDateTime(),
                rs.getTimestamp("date_arrivee").toLocalDateTime(),
                rs.getString("destination"),
                rs.getInt("places_disponibles"),
                rs.getDouble("prix")
        );

        return new Reservation(
                rs.getInt("reservation_id"),  // Correctement référencé sans alias
                passager,
                vol,
                rs.getString("statut")
        );
    }
}