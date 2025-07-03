//package vues;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class LoginView extends JFrame {
//    public JTextField nomField;
//    public JPasswordField passwordField;
//    public JButton loginButton;
//    public JButton changePasswordButton;
//
//    public LoginView() {
//        setTitle("Connexion");
//        setSize(300, 160);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
//        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        JLabel nomLabel = new JLabel("Nom:");
//        nomField = new JTextField(10);
//
//        JLabel passwordLabel = new JLabel("Mot de passe:");
//        passwordField = new JPasswordField(10);
//
//        loginButton = new JButton("Connexion");
//        loginButton.setPreferredSize(new Dimension(120, 25));
//
//        changePasswordButton = new JButton("Changer mot de passe");
//        changePasswordButton.setPreferredSize(new Dimension(120, 25));
//
//        panel.add(nomLabel);
//        panel.add(nomField);
//        panel.add(passwordLabel);
//        panel.add(passwordField);
//        panel.add(loginButton);
//        panel.add(changePasswordButton);
//
//        add(panel);
//        setVisible(true);
//    }
//}
package vues;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import util.ConnexionBDD;

public class LoginView extends JFrame {
    public JTextField nomField;
    public JPasswordField passwordField;
    public JButton loginButton;

    public LoginView() {
        setTitle("Connexion");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nomLabel = new JLabel("Nom:");
        nomField = new JTextField(10);

        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordField = new JPasswordField(10);

        loginButton = new JButton("Connexion");
        loginButton.setPreferredSize(new Dimension(120, 25));



        panel.add(nomLabel);
        panel.add(nomField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);


        add(panel);
        setVisible(true);
    }
}
