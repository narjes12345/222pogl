package gui;

import controleur.Game;
import controleur.Main;
import modele.Ile;
import modele.Joueur;
import modele.Zone;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FrameTitre extends JFrame {
    private JPanel cardPanel;
    private final static String FIRST_PANEL = "FirstPanel";
    private final static String SECOND_PANEL = "SecondPanel";


    // First "card" element
    private JPanel firstPanel;
    private JLabel lblTitre;
    private JButton btnJouer;
    private JButton btnRegles;
    private JButton btnQuitter;

    // Second "card" element
    private JPanel secondPanel;
    private JLabel lblNbJoueurs;
    private JComboBox<Integer> cmbJoueurs;
    private JTextField txtJoueur1;
    private JTextField txtJoueur2;
    private JTextField txtJoueur3;
    private JTextField txtJoueur4;
    private JButton btnCommencer;
    private Dimension dimension;

    public FrameTitre() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setResizable(false);
        setTitle("L'Ile Interdite - Maryam Aggab");

        // On ouvre la fenêtre au millieu de l'écran
        dimension  = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((dimension.width / 2) - (getWidth() / 2), (dimension.height / 2) - (getHeight() / 2));

        // On configure le premier panel (choix "jouer", "règles", "quitter")
        firstPanel = createContent();
        firstPanel.setPreferredSize(getSize());
        lblTitre = new JLabel("       L'Ile Interdite      ");
        lblTitre.setFont(new Font("Jetbrains Mono", Font.BOLD, 30));
        lblTitre.setHorizontalAlignment(JLabel.CENTER);
        lblTitre.setOpaque(true);
        lblTitre.setBackground(new Color(244, 213, 73));
        lblTitre.setForeground(Color.BLACK);

        Font btnFont = new Font("Jetbrains Mono", Font.ITALIC | Font.BOLD, 23);

        btnJouer = new JButton("JOUER");

        btnJouer.setFont(btnFont);
        btnJouer.addActionListener((ActionEvent e) -> {
            switchPanel(SECOND_PANEL);
        });

        btnRegles = new JButton("REGLES");
        btnRegles.setFont(btnFont);
        btnRegles.addActionListener(new ActionListener() {
            @Override

                public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(new File( "C:\\Users\\N\\Downloads\\2022\\2022\\src\\vue\\regles.pdf"));
                } catch (IOException ex) {
                    throw new RuntimeException("URL incorrecte");
                }
            }
        });



        btnQuitter = new JButton("QUITTER");
        btnQuitter.setFont(btnFont);
        btnQuitter.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });


        /// Ces labels ne sont là que pour donner un meilleur rendu à l'interface graphique
        firstPanel.add(new JLabel("                                                                         "));
        firstPanel.add(new JLabel("                                                                         "));
        firstPanel.add(new JLabel("                                                                         "));
        firstPanel.add(new JLabel("                                                                         "));
        firstPanel.add(new JLabel("                                                                         "));
        firstPanel.add(new JLabel("                                                                         "));
        firstPanel.add(new JLabel("                                                                         "));
        firstPanel.add(new JLabel("                                                                         "));
        firstPanel.add(new JLabel("                                                                         "));
        firstPanel.add(new JLabel("                                                                         "));
        firstPanel.add(new JLabel("                                                                         "));
        firstPanel.add(new JLabel("                                                                         "));
        firstPanel.add(new JLabel("                                                                         "));
        firstPanel.add(lblTitre, CENTER_ALIGNMENT);
        firstPanel.add(btnJouer, CENTER_ALIGNMENT);
        firstPanel.add(btnRegles, CENTER_ALIGNMENT);
        firstPanel.add(btnQuitter, CENTER_ALIGNMENT);


        // On configure le second panel (choix du nombre de joueurs + leurs noms)
        secondPanel = createContent2();


        lblNbJoueurs = new JLabel("        NOMBRE DE JOUEURS :");
        lblNbJoueurs.setHorizontalAlignment(JLabel.CENTER);
        lblNbJoueurs.setFont(btnFont);

        cmbJoueurs = new JComboBox<>();
        cmbJoueurs.addItem(2);
        cmbJoueurs.addItem(3);
        cmbJoueurs.addItem(4);
        cmbJoueurs.setSelectedItem(2);
        cmbJoueurs.addActionListener((ActionEvent e) -> {
            int id = (int) cmbJoueurs.getSelectedItem();

            txtJoueur3.setEnabled(id >= 3);
            txtJoueur4.setEnabled(id == 4);
        });

        txtJoueur1 = new JTextField("Joueur 1");
        txtJoueur1.setEnabled(true);
        txtJoueur1.setHorizontalAlignment(JLabel.CENTER);
        txtJoueur1.setFont(btnFont);
        txtJoueur2 = new JTextField("Joueur 2");
        txtJoueur2.setEnabled(true);
        txtJoueur1.setHorizontalAlignment(JLabel.CENTER);
        txtJoueur2.setFont(btnFont);
        txtJoueur3 = new JTextField("Joueur 3");
        txtJoueur3.setEnabled(false);
        txtJoueur1.setHorizontalAlignment(JLabel.CENTER);
        txtJoueur3.setFont(btnFont);
        txtJoueur4 = new JTextField("Joueur 4");
        txtJoueur4.setEnabled(false);
        txtJoueur1.setHorizontalAlignment(JLabel.CENTER);
        txtJoueur4.setFont(btnFont);

        btnCommencer = new JButton("COMMENCER");
        btnCommencer.setFont(new Font("Jetbrains Mono", Font.BOLD | Font.ITALIC, 40));
        btnCommencer.addActionListener((ActionEvent e) -> {
            ArrayList<String> listeJoueurs = new ArrayList<String>();
            listeJoueurs.add(txtJoueur1.getText());
            listeJoueurs.add(txtJoueur2.getText());

            if ((int) cmbJoueurs.getSelectedItem() >= 3) {
                listeJoueurs.add(txtJoueur3.getText());
            }
            if ((int) cmbJoueurs.getSelectedItem() == 4) {
                listeJoueurs.add(txtJoueur4.getText());

            }

            setVisible(false);
            Main.startGame(listeJoueurs);
        });

        secondPanel.add(new JLabel("                                                                         "));
        secondPanel.add(new JLabel("                                                                         "));
        secondPanel.add(new JLabel("                                                                         "));
        secondPanel.add(new JLabel("                                                                         "));
        secondPanel.add(new JLabel("                                                                         "));
        secondPanel.add(new JLabel("                                                                         "));
        secondPanel.add(new JLabel("                                                                         "));
        secondPanel.add(new JLabel("                                                                         "));
        secondPanel.add(new JLabel("                                                                         "));
        secondPanel.add(new JLabel("                                                                         "));
        secondPanel.add(new JLabel("                                                                         "));
        secondPanel.add(new JLabel("                                                                         "));
        secondPanel.add(new JLabel("                                                                         "));
        secondPanel.add(lblNbJoueurs);
        secondPanel.add(new JLabel("                  "));
        secondPanel.add(cmbJoueurs);
        secondPanel.add(txtJoueur1);
        secondPanel.add(txtJoueur2);
        secondPanel.add(txtJoueur3);
        secondPanel.add(txtJoueur4);
        secondPanel.add(btnCommencer);


        // On configure notre card panel
        cardPanel = new JPanel(new CardLayout());
        cardPanel.add(firstPanel, FIRST_PANEL);
        cardPanel.add(secondPanel, SECOND_PANEL);

        add(cardPanel, BorderLayout.CENTER);
    }


    private void switchPanel(String name) {
        CardLayout cl = (CardLayout) (cardPanel.getLayout());
        cl.show(cardPanel, name);
    }

    private JPanel createContent() {
        Image image1 = null;
        try {
            image1 = ImageIO.read(new URL("https://plateaumarmots.fr/wp-content/uploads/2018/10/L_ile_interdite_teaseur.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Image image = image1;
        firstPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };


        return firstPanel;
    }
    private JPanel createContent2() {
        Image image1 = null;
        try {
            image1 = ImageIO.read(new URL("https://img.le-dictionnaire.com/ile-ocean.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Image image = image1;
        secondPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 00, null);
            }
        };

        secondPanel.setPreferredSize(getSize());
        secondPanel.setBackground(new Color(244, 213, 73));
        secondPanel.setBackground(Color.CYAN);
        return secondPanel;
    }
}
