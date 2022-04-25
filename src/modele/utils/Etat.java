package modele.utils;

/**
 * Une énumération permettant d'indiqué l'état d'une zone
 */
public enum Etat {
    Normal, Inonder, Submerger;

    public String toString() {
        return switch (this) {
            case Normal    -> "normal";
            case Inonder   -> "inondé";
            case Submerger -> "submergé";
        };
    }
}
