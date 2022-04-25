package controleur;

import gui.FrameGame;
import gui.ZoneCliquable;
import gui.DialogEchangeCle;
import modele.*;
import modele.utils.*;
import modele.utils.Action;
import vue.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;


public class Game {

    private Ile ile;
    private ArrayList<Joueur> joueurs;
    private ArrayList<ZoneCliquable> savedZones;
    public ZoneCliquable selectedZone;

    private Action currentAction;

    private int tourJoueur;
    private int remainingActions;

    private boolean waitForAction;

    private FrameGame display;

    Joueur currentJoueur;
    private static Stack<Artefact> piochesInnondation;
    private Stack<Artefact> défausseInondation;
    private static Stack<Artefact> piocheCles;
    private static Stack<Artefact> defausseCles;

    public Game(Ile ile, ArrayList<Joueur> joueurs) {
        this.ile = ile;
        this.joueurs = joueurs;
        tourJoueur = 0;
        currentAction = Action.None;
        remainingActions = 3;
        waitForAction = false;
        savedZones = new ArrayList<>();
         currentJoueur = getCurrentJoueur();
        for (ZoneCliquable[] z : ile.getZones()) {
            for (ZoneCliquable zz : z) {
                if (zz instanceof Zone) {
                    ((Zone)zz).setGame(this);
                } else if (zz instanceof ZoneHeliport) {
                    ((ZoneHeliport)zz).setGame(this);
                }
            }
        }
        piocheCles = new Stack<>();
        defausseCles = new Stack<>();
    }

    private static void piocherCarte(Joueur j) {

        System.out.println(piocheCles.size());
        if (piocheCles.isEmpty()) {
            System.out.println("IL N'Y A PLUS DE CLES");

            piocheCles.addAll(defausseCles);
            defausseCles.clear();

            Collections.shuffle(piocheCles);
            System.out.println("mélanger.");
        }

        Artefact clé = piocheCles.firstElement();
        j.getClefs().add(clé);
        System.out.println("Vous avez une clé d'élément :"+clé.toString());
        piocheCles.remove(0);
    }

    public void setDisplay(FrameGame display) {
        this.display = display;
    }


    private void nextTour() {
        tourJoueur++;

        if (tourJoueur >= joueurs.size()) {
            tourJoueur = 0;
        }
        if (remainingActions == 0) {
            display.updateJoueur(getCurrentJoueur());
            remainingActions = 3;
        }
    }
    private void nextAction() {
        remainingActions--;
        System.out.println(remainingActions);

        if (remainingActions <= 0) {
            ile.submergeRandom();
            remainingActions = 0;
            nextTour();
        }
    }

    public boolean setAction(Action e) {
        if (!waitForAction) {
            currentAction = e;
            return true;
        }

        return false;
    }


    public boolean doAction() {
        switch (currentAction) {
            case Assecher:
                if (waitForAction) {
                    waitForAction  = false;
                    assecherLastStep();
                } else {
                    waitForAction = true;
                    assecherFirstStep();
                }
                break;


            case Deplacer:
                if (waitForAction) {
                    waitForAction = false;
                    deplacerLastStep();
                } else {
                    waitForAction = true;
                    deplacerFirstStep();
                }
                break;


            case DonnerCle:

                                if(currentJoueur.getPosition().getLocataires().size()> 1 && currentJoueur.getCles().size()<6) {
                        donnerCleFirstStep();
                        waitForAction = false;
                    }  else if (currentJoueur.getCles().size() > 5) {
                        waitForAction = false;
                            JOptionPane.showMessageDialog(display, "VOUS AVEZ DEJA 5 CLES");
                        } else if (currentJoueur.getPosition().getLocataires().size() < 2) {
                            JOptionPane.showMessageDialog(display, "VOUS DEVEZ ETRE SUR LA MEME ZONE  D'AUTRES JOUEURS POUR ECHANGER DES CLES");
                       System.out.println(   currentJoueur.getPosition().toString());

                            waitForAction = false;
                        }else if(currentJoueur.getCles().size() ==0){
                                    JOptionPane.showMessageDialog(display, "VOUS N'AVEZ PLUS DE CLES");

                                }
                                deplacerLastStep();



                break;

            case Passer:
            case TerminerTour:
                remainingActions = 0;
                nextAction();
                break;

            case RamasserArtefact:
                if (!waitForAction)
                    recupererArtefactStep();
                break;

            case None:
                break;
        }

        return !isLoose();
        //nextAction();
    }

