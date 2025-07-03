import controllers.*;
import vues.LoginView;
import controllers.LoginController;
import util.ConnexionBDD;

import javax.swing.*;


public class Main {
    public static void main(String[] args) {

        // Démarrer le seeder pour peupler la base de données
        ConnexionBDD.main(null);


        LoginView view = new LoginView();
        view.setVisible(true);
        new LoginController(view);


    }
//public static void main(String[] args) {
//    SwingUtilities.invokeLater(() -> {
//        verifierEtNotifierDemandes();
//        System.out.println("Vérification des réservations terminée.");
//    });
//}
}