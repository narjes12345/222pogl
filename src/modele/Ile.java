package modele;

import gui.ZoneCliquable;
import modele.utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Représente l'île. Une île contient toutes les zones ainsi que tous les joueurs
 * pour simplifier les interactions entre ceux deux modèles.
 */
public class Ile {
    private ZoneCliquable[][] zones;
    private ArrayList<Artefact> clefs;
    private ArrayList<Joueur> joueurs;
    private ZoneHeliport heliport;


    /**
     * Génère un array de coordonnées toutes différentes les une des autres.
     *
     * @param size Le nombre de valeurs à générer
     * @param max  La valeur max que peut prendre une valeur
     * @return Un array d'entiers
     */
    public static int[] getCoordonnees(int size, int max) {
        int[] res = new int[size];
        Random rand = new Random();

        for (int i = 0; i < size; i++) {
            res[i] = rand.nextInt(max);

            for (int j = 0; j < i; j++) {
                while (res[i] == res[j]) {
                    res[i] = rand.nextInt(max);
                }
            }
        }

        return res;
    }


    /**
     * Vérifie si des coordonnées ne se trouvent pas déjà dans une collection.
     *
     * @param data  La collection à parcourir
     * @param x     La coordonnée en abscisses
     * @param y     La coordonnée en ordonnée
     *
     * @return {@code true} si la coordonnée se trouve déjà dans la collection,
     *          {@code false} sinon
     */
    private static boolean containsCoordonnees(int[][] data, int x, int y) {
        for (int[] j : data) {
            if (j[0] == x && j[1] == y) {
                return true;
            }
        }

        return false;
    }

    /**
     * Génère des coordonnées aléatoires, toutes différentes les unes des autres, et différentes de celles
     * contenue dans un array donné
     *
     * @param size              Le nombre de coordonnées à générer
     * @param max               La valeur max d'une coordonnée
     * @param forbidenValues    Un array contenant les coordonnées déjà réservées
     *
     * @return Un array à deux dimensions d'entier
     */
    public int[][] getCoordonnes(int size, int max, int[][] forbidenValues) {
        int[] x = new int[size];
        int[] y = new int[size];
        Random rand = new Random();

        for (int i = 0; i < size; i++) {
            x[i] = rand.nextInt(max);
            y[i] = rand.nextInt(max);

            if (containsCoordonnees(forbidenValues, x[i], y[i])) {
                i--;
                continue;
            }


            for (int j = 0; j < i; j++) {
                while (x[i] == x[j]) {
                    x[i] = rand.nextInt(max);
                }
                while (y[i] == y[j]) {
                    y[i] = rand.nextInt(max);
                }
            }
        }

        return new int[][]{x, y};
    }


    /**
     * Génère une liste de clés
     *
     * @return Une ArrayList
     */
    public static ArrayList<Artefact> genereClefs() {
        Artefact[] ar = new Artefact[]{Artefact.Air, Artefact.Eau, Artefact.Terre, Artefact.Feu};
        ArrayList<Artefact> res = new ArrayList<>(Arrays.asList(ar));
        res.addAll(Arrays.asList(ar));
        res.addAll(Arrays.asList(ar));
        res.addAll(Arrays.asList(ar));
        Collections.shuffle(res);

        return res;
    }


    public Ile(int taille, int tailleZone) {
        zones = new ZoneCliquable[taille][taille];
        clefs = genereClefs();
        joueurs = new ArrayList<>();

        Random rand = new Random();

        // On commence par créer les cases "inutilisables" du plateau
        int[][] useless = new int[][] { {0, 0}, {0, 1}, {0, taille-1}, {0, taille-2},
                {1, 0}, {1, taille-1},
                {4, 0}, {4, taille-1},
                {5, 0}, {5, 1}, {5, taille-1}, {5, taille-2}
        };

        for (int[] i : useless) {
            zones[i[0]][i[1]] = new ZoneVide(tailleZone, tailleZone);
            //zones[i[0]][i[1]].setEtat(Etat.Submerger);
        }


        // On crée ensuite les 8 zones spéciales et l'héliport
        int[][] specCoo = getCoordonnes(9, taille-1, useless);
        heliport = new ZoneHeliport(tailleZone, tailleZone, this);
        zones[specCoo[0][0]][specCoo[1][0]] = heliport;
        heliport.setEtat(Etat.Normal);
        for (int i = 1; i < specCoo[0].length; i++) {
            Zone next = new Zone(tailleZone, tailleZone, this);
            next.addArtefact(Artefact.fromInteger(rand.nextInt(4)));
            zones[specCoo[0][i]][specCoo[1][i]] = next;
            next.setEtat(Etat.Normal);
        }

        // Puis on crée les autres zones
        for (int i = 0; i < zones.length; i++) {
            for (int j = 0; j < zones[i].length; j++) {
                if (zones[i][j] == null) {
                    Zone next = new Zone(tailleZone, tailleZone, this);
                    next.setEtat(Etat.Normal);
                    zones[i][j] = next;
                }
            }
        }
        setCoordonnees();
        }


    /// onlymovable: true -> zone déplacement, false -> zone assechables

