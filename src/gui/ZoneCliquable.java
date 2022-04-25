/// Inspiré du code fourni par Thibault Balabonski

package gui;

import modele.Joueur;
import modele.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Une classe abstraite servant de base pour les classes Zone, ZoneHeliport et ZoneVide
 */
public abstract class ZoneCliquable extends JLabel implements MouseListener {
    public final int width;
    public final int height;
    protected Color color;
    protected Etat etat;
    public Coordonnees co;
    public ZoneCliquable(int width, int height, Color color) {
        this.width = width;
        this.height = height;
        this.color = color;

        setPreferredSize(new Dimension(width, height));
        addMouseListener(this);
        setBackground(color);

        draw();
    }


    public abstract void clicGauche();
    public abstract void clicDroit();

    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e))
            clicDroit();
        else
            clicGauche();
    }

    public void mouseEntered(MouseEvent e)   {}
    public void mouseExited(MouseEvent e)    {}
    public void mousePressed(MouseEvent e)   {}
    public void mouseReleased(MouseEvent e)  {}


    public void draw() {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = img.createGraphics();

        graphics.setColor(color);
        graphics.fillRect(0, 0, width, height);

        this.setIcon(new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_DEFAULT)));
        this.repaint();
    }

    public void draw(Color newColor) {
        color = newColor;
        draw();
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public Etat getEtat() {
        return etat;
    }
    public void setCo(int x, int y){
        this.co = new Coordonnees(x, y);
    }

    public Coordonnees getCo() {
        return co;
    }
    public abstract ArrayList<Joueur> getLocataires();

    public abstract void addJoueur(Joueur j);
    public abstract void removeJoueur(Joueur j);
}
