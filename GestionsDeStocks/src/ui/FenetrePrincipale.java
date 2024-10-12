package ui;

import javax.swing.*;
import java.awt.*;

public class FenetrePrincipale extends JFrame {
    private PanneauRechercheVol panneauRechercheVol;
    private PanneauGestionReservation panneauGestionReservation;
    private PanneauGestionPassager panneauGestionPassager;
    private PanneauGestionVol panneauGestionVol;
    private PanneauRapports panneauRapports;
    private PanneauAgenceVoyage panneauAgenceVoyage;

    public FenetrePrincipale() {
        setTitle("Gestion des Réservations de Voyages");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialisation des panneaux
        panneauRechercheVol = new PanneauRechercheVol();
        panneauGestionReservation = new PanneauGestionReservation();
        panneauGestionPassager = new PanneauGestionPassager();
        panneauGestionVol = new PanneauGestionVol();
        panneauRapports = new PanneauRapports();
        panneauAgenceVoyage = new PanneauAgenceVoyage();

        // Configuration des listeners pour la mise à jour des vols
        panneauGestionVol.addVolAjouteListener(panneauRechercheVol);
        panneauGestionVol.addVolAjouteListener(panneauGestionReservation);
        panneauGestionVol.addVolAjouteListener(panneauGestionPassager);

        // Création des onglets
        JTabbedPane onglets = new JTabbedPane();
        onglets.addTab("Recherche de Vols", panneauRechercheVol);
        onglets.addTab("Gestion des Réservations", panneauGestionReservation);
        onglets.addTab("Gestion des Passagers", panneauGestionPassager);
        onglets.addTab("Gestion des Vols", panneauGestionVol);
        onglets.addTab("Rapports", panneauRapports);
        onglets.addTab("Agences de Voyage", panneauAgenceVoyage);

        // Ajout des onglets à la fenêtre principale
        add(onglets);
    }
}
