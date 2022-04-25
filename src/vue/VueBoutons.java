package vue;

import controleur.*;
import modele.*;
import modele.utils.Action;
import gui.FrameGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VueBoutons extends JPanel {
    private final JButton btnAssecher;
    private final JButton btnDeplacer;
    private final JButton btnRamasserArtefact;
    private final JButton btnDonnerCle;
    private final JButton btnTerminerTour;

    private final JTextArea lblInfo;
    private Joueur currentJoueur;
    private Game game;
    private final Ile ile;

    private JPanel buttonPanel;

    private FrameGame parent;


    public VueBoutons(int width, int height, Ile ile, FrameGame parent) {
        super(new FlowLayout(FlowLayout.CENTER, 160, 40));
        this.ile = ile;
        this.parent = parent;

        buttonPanel = new JPanel(new GridLayout(4, 1));

        btnAssecher = new JButton("ASSECHER UNE CASE");
        btnDeplacer = new JButton("SE DEPLACER");
        btnRamasserArtefact = new JButton("RECUPERER ARTEFACT");
        btnDonnerCle = new JButton("DONNER UNE CLEF");
        btnTerminerTour = new JButton("TERMINER LE TOUR");

        buttonPanel.add(btnAssecher);
        buttonPanel.add(btnDeplacer);
        buttonPanel.add(btnRamasserArtefact);
        buttonPanel.add(btnDonnerCle);
        buttonPanel.add(btnTerminerTour);

        lblInfo = new JTextArea();
        lblInfo.setTabSize(2);
        lblInfo.setFont(new Font("Verdana", Font.PLAIN, 12));
        lblInfo.setEnabled(false);
        lblInfo.setText(ile.getJoueurs().get(0).getInfo());
        lblInfo.setSize(100, 1110);

        this.add(lblInfo);
        this.add(buttonPanel);



        btnDeplacer.addActionListener((ActionEvent e) -> {
            btnDeplacerClicked();
        });

        btnAssecher.addActionListener((ActionEvent e) -> {
            btnAssecherClicked();
        });

        btnRamasserArtefact.addActionListener((ActionEvent e) -> {
            btnRamasserArtefactClicked();
        });

        btnDonnerCle.addActionListener((ActionEvent e) -> {
           btnDonnerCleClicked();
        });

        btnTerminerTour.addActionListener((ActionEvent e) -> {
            btnTerminerTourClicked();
        });
    }

    public void setGame(Game g) {
        game = g;
    }

    public void updateDisplay(Joueur j) {
        //lblInfo.setEnabled(true);
        lblInfo.setText(j.getInfo());
        //System.out.println(j.getInfo());
    }



    private void btnDeplacerClicked() {
        if (game.setAction(Action.Deplacer)) {
            if (!game.doAction())
                parent.gameOver();
            else
                ile.update();

            if (game.isWin())
                parent.gameWin();
        }
    }

    private void btnAssecherClicked() {
        if (game.setAction(Action.Assecher)) {
            if (!game.doAction())
                parent.gameOver();
            else
                ile.update();

            if (game.isWin())
                parent.gameWin();
        }
    }

    private void btnRamasserArtefactClicked() {
        if (game.setAction(Action.RamasserArtefact)) {
            if (!game.doAction())
                parent.gameOver();
            else
                ile.update();

            if (game.isWin())
                parent.gameWin();
        }
    }

    private void btnDonnerCleClicked() {
        if (game.setAction(Action.DonnerCle)) {
            if (!game.doAction())
                parent.gameOver();
            else
                ile.update();

            if (game.isWin())
                parent.gameWin();
        }
    }

    private void btnTerminerTourClicked() {
        if (game.setAction(Action.Passer)) {
            if (!game.doAction())
                parent.gameOver();
            else
                ile.update();

            if (game.isWin())
                parent.gameWin();
        }
    }
}
