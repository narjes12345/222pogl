package modele;

import gui.ZoneCliquable;
import modele.utils.*;
import controleur.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/*
 * Une zone représentant l'héliport. Elle peut contenir un/plusieurs joueurs mais pas d'artefacts.
 */
public class ZoneHeliport extends Zone {
    private ArrayList<Joueur> locataires = new ArrayList<>();
    private Etat etat;

    private Game game;

    public ZoneHeliport(int width, int height, Ile i) {
        super(width, height,i );
        etat = Etat.Normal;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public void draw() {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = img.createGraphics();

        graphics.setColor(color);
        graphics.fillRect(0, 0, width, height);

        // On dessine le "H" de l'héliport
        graphics.setColor(Color.WHITE);
        graphics.fillRect(width/3, height/3, width/10, height/2);
        graphics.fillRect(2*(width/3), (height/3), width/10, height/2);
        graphics.fillRect((2*(width/3)) - (width/4),  (height/4) + (height/3), width/4, height/10);

        // On Dessine les joueurs
        for (int i = 0; locataires != null && i < locataires.size(); i++) {
            Color c = locataires.get(i).idInstance == 1 ? Color.BLUE
                    : locataires.get(i).idInstance == 2 ? Color.orange
                    : locataires.get(i).idInstance == 3 ? Color.RED
                    : Color.GRAY;

            graphics.setColor(c);

            int x = (i == 0 || i == 2) ? 0 : width - Joueur.TailleJoueur;
            int y = (i == 0 || i == 1) ? 0 : height - Joueur.TailleJoueur;

            graphics.fillRect(x, y, Joueur.TailleJoueur, Joueur.TailleJoueur);
        }

        this.setIcon(new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_DEFAULT)));
        this.repaint();
    }

    @Override
    public void addJoueur(Joueur j) {
        if (!locataires.contains(j))
            locataires.add(j);
    }

    @Override
    public void removeJoueur(Joueur j) {
        locataires.remove(j);
    }

    @Override
    public void clicGauche() {
        game.zoneClicked(this);
    }

    @Override
    public void clicDroit() {

    }


    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    @Override
    public ArrayList<Joueur> getLocataires() {
        return locataires;
    }
    public void move(Joueur j){
        locataires.remove(j);
    }
}
