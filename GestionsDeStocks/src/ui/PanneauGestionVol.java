package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import donnees.VolDonnees;
import modele.Vol;
import util.UtilitaireDate;

public class PanneauGestionVol extends JPanel {
    private List<VolAjouteListener> volAjouteListeners = new ArrayList<>();
    private JTextField champNumeroVol;
    private JTextField champDateDepart;
    private JTextField champHeureDepart;
    private JTextField champDateArrivee;
    private JTextField champHeureArrivee;
    private JTextField champDestination;
    private JTextField champPlacesDisponibles;
    private JTextField champPrix;
    private JButton boutonAjouter;
    private JButton boutonMettreAJour;
    private JButton boutonAnnuler;
    private JList<String> listeVols;
    private DefaultListModel<String> modeleListeVols;
    private VolDonnees volDonnees;

    public PanneauGestionVol() {
        setLayout(new BorderLayout());
        volDonnees = new VolDonnees();

        // Création des composants
        JPanel panneauSaisie = new JPanel(new GridLayout(8, 2));
        panneauSaisie.add(new JLabel("Numéro de vol:"));
        champNumeroVol = new JTextField();
        panneauSaisie.add(champNumeroVol);
        panneauSaisie.add(new JLabel("Date de départ (jj/mm/aaaa):"));
        champDateDepart = new JTextField();
        panneauSaisie.add(champDateDepart);
        panneauSaisie.add(new JLabel("Heure de départ (HH:mm):"));
        champHeureDepart = new JTextField();
        panneauSaisie.add(champHeureDepart);
        panneauSaisie.add(new JLabel("Date d'arrivée (jj/mm/aaaa):"));
        champDateArrivee = new JTextField();
        panneauSaisie.add(champDateArrivee);
        panneauSaisie.add(new JLabel("Heure d'arrivée (HH:mm):"));
        champHeureArrivee = new JTextField();
        panneauSaisie.add(champHeureArrivee);
        panneauSaisie.add(new JLabel("Destination:"));
        champDestination = new JTextField();
        panneauSaisie.add(champDestination);
        panneauSaisie.add(new JLabel("Places disponibles:"));
        champPlacesDisponibles = new JTextField();
        panneauSaisie.add(champPlacesDisponibles);
        panneauSaisie.add(new JLabel("Prix:"));
        champPrix = new JTextField();
        panneauSaisie.add(champPrix);

        boutonAjouter = new JButton("Ajouter");
        boutonMettreAJour = new JButton("Mettre à jour");
        boutonAnnuler = new JButton("Annuler");

        JPanel panneauBoutons = new JPanel(new FlowLayout());
        panneauBoutons.add(boutonAjouter);
        panneauBoutons.add(boutonMettreAJour);
        panneauBoutons.add(boutonAnnuler);

        add(panneauSaisie, BorderLayout.NORTH);
        add(panneauBoutons, BorderLayout.CENTER);

        modeleListeVols = new DefaultListModel<>();
        listeVols = new JList<>(modeleListeVols);
        add(new JScrollPane(listeVols), BorderLayout.SOUTH);

        // Ajout des écouteurs d'événements
        boutonAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterVol();
            }
        });

        boutonMettreAJour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mettreAJourVol();
            }
        });

        boutonAnnuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                annulerVol();
            }
        });

        rafraichirListeVols();
    }

    public void addVolAjouteListener(VolAjouteListener listener) {
        volAjouteListeners.add(listener);
    }

    private void notifierVolAjoute(Vol vol) {
        for (VolAjouteListener listener : volAjouteListeners) {
            listener.onVolAjoute(vol);
        }
    }


    private void ajouterVol() {
        try {
            String numeroVol = champNumeroVol.getText();
            LocalDateTime dateDepart = UtilitaireDate.stringVersLocalDateTime(champDateDepart.getText() + " " + champHeureDepart.getText());
            LocalDateTime dateArrivee = UtilitaireDate.stringVersLocalDateTime(champDateArrivee.getText() + " " + champHeureArrivee.getText());
            String destination = champDestination.getText();
            int placesDisponibles = Integer.parseInt(champPlacesDisponibles.getText());
            double prix = Double.parseDouble(champPrix.getText());

            Vol nouveauVol = new Vol(numeroVol, dateDepart, dateArrivee, destination, placesDisponibles, prix);
            volDonnees.ajouterVol(nouveauVol);
            JOptionPane.showMessageDialog(this, "Vol ajouté avec succès");
            viderChamps();
            rafraichirListeVols();
            notifierVolAjoute(nouveauVol);  // Notifier les listeners
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Erreur de format de date: " + ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du vol dans la base de données: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du vol: " + ex.getMessage());
        }
    }

    private void mettreAJourVol() {
        try {
            String numeroVol = champNumeroVol.getText();
            LocalDateTime dateDepart = UtilitaireDate.stringVersLocalDateTime(champDateDepart.getText() + " " + champHeureDepart.getText());
            LocalDateTime dateArrivee = UtilitaireDate.stringVersLocalDateTime(champDateArrivee.getText() + " " + champHeureArrivee.getText());
            String destination = champDestination.getText();
            int placesDisponibles = Integer.parseInt(champPlacesDisponibles.getText());
            double prix = Double.parseDouble(champPrix.getText());

            Vol volMisAJour = new Vol(numeroVol, dateDepart, dateArrivee, destination, placesDisponibles, prix);
            volDonnees.mettreAJourVol(volMisAJour);
            JOptionPane.showMessageDialog(this, "Vol mis à jour avec succès");
            viderChamps();
            rafraichirListeVols();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Erreur de format de date: " + ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du vol dans la base de données: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du vol: " + ex.getMessage());
        }
    }

    private void annulerVol() {
        try {
            String numeroVol = champNumeroVol.getText();
            volDonnees.annulerVol(numeroVol);
            JOptionPane.showMessageDialog(this, "Vol annulé avec succès");
            viderChamps();
            rafraichirListeVols();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'annulation du vol dans la base de données: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'annulation du vol: " + ex.getMessage());
        }
    }

    private void viderChamps() {
        champNumeroVol.setText("");
        champDateDepart.setText("");
        champHeureDepart.setText("");
        champDateArrivee.setText("");
        champHeureArrivee.setText("");
        champDestination.setText("");
        champPlacesDisponibles.setText("");
        champPrix.setText("");
    }

    private void rafraichirListeVols() {
        try {
            modeleListeVols.clear();
            for (Vol vol : volDonnees.obtenirTousLesVols()) {
                modeleListeVols.addElement(formatVolPourAffichage(vol));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du rafraîchissement de la liste des vols: " + ex.getMessage());
        }
    }

    private String formatVolPourAffichage(Vol vol) {
        return vol.getNumeroVol() + " - " + vol.getDestination() + " - " +
                UtilitaireDate.localDateTimeVersString(vol.getDateDepart()) + " - " +
                UtilitaireDate.localDateTimeVersString(vol.getDateArrivee()) + " - " +
                "Places: " + vol.getPlacesDisponibles() + " - Prix: " + vol.getPrix();
    }
}
