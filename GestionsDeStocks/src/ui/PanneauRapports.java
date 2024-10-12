package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import donnees.RapportDonnees;
import java.util.Map;

public class PanneauRapports extends JPanel {
    private JButton boutonDestinationsPopulaires;
    private JButton boutonNombreTotalPassagers;
    private JButton boutonStatistiquesReservations;
    private JTextArea zoneResultats;
    private RapportDonnees rapportDonnees;

    public PanneauRapports() {
        setLayout(new BorderLayout());
        rapportDonnees = new RapportDonnees();

        JPanel panneauBoutons = new JPanel(new FlowLayout());
        boutonDestinationsPopulaires = new JButton("Destinations populaires");
        boutonNombreTotalPassagers = new JButton("Nombre total de passagers");
        boutonStatistiquesReservations = new JButton("Statistiques des réservations");

        panneauBoutons.add(boutonDestinationsPopulaires);
        panneauBoutons.add(boutonNombreTotalPassagers);
        panneauBoutons.add(boutonStatistiquesReservations);

        add(panneauBoutons, BorderLayout.NORTH);

        zoneResultats = new JTextArea();
        zoneResultats.setEditable(false);
        add(new JScrollPane(zoneResultats), BorderLayout.CENTER);

        boutonDestinationsPopulaires.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherDestinationsPopulaires();
            }
        });

        boutonNombreTotalPassagers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherNombreTotalPassagers();
            }
        });

        boutonStatistiquesReservations.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherStatistiquesReservations();
            }
        });
    }

    private void afficherDestinationsPopulaires() {
        try {
            Map<String, Integer> destinationsPopulaires = rapportDonnees.obtenirDestinationsPopulaires();
            StringBuilder sb = new StringBuilder("Destinations populaires:\n");
            for (Map.Entry<String, Integer> entry : destinationsPopulaires.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(" réservations\n");
            }
            zoneResultats.setText(sb.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des destinations populaires: " + ex.getMessage());
        }
    }

    private void afficherNombreTotalPassagers() {
        try {
            int nombreTotalPassagers = rapportDonnees.obtenirNombreTotalPassagers();
            zoneResultats.setText("Nombre total de passagers: " + nombreTotalPassagers);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération du nombre total de passagers: " + ex.getMessage());
        }
    }

    private void afficherStatistiquesReservations() {
        try {
            Map<String, Integer> statistiques = rapportDonnees.obtenirStatistiquesReservationsParStatut();
            StringBuilder sb = new StringBuilder("Statistiques des réservations:\n");
            for (Map.Entry<String, Integer> entry : statistiques.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            zoneResultats.setText(sb.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des statistiques des réservations: " + ex.getMessage());
        }
    }
}
