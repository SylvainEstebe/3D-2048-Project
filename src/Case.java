
import java.util.ArrayList;


/**
 * Classe qui s'occupe de la formation d'une case et de ses modifications dans une grille
 * @author Alexanne WORM
 * 
 */

public class Case implements Parametres{
    /**
 *
 * @author Alexanne
 */

    private int x, y, valeur;
    private Grille grille;

    public Case(int abs, int ord, int v, Grille g) {
        this.x = abs;
        this.y = ord;
        this.valeur = v;
        grille=g;
    }

    public void setGrille(Grille g) {
        this.grille = g;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    public int getValeur() {
        return this.valeur;
    }


    public boolean valeurEgale(Case c) {
        if (c != null) {
            return this.valeur == c.valeur;
        } else {
            return false;
        }
    }

    public Case getVoisinDirect(int direction) {
        switch (direction) {
            case GAUCHE -> {
                if(y>0){
                    return grille.getGrille().get(x).get(y-1);
                }
                return null;
            }
            case DROITE -> {
                if(y<TAILLE-1){
                    return grille.getGrille().get(x).get(y+1);
                }
                return null;
            }
            case HAUT -> {
                if(x>0){
                    return grille.getGrille().get(x-1).get(y);
                }
                return null;
            }
            case BAS -> {
                if(x<TAILLE-1){
                    return grille.getGrille().get(x+1).get(y);
                }
                return null;
            }
            default -> {
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Case(" + this.x + "," + this.y + "," + this.valeur + ")";
    }
}


