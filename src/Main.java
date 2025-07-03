import vues.LoginView;
import controllers.LoginController;
import util.ConnexionBDD;

public class Main {
    public static void main(String[] args) {

        // Démarrer le seeder pour peupler la base de données
        ConnexionBDD.main(null);


        LoginView view = new LoginView();
        view.setVisible(true);
        new LoginController(view);
    }
}