    /**
     * Renvoie les zones voisines d'une zone donnée
     *
     * @param c  Une zone
     * @param needMovable  si {@code true} on ne renvoie que les zones dans lesquelles le joueur pourras se déplacer
     *                     si {@code false} on ne renvoie que les zones qui peuvent être asséchée
     *
     * @return Une ArrayList
     */
    public ArrayList<ZoneCliquable> getNeighbourZones(ZoneCliquable c, boolean needMovable) {
        ArrayList<ZoneCliquable> res = new ArrayList<>();

        for (int i = 0; i < zones.length; i++) {
            for (int j = 0; j < zones[i].length; j++) {
                if (zones[i][j] == c) {
                    if (i+1 < zones.length && !(zones[i+1][j] instanceof ZoneVide))
                        res.add(zones[i+1][j]);

                    if (i-1 >= 0 && !(zones[i-1][j] instanceof ZoneVide))
                        res.add(zones[i-1][j]);

                    if (j+1 < zones[i].length && !(zones[i][j+1] instanceof ZoneVide))
                        res.add(zones[i][j+1]);

                    if (j-1 >= 0 && !(zones[i][j-1] instanceof ZoneVide))
                        res.add(zones[i][j-1]);
                }
            }
        }

        if (needMovable) {
            res.removeIf(z -> z instanceof Zone && ((Zone) z).getEtat() == Etat.Submerger);
        } else {
            res.removeIf(z -> z instanceof Zone && ((Zone)z).getEtat() != Etat.Inonder );
            if(c.getEtat()== Etat.Inonder){  res.add(c);}

        }

        return res;
    }


    /**
     * Ajoute un joueur à l'île
     *
     * @param j Un joueur à ajouter
     */
    public void addJoueur(Joueur j) {
        if (!joueurs.contains(j)) {
            j.ajouteCle(clefs.get(0));
            j.ajouteCle(clefs.get(1));

            //j.ajouteCle(Artefact.Eau);
            //j.ajouteCle(Artefact.Eau);
            //j.ajouteCle(Artefact.Eau);
            //j.ajouteCle(Artefact.Eau);

            clefs.remove(0);
            clefs.remove(1);

            joueurs.add(j);
        }
    }


    public ZoneCliquable[][] getZones() {
        return zones;
    }

    public int getWidth() {
        return zones.length;
    }

    public int getHeight() {
        return zones[0].length;
    }


    public ZoneCliquable getSpawnZone() {
        return heliport;
    }

    public ArrayList<Joueur> getJoueurs() {
        return joueurs;
    }

    /**
     * Met à jour l'affichage des zones
     */
    public void update() {
        for (ZoneCliquable[] z : zones) {
            for (ZoneCliquable zz : z) {
                zz.draw();
            }
        }
    }


    /**
     * Submerge trois zones aléatoirement
     */
    public void submergeRandom() {
        // On stocke les zones à inonder dans une ArrayList.
        // A chaque fois qu'on inonde une zone, on la supprime de la liste, pour éviter
        // de l'inonder une deuxième fois
        ArrayList<ZoneCliquable> zoneToSubmerge = new ArrayList<>();
        Random rand = new Random();

        // On commence par chercher les cases normales
        for (ZoneCliquable[] z : zones) {
            for (ZoneCliquable zz : z) {
                if (zz instanceof Zone && ((Zone) zz).getEtat() == Etat.Normal) {
                    zoneToSubmerge.add(zz);
                }
            }
        }

        // Premier cas: on a assez de cases normales
        if (zoneToSubmerge.size() > 3) {
            for (int i = 0; i < 3; i++) {
                int id = rand.nextInt(zoneToSubmerge.size());
                ((Zone)zoneToSubmerge.get(id)).inonde();

                zoneToSubmerge.remove(id);
            }
        }

        // Deuxième cas: pile 3 zones normales
        else if (zoneToSubmerge.size() == 3) {
            for (ZoneCliquable z : zoneToSubmerge) {
                ((Zone)z).inonde();
            }
        }

        // Dernier cas: 0, 1, ou 2 zone normales seulement
        else {
            // On récupère le nombre de cases normales
            int nbCaseNormales = zoneToSubmerge.size();

            // On récupère ensuite les cases inondées
            for (ZoneCliquable[] z : zones) {
                for (ZoneCliquable zz : z) {
                    if (zz instanceof Zone && ((Zone)zz).getEtat() == Etat.Inonder) {
                        zoneToSubmerge.add(zz);
                    }
                }
            }

            // Si la taille du tableau vaut 0, alors toutes les cases sont déjà
            // submergées
            if (zoneToSubmerge.size() == 0)
                return;

            // Comme on priorise les cases normales, et qu'on sait que toute nos cases
            // normales sont au début de la liste, on commence par inonder les premières cases
            for (int i = 0; i < nbCaseNormales; i++) {
                ((Zone)zoneToSubmerge.get(i)).inonde();
                zoneToSubmerge.remove(i);
            }

            // Il ne nous reste plus qu'à choisir des zones au hasard parmis les zones
            // restantes, qui sont toutes déjà inondées
            for (int i = nbCaseNormales; i < 3 && zoneToSubmerge.size() > 0; i++) {
                int id = rand.nextInt(zoneToSubmerge.size());

                ((Zone)zoneToSubmerge.get(id)).inonde();
                zoneToSubmerge.remove(id);
            }
        }
    }
    public void setCoordonnees(){
        for(int i = 0; i<zones.length; i++){
            for(int j = 0; j<zones.length; j++){
                zones[i][j].setCo(i,j);
            }
        }
    }

    public void removeAllArtefacts(Artefact artefact) {
        for (ZoneCliquable[] z : zones) {
            for (ZoneCliquable zz : z) {
                if (zz instanceof Zone && ((Zone)zz).getArtefact() == artefact) {
                    ((Zone)zz).removeArtefact();
                }
            }
        }
    }
//    public ZoneCliquable getPosition(Zone c){
//        for(ZoneCliquable [] z : zones) {
//            for(ZoneCliquable y : z){
//                if(c.getCo() == y.getCo()){
//                    return ( y);
//                }
//            }
        }


