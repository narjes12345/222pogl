package modele;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gui.ZoneCliquable;
import modele.utils.*;
import controleur.Game;

import javax.swing.*;

/*
 * Une zone pouvant contenir un artefact et un/plusieurs joueurs
 */
public class Zone extends ZoneCliquable {
    private Ile ile;                            // L'île dans laquelle la zone se trouve
    private Game game;                          // Le controleur de la partie
    private Artefact artefact;                  // [null] si la zone ne contient pas d'artefact, une valeur sinon
    private Artefact clef;                      // [null] si la zone ne contient pas de clef, une valeur sinon
    private Etat etat;                          // L'état actuel de la zone
    private ArrayList<Joueur> locataires;       // Les joueurs actuellement situés dans cette zone

    private static int nbInstances = 0;         // Le nombre total de zones créées
    private int idInstance;                     // L'identifiant de cette zone
    private Coordonnees co;

    public Zone(int width, int height, Ile ile) {
        super(width, height, Couleurs.fromEtat(Etat.Normal));
        this.ile = ile;
        artefact = null;
        clef = null;
        etat = Etat.Normal;

        locataires = new ArrayList<>();

        nbInstances++;
        idInstance = nbInstances;
    }

    public void setGame(Game game) {
        this.game = game;
    }


    /**
     * @return {@code true} si la zone contient un artefact,
     * {@code false} sinon
     */
    public boolean hasArtefact() {
        return artefact != null;
    }

    /**
     * @return {@code null} si la zone ne contient pas d'artefact,
     * une valeur de type {@link Artefact sinon}
     */
    public Artefact getArtefact() {
        return artefact;
    }

    /**
     * Ajoute un artefact si la zone n'en contient pas déjà un
     *
     * @param value L'artefact à ajouter à cette zone
     * @return {@code true} si l'artefact a été ajouté,
     * {@code false} sinon
     */
    public boolean addArtefact(Artefact value) {
        if (artefact == null) {
            artefact = value;
            return true;
        }

        return false;
    }

    /**
     * Supprime l'artefact de la zone, si elle en contient déjà un
     *
     * @return {@code true} si la zone contenait un artefact,
     * {@code false} sinon
     */
    public boolean removeArtefact() {
        if (artefact != null) {
            artefact = null;
            return true;
        }

        return false;
    }


    /**
     * @return {@code true} si la zone contient une clé,
     * {@code false} sinon
     */
    public boolean hasCle() {
        return clef != null;
    }

    /**
     * @return {@code null} si la zone ne contient pas de clé,
     * une valeur de type {@link Artefact sinon}
     */
    public Artefact getCle() {
        return clef;
    }

    /**
     * Ajoute une clé si la zone n'en contient pas déjà une
     *
     * @param value  La clé à ajouter à cette zone
     *
     * @return {@code true} si la clé a été ajouté,
     *         {@code false} sinon
     */
    public boolean addCle(Artefact value) {
        if (clef == null) {
            clef = value;
            return true;
        }

        return false;
    }


    /**
     * Supprime la clé de la zone, si elle en contient déjà une
     *
     * @return {@code true} si la zone contenait une clé,
     * {@code false} sinon
     */
    public boolean removeCle() {
        if (clef != null) {
            clef = null;
            return true;
        }

        return false;
    }


    /**
     * @return L'état actuel de la Zone
     */
    public Etat getEtat() {
        return etat;
    }

    /**
     * Inonde la zone.
     * Si la zone est {@code Normal}, alors elle devient {@code Inondee},
     * Si la zone est {@code Inonde}, alors elle devient {@code Submergee}
     *
     * @return {@code true} si la zone a changé d'état, {@code false} sinon
     */
    public boolean inonde() {
        if (etat == Etat.Normal) {
            etat = Etat.Inonder;
            color = Couleurs.CASE_INONDER;
            draw();
            return true;
        }

        if (etat == Etat.Inonder) {
            etat = Etat.Submerger;
            color = Couleurs.CASE_SUBMERGER;
            draw();
            return true;
        }

        return false;
    }

    /**
     * Assèche une zone.
     * Cette fonction n'a d'effet que sur les zones inondées
     *
     * @return {@code true} si la zone a été asséchée, {@code false} sinon
     */
    public boolean asseche() {
        if (etat != Etat.Submerger) {
            etat = Etat.Normal;
            color = Couleurs.CASE_NORMALE;
            draw();
            return true;
        }

        return false;
    }


    /**
     * @return {@code true} si la zone contient au moins un joueur,
     * {@code false} sinon
     */
    public boolean containsJoueur() {
        return !locataires.isEmpty();
    }

    /**
     * Ajoute un joueur dans cette zone. Si le joueur se trouve déjà dans la
     * zone, cette fonction n'a aucun effet.
     *
     * @param j Le joueur à ajouter
     */
    public void addJoueur(Joueur j) {
        if (!locataires.contains(j)) {
            locataires.add(j);
        }
    }

    /**
     * Supprime un joueur de cette zone.
     *
     * @param j Le joueur à supprimer de la zone
     */
    public void removeJoueur(Joueur j) {
        locataires.remove(j);
    }


    /**
     * @return L'identifiant de cette zone
     */
    public int getId() {
        return idInstance;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Zone ");
        sb.append(idInstance);
        sb.append(": {etat: ");
        sb.append(etat);
        sb.append(", nbJoueurs: ");
        sb.append(containsJoueur());
        sb.append(", artefact: ");

        if (hasArtefact())
            sb.append(artefact.toString());
        else
            sb.append("non");

        sb.append(", clé: ");
        if (hasCle())
            sb.append(clef.toString());
        else
            sb.append("non");

        sb.append("}");

        return sb.toString();
    }

    @Override
    public void clicDroit() {

    }

    @Override
    public void clicGauche() {
        game.zoneClicked(this);
    }


    @Override
    public void draw() {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = img.createGraphics();

        graphics.setColor(color);
        graphics.fillRect(0, 0, width, height);

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

        // On dessine l'artefact
        if (hasArtefact()) {
            graphics.setColor(Couleurs.fromArtefact(artefact));
            graphics.fillRect((width/2) - 5, (height/2) - 5, 10, 10);
            graphics.setColor(Color.BLACK);
            graphics.drawRect((width/2)-5, (height/2)-5, 10, 10);
        }

        this.setIcon(new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_DEFAULT)));
        this.repaint();
    }

    public ArrayList<Joueur> getLocataires() {
        return locataires;
    }
    public void setCo(int x, int y){
        this.co = new Coordonnees(x, y);
    }
    public class ZoneHeliport extends ZoneCliquable {
        private ArrayList<Joueur> locataires = new ArrayList<>();
        private Etat etat;
        public ZoneHeliport(int width, int height) {
            super(width, height, Couleurs.CASE_HELIPORT);
            etat = Etat.Normal;
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

        }

        @Override
        public void clicDroit() {

        }

        @Override
        public void setEtat(Etat etat) {
            this.etat = etat;
        }

        @Override
        public ArrayList<Joueur> getLocataires() {
            return null;
        }
        public void move(Joueur j){
            locataires.remove(j);
        }
    }
}
