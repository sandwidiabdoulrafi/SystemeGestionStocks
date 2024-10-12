package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import donnees.PassagerDonnees;
import donnees.VolDonnees;
import donnees.ReservationDonnees;
import modele.Passager;
import modele.Vol;
import modele.Reservation;

public class PanneauGestionPassager extends JPanel implements VolAjouteListener {
    private JTextField champNom;
    private JTextField champEmail;
    private JTextField champNumeroPasseport;
    private JComboBox<String> comboBoxVols;
    private JButton boutonAjouter;
    private JList<String> listePassagers;
    private DefaultListModel<String> modeleListePassagers;
    private PassagerDonnees passagerDonnees;
    private VolDonnees volDonnees;
    private ReservationDonnees reservationDonnees;

    public PanneauGestionPassager() {
        setLayout(new BorderLayout());
        passagerDonnees = new PassagerDonnees();
        volDonnees = new VolDonnees();
        reservationDonnees = new ReservationDonnees();

        JPanel panneauSaisie = new JPanel(new GridLayout(5, 2));
        panneauSaisie.add(new JLabel("Nom:"));
        champNom = new JTextField();
        panneauSaisie.add(champNom);
        panneauSaisie.add(new JLabel("Email:"));
        champEmail = new JTextField();
        panneauSaisie.add(champEmail);
        panneauSaisie.add(new JLabel("Numéro de passeport:"));
        champNumeroPasseport = new JTextField();
        panneauSaisie.add(champNumeroPasseport);
        panneauSaisie.add(new JLabel("Vol:"));
        comboBoxVols = new JComboBox<>();
        panneauSaisie.add(comboBoxVols);
        boutonAjouter = new JButton("Ajouter");
        panneauSaisie.add(boutonAjouter);

        add(panneauSaisie, BorderLayout.NORTH);

        modeleListePassagers = new DefaultListModel<>();
        listePassagers = new JList<>(modeleListePassagers);
        add(new JScrollPane(listePassagers), BorderLayout.CENTER);

        boutonAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterPassager();
            }
        });

        chargerVols();
        chargerPassagers();
    }

    private void chargerVols() {
        comboBoxVols.removeAllItems();
        try {
            for (Vol vol : volDonnees.obtenirTousLesVols()) {
                comboBoxVols.addItem(vol.getNumeroVol() + " - " + vol.getDestination() + " (" + vol.getPlacesDisponibles() + " places)");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des vols : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chargerPassagers() {
        modeleListePassagers.clear();
        try {
            for (Passager passager : passagerDonnees.obtenirTousPassagers()) {
                modeleListePassagers.addElement(passager.toString());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des passagers : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ajouterPassager() {
        String nom = champNom.getText();
        String email = champEmail.getText();
        String numeroPasseport = champNumeroPasseport.getText();
        String volSelectionne = (String) comboBoxVols.getSelectedItem();

        if (nom.isEmpty() || email.isEmpty() || numeroPasseport.isEmpty() || volSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Passager nouveauPassager = new Passager(0, nom, email, numeroPasseport);
            passagerDonnees.ajouterPassager(nouveauPassager);

            String numeroVol = volSelectionne.split(" - ")[0];
            Vol vol = volDonnees.obtenirVol(numeroVol);
            if (vol.getPlacesDisponibles() > 0) {
                vol.setPlacesDisponibles(vol.getPlacesDisponibles() - 1);
                volDonnees.mettreAJourVol(vol);

                Reservation nouvelleReservation = reservationDonnees.creerReservation(String.valueOf(nouveauPassager.getId()), vol);

                // Mettre à jour la liste des passagers
                chargerPassagers();

                // Mettre à jour la liste des vols
                chargerVols();

                // Effacer les champs après l'ajout
                champNom.setText("");
                champEmail.setText("");
                champNumeroPasseport.setText("");

                JOptionPane.showMessageDialog(this, "Passager ajouté avec succès et associé au vol " + numeroVol + ". Numéro de réservation : " + nouvelleReservation.getId(), "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Désolé, il n'y a plus de places disponibles sur ce vol.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du passager : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void volAjoute(Vol nouveauVol) {

    }

    @Override
    public void onVolAjoute(Vol nouveauVol) {
        comboBoxVols.addItem(nouveauVol.getNumeroVol() + " - " + nouveauVol.getDestination() + " (" + nouveauVol.getPlacesDisponibles() + " places)");
    }
}
