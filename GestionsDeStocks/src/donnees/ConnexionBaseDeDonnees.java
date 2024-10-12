package donnees;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBaseDeDonnees {
    private static final String URL = "jdbc:postgresql://localhost:5432/gestion_reservations_voyages";
    private static final String UTILISATEUR = "postgres";
    private static final String MOT_DE_PASSE = "";

    public static Connection obtenirConnexion() throws SQLException {
        return DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
    }
}
