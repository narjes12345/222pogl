package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import modele.utils.*;
import modele.*;
import controleur.Game;

public class DialogEchangeCle extends JFrame {

    public static Joueur joueur;
    public static Artefact clef;

    private Game game;

    public DialogEchangeCle(ArrayList<Joueur> joueurs, ArrayList<Artefact> cles, Game game) {
        super("Echange de Clef");
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.game = game;

        JLabel labelNom = new JLabel("Joueur receveur: ");
        JLabel labelArtefact = new JLabel("Clé: ");

        JComboBox<String> comboJoueur = new JComboBox<>();
        JComboBox<String> comboCle = new JComboBox<>();

        for (Joueur j : joueurs) {
            comboJoueur.addItem(j.nom);
        }

        for (Artefact a : cles) {
            comboCle.addItem(a.toString());
        }


        comboCle.addActionListener((ActionEvent e) -> {
            for (Artefact a : cles) {
                if (a.toString().equals(comboCle.getSelectedItem())) {
                    clef = a;
                    break;
                }
            }
        });

        comboJoueur.addActionListener((ActionEvent e) -> {
            for (Joueur j : joueurs) {
                if (j.nom.equals(comboJoueur.getSelectedItem())) {
                    joueur = j;
                    break;
                }
            }
        });

        JButton okButton = new JButton("OK");

        okButton.addActionListener((ActionEvent e) -> {
            game.doAction();
            dispose();
        });

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(labelNom);
        panel.add(comboJoueur);
        panel.add(labelArtefact);
        panel.add(comboCle);
        panel.add(okButton);

        this.add(panel);
        this.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width/2)-(getWidth()/2), (screenSize.height/2) - (getHeight()/2));

        joueur = joueurs.get(0);
        clef = cles.get(0);
    }
}
