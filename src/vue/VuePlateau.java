package vue;

import gui.ZoneCliquable;
import modele.*;
import controleur.*;

import javax.swing.*;
import java.awt.*;

public class VuePlateau extends JPanel {
    private Ile ile;
    private Game game;

    public final int width;
    public final int height;


    public VuePlateau(int width, int height, Ile ile) {
        super(new GridLayout(ile.getWidth(), ile.getHeight()));
        setPreferredSize(new Dimension(width, height));

        this.width = width;
        this.height = height;
        this.ile = ile;

        ZoneCliquable[][] zones = ile.getZones();
        for (ZoneCliquable[] z : zones) {
            for (ZoneCliquable zz : z) {
                this.add(zz);
            }
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
