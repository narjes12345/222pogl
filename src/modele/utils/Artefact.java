package modele.utils;

/**
 * Une énumération permettant d'indiquer le type d'un artefact ou d'une clé
 */
public enum Artefact {
    Eau, Feu, Air, Terre;

    /**
     * Convertie une valeur de type {@link Artefact} en {@link String}
     *
     * @return Une chaine de charactères
     */
    @Override
    public String toString() {
        return switch (this) {
            case Eau   -> "eau";
            case Feu   -> "feu";
            case Air   -> "air";
            case Terre -> "terre";
        };
    }

    /**
     * Crée une valeur de type {@link Artefact} à partir d'un entier
     * Cette fonction est utilisée pour simplifier la distribution d'artefacts
     *
     * @param i  Un entier dans l'intervalle {@code [0, 3}
     *
     * @return Une valeur de type {@link Artefact}
     */
    public static Artefact fromInteger(int i) {
        return switch (i) {
            case 0 -> Artefact.Eau;
            case 1 -> Artefact.Feu;
            case 2 -> Artefact.Air;
            case 3 -> Artefact.Terre;
            default -> throw new IllegalArgumentException("Le nombre doit être compris entre 0 et 3 (inclus)");
        };
    }
}
