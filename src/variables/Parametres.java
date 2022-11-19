package variables;





/**
 * Interface qui possède toutes les variables du jeu (taille de la grille...)
 * @author Alexanne WORM
 */
public interface Parametres {
    static final int HAUT = 1;
    static final int DROITE = 2;
    static final int BAS = -1;
    static final int GAUCHE = -2;
    static final int TAILLE = 3;
    static final int OBJECTIF = 2048;
    static final int MONTERG=-3;
    static final int DESCG=-4;
    static final int GRILLEH=-5; //grille du haut
    static final int GRILLEM=-6; //grille du milieu
    static final int GRILLEB=-7; //grille du bas
    
}
