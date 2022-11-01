package jeuConsole;


import java.io.Serializable;
import java.util.ArrayList;


/**
 * Classe qui s'occupe de la formation d'une case et de ses modifications dans une grille
 * @author Alexanne WORM
 * 
 */

public class Case implements Parametres, Serializable {

    private int x, y, valeur; 
    private Grille grille;

    /**
     * Constructeur de case
     * @param abs l'abscisse de la case
     * @param ord l'ordonnée de la case
     * @param v la valeur de la case
     * @param g la grille dans laquelle se trouve la case
     */
    public Case(int abs, int ord, int v, Grille g) {
        this.x = abs;
        this.y = ord;
        this.valeur = v;
        grille=g;
    }

    /**
     * Méthode qui modifie la grille à laquelle est associée la case
     * @param g la nouvelle grille
     */
    public void setGrille(Grille g) {
        this.grille = g;
    }
    
    /**
    * Méthode qui permet d'obtenir la grille dans laquelle se trouve la case 
    * @return la grille 
    */
    public Grille getGrille() {
        return this.grille;
    }

    /**
     * Méthode qui permet d'obtenir l'abscisse de la case
     * @return x l'abscisse
     */
    public int getX() {
        return this.x;
    }

    /**
     * Méthode qui permet d'obtenir l'ordonnée de la case
     * @return y l'ordonnée
     */
    public int getY() {
        return this.y;
    }

    /**
     * Méthode qui permet de modifier l'abscisse de la case
     * @param x la nouvelle abscisse
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Méthode qui permet de modifier l'ordonnée de la case
     * @param y la nouvelle ordonnée
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Méthode qui permet de modifier la valeur de la case
     * @param valeur la nouvelle valeur
     */
    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    /**
     * Méthode qui permet de récupérer la valeur de la case
     * @return la valeur de la case
     */
    public int getValeur() {
        return this.valeur;
    }

    /**
     * Méthode qui permet de vérifier si deux cases ont la même valeur
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
     * Méthode qui permet d'obtenir le voisin direct de la case dans
     * une certaine direction
     * @param direction (gauche,droite,haut,bas monterg, descg)
     * @return la case voisine
     */
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
            case MONTERG -> {
                ArrayList<Grille> grilles = grille.getJeu().getgrilles();
                int index = grilles.indexOf(this.grille); //index de la grille à laquelle appartient la case
                if (grille.getType() != GRILLEH) { 
                    return grilles.get(index -1).getGrille().get(x).get(y);
                }
                return null;
            }   
            case DESCG -> {
                ArrayList<Grille> grilles = grille.getJeu().getgrilles();
                int index = grilles.indexOf(this.grille); //index de la grille à laquelle appartient la case
                if (grille.getType() != GRILLEB) { 
                    return grilles.get(index +1).getGrille().get(x).get(y);
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
     * @Override
     * @return Un string représentant la case dans la console
     */
    public String toString() {
        return "Case(" + this.x + "," + this.y + "," + this.valeur + ")";
    }
}


