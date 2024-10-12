package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import donnees.ReservationDonnees;
import donnees.VolDonnees;
import donnees.PassagerDonnees;
import modele.Reservation;
import modele.Vol;
import modele.Passager;

public class PanneauGestionReservation extends JPanel implements VolAjouteListener {
    private JComboBox<String> comboBoxVols;
    private JTextField champNomPassager;
    private JTextField champEmailPassager;
    private JTextField champNumeroPasseport;
    private JButton boutonReserver;
    private JTextField champNumeroReservation;
    private JButton boutonRechercher;
    private JTextArea zoneDetailsReservation;
    private JButton boutonConfirmer;
    private JButton boutonAnnuler;
    private ReservationDonnees reservationDonnees;
    private VolDonnees volDonnees;
    private PassagerDonnees passagerDonnees;
    private Reservation reservationActuelle;

    public PanneauGestionReservation() {
        setLayout(new BorderLayout());
        reservationDonnees = new ReservationDonnees();
        volDonnees = new VolDonnees();
        passagerDonnees = new PassagerDonnees();

        JPanel panneauReservation = new JPanel(new GridLayout(5, 2));
        panneauReservation.add(new JLabel("Vol:"));
        comboBoxVols = new JComboBox<>();
        panneauReservation.add(comboBoxVols);
        panneauReservation.add(new JLabel("Nom du passager:"));
        champNomPassager = new JTextField();
        panneauReservation.add(champNomPassager);
        panneauReservation.add(new JLabel("Email du passager:"));
        champEmailPassager = new JTextField();
        panneauReservation.add(champEmailPassager);
        panneauReservation.add(new JLabel("Numéro de passeport:"));
        champNumeroPasseport = new JTextField();
        panneauReservation.add(champNumeroPasseport);
        boutonReserver = new JButton("Réserver");
        panneauReservation.add(boutonReserver);

        JPanel panneauRecherche = new JPanel(new FlowLayout());
        panneauRecherche.add(new JLabel("Numéro de réservation:"));
        champNumeroReservation = new JTextField(10);
        panneauRecherche.add(champNumeroReservation);
        boutonRechercher = new JButton("Rechercher");
        panneauRecherche.add(boutonRechercher);

        JPanel panneauNord = new JPanel(new BorderLayout());
        panneauNord.add(panneauReservation, BorderLayout.NORTH);
        panneauNord.add(panneauRecherche, BorderLayout.SOUTH);

        add(panneauNord, BorderLayout.NORTH);

        zoneDetailsReservation = new JTextArea();
        zoneDetailsReservation.setEditable(false);
        add(new JScrollPane(zoneDetailsReservation), BorderLayout.CENTER);

        JPanel panneauBoutons = new JPanel(new FlowLayout());
        boutonConfirmer = new JButton("Confirmer");
        boutonAnnuler = new JButton("Annuler");
        panneauBoutons.add(boutonConfirmer);
        panneauBoutons.add(boutonAnnuler);

        add(panneauBoutons, BorderLayout.SOUTH);

        boutonReserver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reserverBillet();
            }
        });

        boutonRechercher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rechercherReservation();
            }
        });

        boutonConfirmer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmerReservation();
            }
        });

        boutonAnnuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                annulerReservation();
            }
        });

        chargerVols();
    }

    private void chargerVols() {
        comboBoxVols.removeAllItems();
        try {
            for (Vol vol : volDonnees.obtenirTousLesVols()) {
                comboBoxVols.addItem(vol.getNumeroVol() + " - " + vol.getDestination());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des vols : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void reserverBillet() {
        String volSelectionne = (String) comboBoxVols.getSelectedItem();
        String nomPassager = champNomPassager.getText();
        String emailPassager = champEmailPassager.getText();
        String numeroPasseport = champNumeroPasseport.getText();

        if (volSelectionne == null || nomPassager.isEmpty() || emailPassager.isEmpty() || numeroPasseport.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String numeroVol = volSelectionne.split(" - ")[0];
            Vol vol = volDonnees.obtenirVol(numeroVol);
            if (vol.getPlacesDisponibles() > 0) {
                Passager passager = new Passager(0, nomPassager, emailPassager, numeroPasseport);
                passagerDonnees.ajouterPassager(passager);
                Reservation nouvelleReservation = reservationDonnees.creerReservation(String.valueOf(passager.getId()), vol);
                vol.setPlacesDisponibles(vol.getPlacesDisponibles() - 1);
                volDonnees.mettreAJourVol(vol);

                JOptionPane.showMessageDialog(this, "Réservation créée avec succès. Numéro de réservation : " + nouvelleReservation.getId(), "Succès", JOptionPane.INFORMATION_MESSAGE);
                viderChamps();
            } else {
                JOptionPane.showMessageDialog(this, "Désolé, il n'y a plus de places disponibles sur ce vol.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la réservation : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rechercherReservation() {
        try {
            int numeroReservation = Integer.parseInt(champNumeroReservation.getText());
            reservationActuelle = reservationDonnees.obtenirReservation(numeroReservation);
            if (reservationActuelle != null) {
                zoneDetailsReservation.setText(reservationActuelle.toString());
            } else {
                zoneDetailsReservation.setText("Aucune réservation trouvée avec ce numéro.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un numéro de réservation valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la recherche de la réservation : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmerReservation() {
        if (reservationActuelle != null) {
            try {
                reservationDonnees.confirmerReservation(reservationActuelle.getId());
                JOptionPane.showMessageDialog(this, "Réservation confirmée avec succès.", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                rechercherReservation(); // Rafraîchir les détails de la réservation
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la confirmation de la réservation : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord rechercher une réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void annulerReservation() {
        if (reservationActuelle != null) {
            try {
                reservationDonnees.annulerReservation(reservationActuelle.getId());
                Vol vol = reservationActuelle.getVol();
                vol.setPlacesDisponibles(vol.getPlacesDisponibles() + 1);
                volDonnees.mettreAJourVol(vol);
                JOptionPane.showMessageDialog(this, "Réservation annulée avec succès.", "Annulation", JOptionPane.INFORMATION_MESSAGE);
                rechercherReservation(); // Rafraîchir les détails de la réservation
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'annulation de la réservation : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord rechercher une réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viderChamps() {
        champNomPassager.setText("");
        champEmailPassager.setText("");
        champNumeroPasseport.setText("");
    }

    @Override
    public void volAjoute(Vol nouveauVol) {

    }

    @Override
    public void onVolAjoute(Vol nouveauVol) {
        comboBoxVols.addItem(nouveauVol.getNumeroVol() + " - " + nouveauVol.getDestination());
    }
}
