package modele.utils;

import java.awt.Color;

/**
 * Une classe contentant des attributs statiques pour définir les couleurs
 * des différents éléments de notre île.
 */
public class Couleurs {

    /// Les couleurs pour les different types de cases
    public static final Color CASE_VIDE = Color.WHITE;
    public static final Color CASE_HELIPORT = Color.MAGENTA;
    public static final Color CASE_NORMALE = Color.GREEN;
    public static final Color CASE_INONDER = Color.CYAN;
    public static final Color CASE_SUBMERGER = Color.BLUE;
    public static final Color CASE_HIGHLIGHT = Color.RED;


    /// Les couleurs pour les different types d'artefacts/clefs
    public static final Color ARTEFACT_FEU = Color.RED;
    public static final Color ARTEFACT_EAU = Color.CYAN;
    public static final Color ARTEFACT_AIR = Color.GRAY;
    public static final Color ARTEFACT_TERRE = Color.ORANGE;


    /**
     * Convertie un {@link Artefact} en {@link Color}
     *
     * @param ar  La valeur à convertir
     *
     * @return La couleur correspondant à la valeur donnée
     */
    public static Color fromArtefact(Artefact ar) {
        return switch (ar) {
            case Air   -> ARTEFACT_AIR;
            case Eau   -> ARTEFACT_EAU;
            case Feu   -> ARTEFACT_FEU;
            case Terre -> ARTEFACT_TERRE;
        };
    }

    /**
     * Convertie un {@link Etat} en {@link Color}
     *
     * @param e  La valeur à convertir
     *
     * @return La couleur correspondant à la valeur donnée
     */
    public static Color fromEtat(Etat e) {
        return switch (e) {
            case Normal -> CASE_NORMALE;
            case Inonder -> CASE_INONDER;
            case Submerger -> CASE_SUBMERGER;
        };
    }
}
