import ui.FenetrePrincipale;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FenetrePrincipale fenetrePrincipale = new FenetrePrincipale();
            fenetrePrincipale.setVisible(true);
        });
    }
}
