package modele;

import gui.ZoneCliquable;
import modele.utils.*;

import java.util.ArrayList;

/**
 * Une zone vide. Les zone vide ne servent à rien à part à donner un meilleur rendu
 * à l'interface graphique
 */
public class ZoneVide extends ZoneCliquable {

    public ZoneVide(int width, int height) {


        super(width, height, Couleurs.CASE_VIDE);
    }

    @Override
    public void clicGauche() {

    }

    @Override
    public void clicDroit() {

    }

    @Override
    public ArrayList<Joueur> getLocataires() {
        return null;
    }

    @Override
    public void addJoueur(Joueur j) {

    }

    @Override
    public void removeJoueur(Joueur j) {

    }
}
