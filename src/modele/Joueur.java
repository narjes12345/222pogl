package modele;

import gui.ZoneCliquable;
import modele.utils.*;

import java.util.ArrayList;

/**
 * Un joueur. Contiens les clés et les artefacts que possède un joueur.
 */
public class Joueur {
    private Ile ile;
    private Zone position;
    private ArrayList<Artefact> clefs;
    private ArrayList<Artefact> artefacts;

    public final String nom;

    private static int nbInstance = 0;
    public final int idInstance;
    public static final int TailleJoueur = 30;


    public Joueur(String nom, Zone spawnPos, Ile ile) {
        this.nom = nom;
        this.ile = ile;

        position = spawnPos;
        position.addJoueur(this);

        clefs = new ArrayList<>();
        artefacts = new ArrayList<>();

        nbInstance++;
        idInstance = nbInstance;
    }

    public Zone getPosition() {
        return (Zone)position;

    }



    public void ajouteCle(Artefact clef) {
        clefs.add(clef);
    }

    public void retireCle(Artefact clef) {
        clefs.remove(clef);
    }

    public ArrayList<Artefact> getCles() {
        return clefs;
    }


    public boolean artefactCle (Artefact artefact) {
        int nbCle = 0;
        for (Artefact c : clefs) {
            if (c == artefact) {
                nbCle++;
            }
        }
        return nbCle >= 4;
    }
    public void addArtefact(Artefact a){
        if(artefactCle(a)){
            artefacts.add(a);

        }
    }

    public void retireArtefact(Artefact artefact) {
        artefacts.remove(artefact);
    }

    public ArrayList<Artefact> getArtefacts() {
        return artefacts;
    }

    public boolean hasArtefact(){
        return (artefacts.size()>0);
    }
     public void ajouteArtefact(Artefact a){
        artefacts.add(a);
     }
    public String getInfo() {
        int eau = 0, feu = 0, air = 0, terre = 0;
        for (Artefact a : artefacts) {
            switch (a) {
                case Air -> air++;
                case Feu -> feu++;
                case Eau -> eau++;
                case Terre -> terre++;
            }
        }

        int ceau = 0, cfeu = 0, cair = 0, cterre = 0;
        for (Artefact a : clefs) {
            switch (a) {
                case Air -> cair++;
                case Feu -> cfeu++;
                case Eau -> ceau++;
                case Terre -> cterre++;
            }
        }

        StringBuilder sb = new StringBuilder("\t");
        sb.append(nom);
        sb.append("\n\nArtefact Eau: ");
        sb.append(eau);
        sb.append("\n\nArtefact Air: ");
        sb.append(air);
        sb.append("\n\nArtefact Feu: ");
        sb.append(feu);
        sb.append("\n\nArtefact Terre: ");
        sb.append(terre);

        sb.append("\n\n\nClé Eau: ");
        sb.append(ceau);
        sb.append("\n\nClé Air: ");
        sb.append(cair);
        sb.append("\n\nClé Feu: ");
        sb.append(cfeu);
        sb.append("\n\nClé Terre: ");
        sb.append(cterre);

        return sb.toString();
    }
    public ArrayList<ZoneCliquable> zonesAssechables() {
        ArrayList<ZoneCliquable> zonesAssechables = new ArrayList<>();

        if (this.getPosition().getEtat()==Etat.Inonder) {
            zonesAssechables.add(this.getPosition());
        }

       for(ZoneCliquable c : ile.getNeighbourZones(this.getPosition(), false)){
            if (c.getEtat()==Etat.Inonder) {
                zonesAssechables.add(c);
            }
        }
        return zonesAssechables;
    }

    public Ile getIle() {
        return ile;
    }

    public String getNom() {
        return nom;
    }

    public ArrayList<Artefact> getClefs() {
        return clefs;
    }

    public void move(Zone position) {
        this.position = position;
    }
}
