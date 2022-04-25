package controleur;

import java.util.ArrayList;

import gui.FrameGame;
import gui.FrameTitre;
import modele.*;
import modele.utils.*;
import vue.*;

public class Main {

    public static void main(String[] args) {
        FrameTitre titres = new FrameTitre();
        titres.setVisible(true);

    }

    public static void startGame(ArrayList<String> listeJoueurs) {
        Ile ile = new Ile(6, 80);

        ArrayList<Joueur> joueurs = new ArrayList<>();
        for (String s : listeJoueurs) {
            Joueur j = new Joueur(s, (Zone) ile.getSpawnZone(), ile);
            ile.addJoueur(j);
            ile.getSpawnZone().addJoueur(j);
            joueurs.add(j);
        }

        Game game = new Game(ile, joueurs);
        ile.update();

        FrameGame frame = new FrameGame(game, ile, joueurs);
        frame.setVisible(true);
    }

}
