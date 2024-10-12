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

public class PanneauRechercheVol extends JPanel implements VolAjouteListener {
    private JTextField champDestination;
    private JTextField champDateDepart;
    private JTextField champHeureDepart;
    private JTextField champNombrePassagers;
    private JButton boutonRechercher;
    private JList<String> listeResultats;
    private DefaultListModel<String> modeleListeResultats;
    private VolDonnees volDonnees;

    public PanneauRechercheVol() {
        setLayout(new BorderLayout());
        volDonnees = new VolDonnees();

        JPanel panneauSaisie = new JPanel(new GridLayout(5, 2));
        panneauSaisie.add(new JLabel("Destination:"));
        champDestination = new JTextField();
        panneauSaisie.add(champDestination);
        panneauSaisie.add(new JLabel("Date de départ (jj/mm/aaaa):"));
        champDateDepart = new JTextField();
        panneauSaisie.add(champDateDepart);
        panneauSaisie.add(new JLabel("Heure de départ (HH:mm):"));
        champHeureDepart = new JTextField();
        panneauSaisie.add(champHeureDepart);
        panneauSaisie.add(new JLabel("Nombre de passagers:"));
        champNombrePassagers = new JTextField();
        panneauSaisie.add(champNombrePassagers);
        boutonRechercher = new JButton("Rechercher");
        panneauSaisie.add(boutonRechercher);

        add(panneauSaisie, BorderLayout.NORTH);

        modeleListeResultats = new DefaultListModel<>();
        listeResultats = new JList<>(modeleListeResultats);
        add(new JScrollPane(listeResultats), BorderLayout.CENTER);

        boutonRechercher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rechercherVols();
            }
        });
    }

    private void rechercherVols() {
        String destination = champDestination.getText();
        String dateDepart = champDateDepart.getText();
        String heureDepart = champHeureDepart.getText();
        String nombrePassagersStr = champNombrePassagers.getText();

        if (destination.isEmpty() || dateDepart.isEmpty() || heureDepart.isEmpty() || nombrePassagersStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDateTime dateHeure = UtilitaireDate.stringVersLocalDateTime(dateDepart + " " + heureDepart);
            int nombrePassagers = Integer.parseInt(nombrePassagersStr);
            List<Vol> vols = volDonnees.rechercherVols(destination, dateHeure, nombrePassagers);

            modeleListeResultats.clear();
            for (Vol vol : vols) {
                modeleListeResultats.addElement(formatVolPourAffichage(vol));
            }

            if (modeleListeResultats.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucun vol trouvé avec suffisamment de places disponibles", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Le nombre de passagers doit être un nombre entier", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Format de date ou d'heure invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void volAjoute(Vol nouveauVol) {
        String destination = champDestination.getText();
        String dateDepart = champDateDepart.getText();
        String heureDepart = champHeureDepart.getText();
        String nombrePassagersStr = champNombrePassagers.getText();

        if (!destination.isEmpty() && !dateDepart.isEmpty() && !heureDepart.isEmpty() && !nombrePassagersStr.isEmpty()) {
            try {
                LocalDateTime dateHeure = UtilitaireDate.stringVersLocalDateTime(dateDepart + " " + heureDepart);
                int nombrePassagers = Integer.parseInt(nombrePassagersStr);

                if (nouveauVol.getDestination().equalsIgnoreCase(destination) &&
                        !nouveauVol.getDateDepart().isBefore(dateHeure) &&
                        nouveauVol.getPlacesDisponibles() >= nombrePassagers) {

                    modeleListeResultats.addElement(formatVolPourAffichage(nouveauVol));

                    List<String> listeTriee = new ArrayList<>();
                    for (int i = 0; i < modeleListeResultats.size(); i++) {
                        listeTriee.add(modeleListeResultats.get(i));
                    }
                    listeTriee.sort((s1, s2) -> {
                        Vol v1 = extraireVolDeString(s1);
                        Vol v2 = extraireVolDeString(s2);
                        return v1.getDateDepart().compareTo(v2.getDateDepart());
                    });

                    modeleListeResultats.clear();
                    for (String s : listeTriee) {
                        modeleListeResultats.addElement(s);
                    }
                }
            } catch (DateTimeParseException | NumberFormatException ex) {
                // Ignorer les erreurs de parsing, ne pas mettre à jour la liste
            }
        }
    }

    private String formatVolPourAffichage(Vol vol) {
        return vol.getNumeroVol() + " - " + vol.getDestination() + " - " +
                UtilitaireDate.localDateTimeVersString(vol.getDateDepart()) + " - " +
                "Places disponibles: " + vol.getPlacesDisponibles();
    }

    private Vol extraireVolDeString(String volString) {
        String numeroVol = volString.split(" - ")[0];
        try {
            return volDonnees.obtenirVol(numeroVol);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onVolAjoute(Vol vol) {

    }
}
