package modele;

import variables.Parametres;
import java.io.Serializable;
import java.util.ArrayList;
import static variables.Parametres.BAS;
import static variables.Parametres.DESCG;
import static variables.Parametres.DROITE;
import static variables.Parametres.GAUCHE;
import static variables.Parametres.GRILLEB;
import static variables.Parametres.GRILLEH;
import static variables.Parametres.HAUT;
import static variables.Parametres.MONTERG;
import static variables.Parametres.TAILLE;

/**
 * Classe qui s'occupe de la formation d'une case et de ses modifications dans
 * une grille
 *
 * @author Alexanne WORM
 *
 */
public class Case implements Parametres, Serializable {

    /**
     * Coordonnées et valeur de la case
     */
    private int x, y, valeur, valAv;
    /**
     * Grille à laquelle est associée la case
     */
    private Grille grille;
    /**
     * Type de la grille où la case est après un déplacement
     */
    private int grilleApDepl;
    /**
     * Booléen qui indique si la case a été fusionnée durant un déplacement
     */
    private boolean fusionnee; //indique si la case a déjà été fusionnée durant un coup donné, réinitialisée à chaque coup
    /**
     * Indique le nombre de déplacement qu'a réalisé la case lors d'un coup
     */
    private int nbDeplacements = 0;

    /**
     * Constructeur de case
     *
     * @param abs l'abscisse de la case
     * @param ord l'ordonnée de la case
     * @param v la valeur de la case
     * @param g la grille dans laquelle se trouve la case
     */
    public Case(int abs, int ord, int v, Grille g) {
        this.x = abs;
        this.y = ord;
        this.valeur = v;
        grille = g;
        this.fusionnee = false;
    }

    /**
     * Modifie le type de la grille où la case s'est déplacée après un coup
     *
     * @param grilleap le type de la nouvelle grille
     */
    public void setGrilleApDepl(int grilleap) {
        this.grilleApDepl = grilleap;
    }

    /**
     * Donne le type de la grille où est la case après un coup
     *
     * @return un entier qui représente la grille dans laquelle est la case après déplacement.
     */
    public int getGrilleApDepl() {
        return grilleApDepl;
    }

    /**
     * Permet de récupérer la valeur qu'avait précédemment la case
     * @return la valeur précédente
     */
    public int getValAv() {
        return valAv;
    }

    /**
     * Méthode qui permet de modifier la valeur précédente de la case
     * @param i la nouvelle valeur pour la valeur de la case précédente
     */
    public void setValAv(int i) {
        valAv = i;
    }

    /**
     * Méthode qui modifie la grille à laquelle est associée la case
     *
     * @param g la nouvelle grille
     */
    public void setGrille(Grille g) {
        this.grille = g;
    }

    /**
     * Méthode qui permet d'obtenir la grille dans laquelle se trouve la case
     *
     * @return la grille
     */
    public Grille getGrille() {
        return this.grille;
    }

    /**
     * Donne le nombre de déplacements effectué par la case lors d'un coup
     *
     * @return le nombre de déplacements
     */
    public int getNbDeplac() {
        return nbDeplacements;
    }

    /**
     * Modifie le nombre de déplacements effectué par la case lors d'un coup
     *
     * @param dep le nouveau nombre de déplacements
     */
    public void setNbDeplac(int dep) {
        nbDeplacements = dep;
    }

    /**
     * Méthode qui permet d'obtenir l'abscisse de la case
     *
     * @return x l'abscisse
     */
    public int getX() {
        return this.x;
    }

    /**
     * Méthode qui permet d'obtenir l'ordonnée de la case
     *
     * @return y l'ordonnée
     */
    public int getY() {
        return this.y;
    }

    /**
     * Méthode qui permet de modifier l'abscisse de la case
     *
     * @param x la nouvelle abscisse
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Méthode qui permet de modifier l'ordonnée de la case
     *
     * @param y la nouvelle ordonnée
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Méthode qui permet de modifier la valeur de la case
     *
     * @param valeur la nouvelle valeur
     */
    public void setValeur(int valeur) {
        this.valeur = valeur;

    }

    /**
     * Méthode qui permet de récupérer la valeur de la case
     *
     * @return la valeur de la case
     */
    public int getValeur() {
        return this.valeur;
    }

    /**
     * Méthode qui permet de modifier l'ordonnée de la case
     *
     * @param f le nouveau booléen fusionnee
     */
    public void setFusionnee(boolean f) {
        this.fusionnee = f;
    }

    /**
     * Méthode qui permet d'obtenir le booléen fusionnee
     *
     * @return fusionnee le booléen qui indique si la case a déjà été fusionnée
     */
    public boolean isFusionnee() {
        return this.fusionnee;
    }

    /**
     * Méthode qui permet de vérifier si deux cases ont la même valeur
     *
     * @param c la case qu'on compare avec celle-ci
     * @return un booléen indiquant si les cases ont la même valeur
     */
    public boolean valeurEgale(Case c) {
        if (c != null) {
            return this.valeur == c.valeur;
        } else {
            return false;
        }
    }

    /**
     * Méthode qui permet d'obtenir le voisin direct de la case dans une
     * certaine direction
     *
     * @param direction (gauche,droite,haut,bas monterg, descg)
     * @return la case voisine
     */
    public Case getVoisinDirect(int direction) {
        switch (direction) {
            case GAUCHE -> {
                if (y > 0) {
                    return grille.getGrille().get(x).get(y - 1);
                }
                return null;
            }
            case DROITE -> {
                if (y < TAILLE - 1) {
                    return grille.getGrille().get(x).get(y + 1);
                }
                return null;
            }
            case HAUT -> {
                if (x > 0) {
                    return grille.getGrille().get(x - 1).get(y);
                }
                return null;
            }
            case BAS -> {
                if (x < TAILLE - 1) {
                    return grille.getGrille().get(x + 1).get(y);
                }
                return null;
            }
            case MONTERG -> {
                ArrayList<Grille> grilles = grille.getJeu().getGrilles();
                int index = grilles.indexOf(this.grille); //index de la grille à laquelle appartient la case
                if (grille.getType() != GRILLEH) {
                    return grilles.get(index - 1).getGrille().get(x).get(y);
                }
                return null;
            }
            case DESCG -> {
                ArrayList<Grille> grilles = grille.getJeu().getGrilles();
                int index = grilles.indexOf(this.grille); //index de la grille à laquelle appartient la case
                if (grille.getType() != GRILLEB) {
                    return grilles.get(index + 1).getGrille().get(x).get(y);
                }
                return null;
            }

            default -> {
            }
        }
        return null;
    }

    /**
     * Méthode qui permet d'afficher la case
     *
     * 
     * @return Un string reprÃ©sentant la case dans la console
     */
    public String toString() {
        return "Case(" + this.x + "," + this.y + "," + this.valeur + ")";
    }

}
