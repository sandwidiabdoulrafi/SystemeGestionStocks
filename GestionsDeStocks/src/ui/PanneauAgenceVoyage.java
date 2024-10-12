package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import donnees.AgenceVoyageDonnees;
import modele.AgenceVoyage;

public class PanneauAgenceVoyage extends JPanel {
    private JComboBox<String> comboBoxAgences;
    private JTextArea zoneDetailsAgence;
    private JTextArea zoneOffresSpeciales;
    private AgenceVoyageDonnees agenceVoyageDonnees;

    public PanneauAgenceVoyage() {
        setLayout(new BorderLayout());
        agenceVoyageDonnees = new AgenceVoyageDonnees();

        JPanel panneauSelection = new JPanel(new FlowLayout());
        panneauSelection.add(new JLabel("Sélectionner une agence:"));
        comboBoxAgences = new JComboBox<>();
        panneauSelection.add(comboBoxAgences);

        add(panneauSelection, BorderLayout.NORTH);

        JPanel panneauCentral = new JPanel(new GridLayout(1, 2));
        zoneDetailsAgence = new JTextArea();
        zoneDetailsAgence.setEditable(false);
        panneauCentral.add(new JScrollPane(zoneDetailsAgence));

        zoneOffresSpeciales = new JTextArea();
        zoneOffresSpeciales.setEditable(false);
        panneauCentral.add(new JScrollPane(zoneOffresSpeciales));

        add(panneauCentral, BorderLayout.CENTER);

        comboBoxAgences.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherDetailsAgence();
            }
        });

        chargerAgences();
    }

    private void chargerAgences() {
        try {
            for (AgenceVoyage agence : agenceVoyageDonnees.obtenirToutesAgences()) {
                comboBoxAgences.addItem(agence.getNom());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des agences : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void afficherDetailsAgence() {
        String nomAgence = (String) comboBoxAgences.getSelectedItem();
        if (nomAgence != null) {
            try {
                AgenceVoyage agence = agenceVoyageDonnees.obtenirAgenceParNom(nomAgence);
                zoneDetailsAgence.setText("Nom: " + agence.getNom() + "\n" +
                        "Adresse: " + agence.getAdresse() + "\n" +
                        "Téléphone: " + agence.getTelephone());

                zoneOffresSpeciales.setText("Offres spéciales:\n" + agenceVoyageDonnees.obtenirOffresSpeciales(agence.getId()));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'affichage des détails de l'agence : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
