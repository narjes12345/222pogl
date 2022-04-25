package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import controleur.*;
import modele.*;
import vue.*;

public class FrameGame extends JFrame {
    private Game game;
    private Ile ile;
    private ArrayList<Joueur> joueurs;

    private VuePlateau plateau;
    private VueBoutons boutons;


    public static int width = 1000;
    public static int height = 550;


    public FrameGame(Game game, Ile ile, ArrayList<Joueur> joueurs) {
        super();
        super.setLayout(new GridLayout(1, 2));
        super.setTitle("Ile Interdite");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setPreferredSize(new Dimension(width, height));
        super.setLocation((screenSize.width/2) - (width/2), (screenSize.height/2) - (height/2));

        this.game = game;
        this.ile = ile;
        this.joueurs = joueurs;

        plateau = new VuePlateau(100, 100, ile);
        boutons = new VueBoutons(100, 100, ile, this);

        boutons.setGame(game);
        game.setDisplay(this);

        this.add(plateau);
        this.add(boutons);

        this.setVisible(true);
        this.setResizable(false);
        this.pack();
    }

    public void gameOver() {
        System.out.println("GAME OVER");
    }

    public void updateJoueur(Joueur j) {
        boutons.updateDisplay(j);
    }
}