    public Joueur getCurrentJoueur() {
        return joueurs.get(tourJoueur);
    }

    public ZoneCliquable getSelectedZone() {
        return selectedZone;
    }

    public void setSelectedZone(ZoneCliquable z) {
        selectedZone = z;
    }

    public boolean isWaitingForAction() {
        return waitForAction;
    }

    public ArrayList<ZoneCliquable> getSavedZone() {
        return savedZones;
    }


    private void assecherFirstStep() {
        Joueur current = getCurrentJoueur();
        savedZones = ile.getNeighbourZones(current.getPosition(), false);
        for (ZoneCliquable z : savedZones) {
            if (z instanceof Zone) {
                ((Zone)z).draw(Couleurs.CASE_HIGHLIGHT);
            }
        }
    }


    private void assecherLastStep() {
        Joueur current = getCurrentJoueur();

        if (selectedZone instanceof Zone && ((Zone) selectedZone).getEtat() == Etat.Inonder && selectedZone != null) {
            ((Zone)selectedZone).asseche();
            for (ZoneCliquable z : savedZones) {
                if (z instanceof Zone)
                    z.draw(Couleurs.fromEtat(((Zone)z).getEtat()));
            }

            waitForAction = false;
            nextAction();
        } else {
            JOptionPane.showMessageDialog(display, "AUCUNE ZONE ASSECHABLES AUTOUR DE VOUS ! CHOISISSEZ UNE AUTRE ACTION !");
            System.err.println("Erreur: la zone selectionnée ne peut pas être assechée");
            remainingActions ++;

        }
        refresh();
        nextAction();
    }



    private void deplacerFirstStep() {
        Joueur current = getCurrentJoueur();
        savedZones = ile.getNeighbourZones(current.getPosition(), true);
        for (ZoneCliquable z : savedZones) {
            if (z instanceof Zone) {
                ((Zone)z).draw(Couleurs.CASE_HIGHLIGHT);
            }
        }
    }
    private void deplacerLastStep() {
        if ((selectedZone instanceof Zone) && ((Zone) selectedZone).getEtat() != Etat.Submerger ){
          for (ZoneCliquable z : savedZones) {
              if (z instanceof Zone ) {
                  ((Zone) z).draw(Couleurs.fromEtat(((Zone) z).getEtat()));
              }
              if( z instanceof ZoneHeliport){
                  System.out.println("cliue heliport");
                  ( (ZoneHeliport)  z).draw(Couleurs.fromEtat(((Zone) z).getEtat()));
              }
          }

        }else{
        System.err.println("Erreur: la zone selectionnée n'est pas accessible");
            JOptionPane.showMessageDialog(display, "VEUILLEZ SELECTIONNER UNE ZONE ACCESSIBLE");

            remainingActions++;
            }

        refresh();
        nextAction();
    }



    private void donnerCleFirstStep() {
        Thread t = new Thread() {
            public void run() {
                ArrayList<Joueur> joueurs = ile.getJoueurs();
                joueurs.remove(getCurrentJoueur());
                DialogEchangeCle dial = new DialogEchangeCle(joueurs, getCurrentJoueur().getCles(), Game.this);
                dial.setVisible(true);

                synchronized (dial) {
                    while (dial.isVisible()) {
                        try {
                            dial.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        t.start();
    }
    private void donnerCleLastStep() {
        Joueur j2 = DialogEchangeCle.joueur;
        Artefact cle = DialogEchangeCle.clef;

        getCurrentJoueur().retireCle(cle);
        j2.ajouteCle(cle);


        display.updateJoueur(getCurrentJoueur());
        nextAction();
    }

    public void recupererArtefactStep() {
        ZoneCliquable pos = getCurrentJoueur().getPosition();

        if (pos instanceof Zone && ((Zone)pos).hasArtefact() ) {
            if( currentJoueur.artefactCle(((Zone) pos).getArtefact())){
            Zone zone = (Zone)pos;
            System.err.println("récuperer artefact ok");
            getCurrentJoueur().ajouteArtefact(zone.getArtefact());
            zone.removeArtefact();
            display.updateJoueur(getCurrentJoueur());
            nextAction();
            System.out.println("artefat recup");
             }
                JOptionPane.showMessageDialog(display, "VOUS DEVEZ POSSEDER LES 4 CLES DE L'ELEMENT : "+ ((Zone)pos).getArtefact().toString());
                System.err.println("vous n'avez pas les clés");
                remainingActions++;
            }
        if (pos instanceof Zone && !((Zone)pos).hasArtefact()){

            JOptionPane.showMessageDialog(display, "LA ZONE OU VOUS TROUVEZ NE CONTIENT PAS D'ARTEFACTS !");
            System.err.println("Erreur: la case ne contient pas d'artefact");
            remainingActions++;
            nextAction();
        }
    }


    public void zoneClicked(ZoneCliquable zone) {
        setSelectedZone(zone);

        if (savedZones.contains(zone)) {
            switch (currentAction) {
                case Assecher:
                    if (zone instanceof Zone && waitForAction) {
                        selectedZone = zone;
                        doAction();
                    }
                    break;


                case Deplacer:
                    if (!(zone instanceof ZoneVide) && waitForAction) {
                        Joueur current = joueurs.get(tourJoueur);
                        if (zone instanceof Zone) {
                            Zone zz = (Zone) zone;
                            current.getPosition().removeJoueur(current);
                            zz.addJoueur(current);
                            current.move(zz);
                        }
                       if(zone instanceof ZoneHeliport){
                            ZoneHeliport zz = (ZoneHeliport) zone;
                            current.getPosition().removeJoueur(current);
                            zz.addJoueur(current);
                            current.move(zz);
                        }
                        doAction();
                    }
                    break;

                default:
                    break;
            }
        }
    }

    public void refresh() {
        for (ZoneCliquable[] z : ile.getZones()) {
            for (ZoneCliquable zz : z) {
                zz.draw();
            }
        }
    }

    public Ile getIle() {
        return ile;
    }
    public void getCle(){
        if(currentJoueur.getPosition().hasCle()){
            currentJoueur.ajouteCle(currentJoueur.getPosition().getCle());
                currentJoueur.getPosition().removeCle();
            currentJoueur.getPosition().draw();
            }
        }
    /**
     * @param defausse the defausseCarteTresor to set
     */
    public static void setDefausseCarteTresor(Stack<Artefact> defausse) {
        defausseCles = defausse;
    }

    public static Stack<Artefact> getDefausseCles() {
        return defausseCles;
    }


    public boolean isLoose() {
        ArrayList<Artefact> submergedArtefacts = new ArrayList<>();

        for (ZoneCliquable[] z : ile.getZones()) {
            for (ZoneCliquable zz : z) {
                if (zz instanceof Zone) {
                    Zone current = (Zone)zz;

                    // Tout les artefacts d'un même type sont dans des zone submergées
                    if (current.hasArtefact() && current.getEtat() == Etat.Submerger) {
                        Artefact ar = current.getArtefact();

                        if (submergedArtefacts.contains(ar)) {
                            return true;
                        } else {
                            submergedArtefacts.add(ar);
                        }
                    }

                    // Un joueur se trouve dans une zone submergée
                    if (current.getEtat() == Etat.Submerger && current.containsJoueur()) {
                        return true;
                    }

                    // Si la zone est un héliport et qu'elle est entourée de zone submergée
                    if (current instanceof ZoneHeliport) {
                        if (ile.getNeighbourZones(current, true).isEmpty()) {
                            return true;
                        }
                    }
                }

            }
        }

        // Si un des joueurs est entouré de zones submergées
        for (Joueur j : joueurs) {
            if (ile.getNeighbourZones(j.getPosition(), true).isEmpty()) {
                return true;
            }
        }

        return false;
    }



    //

//      public boolean checkArtefact(){
//
//        for(int i = 0; i< joueurs.size(); i++){
//            for(Joueur j : joueurs){
//                if(j.hasArtefact() && j.getArtefacts(). ){
//
//                }
//            }
//        }

    }



