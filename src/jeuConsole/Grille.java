package jeuConsole;


import jeuConsole.Case;
import java.util.ArrayList;
import java.util.Random;

/**
 * Classe qui réalise toutes les fonctionnalités sur une grille précise du jeu
 *
 * @author Alexanne
 */
public class Grille implements Parametres {

    private ArrayList<ArrayList<Case>> grille;
    private int scoreg;
    private int type; //indique si c'est une grille du haut, du milieu ou du bas
    private Jeu jeu; //jeu auquel appartient la grille
    
    /**
     * Constructeur qui initialise une grille vide
     */
    public Grille(Jeu jeu, int t) {
        grille = new ArrayList<ArrayList<Case>>();
        for (int i = 0; i < TAILLE; i++) {
            grille.add(new ArrayList<Case>());
            for (int j = 0; j < TAILLE; j++) {
                grille.get(i).add(new Case(i, j, 0, this));
            }
        }
        this.jeu = jeu;
        this.type = t;
    }

    @Override
    /**
     * Méthode qui affiche la grille dans le terminal.
     *
     * @return La grille sous forme de String
     */
    public String toString() {
        String result = "";
        for (int i = 0; i < TAILLE; i++) {
            result += "[";
            for (int j = 0; j < TAILLE; j++) {
                if (j != TAILLE - 1) {
                    result += grille.get(i).get(j).getValeur() + ", ";
                } else {
                    result += grille.get(i).get(j).getValeur() + "]";
                }
            }
            result += "\n";
        }
        return result;
    }

    /**
     * Méthode qui retourne la grille sous forme de tableau
     *
     * @return la grille sous forme de tableau à deux dimensions
     */
    public ArrayList<ArrayList<Case>> getGrille() {
        return grille;
    }

    /**
     * Méthode qui retourne le score obtenu lors du jeu pour une grille
     *
     * @return le score sous forme d'entier
     */
    public int getScoreG() {
        return scoreg;
    }

    /**
     * Méthode qui retourne le type de la grille (haut/milieu/bas)
     *
     * @return le type sous forme d'entier
     */
    public int getType() {
        return type;
    }
    
    /**
     * Méthode qui retourne le jeu auquel appartient la grille
     *
     * @return le Jeu de la grille
     */
    public Jeu getJeu(){
        return this.jeu;
    }

    /**
     * Méthode qui permet de modifier le type de la grille
     *
     * @param type sous forme d'entier
     */
    public void setType(int type) {
        this.type = type;
    }
    

    /**
     * Méthode qui vérifie si une partie est finie (éléments impossibles à
     * bouger).
     *
     * @return un booléen qui indique si la partie est finie ou non
     */
    public boolean partieFinieG() {
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                if (grille.get(i).get(j).getValeur() == 0) {
                    return false;
                }
                if (grille.get(i).get(j).getVoisinDirect(HAUT) != null) {
                    if (grille.get(i).get(j).valeurEgale(grille.get(i).get(j).getVoisinDirect(HAUT))) {
                        return false;
                    }
                }
                if (grille.get(i).get(j).getVoisinDirect(BAS) != null) {
                    if (grille.get(i).get(j).valeurEgale(grille.get(i).get(j).getVoisinDirect(BAS))) {
                        return false;
                    }
                }
                if (grille.get(i).get(j).getVoisinDirect(DROITE) != null) {
                    if (grille.get(i).get(j).valeurEgale(grille.get(i).get(j).getVoisinDirect(DROITE))) {
                        return false;
                    }
                }
                if (grille.get(i).get(j).getVoisinDirect(GAUCHE) != null) {
                    if (grille.get(i).get(j).valeurEgale(grille.get(i).get(j).getVoisinDirect(GAUCHE))) {
                        return false;
                    }
                }
                //on fait le test dans la classe Grille pour MONTERG et DESCG car il se peut qu'une seule grille soit terminée 
                //mais que des fusions en montant et descendant soient toujours possibles
                if (grille.get(i).get(j).getVoisinDirect(MONTERG) != null) { 
                    if (grille.get(i).get(j).valeurEgale(grille.get(i).get(j).getVoisinDirect(MONTERG))) {
                        return false;
                    }
                }
                if (grille.get(i).get(j).getVoisinDirect(DESCG) != null) {
                    if (grille.get(i).get(j).valeurEgale(grille.get(i).get(j).getVoisinDirect(DESCG))) {
                        return false;
                    }
                }
                
            }
        }
        return true;
    }

    /**
     * Fonction qui fusionne deux cases d'une grille et augmente le score de la
     * grille
     *
     * @param c la case qu'on fusionne
     */
    public void fusion(Case c) {
        c.setValeur(c.getValeur() * 2);
        this.scoreg = scoreg + c.getValeur();
    }

    /**
     * Méthode qui permet de déplacer les cases dans une direction
     *
     * @param direction un entier (gauche, droite, haut, bas)
     * @return un booléen qui indique si on a bougé des cases
     */
    public boolean deplacerCases(int direction) {
        boolean deplacement = false;

        if (direction == HAUT || direction == BAS) {
            ArrayList<ArrayList<Case>> tabHB = new ArrayList<>();
            tabHB.add(new ArrayList<>());
            tabHB.add(new ArrayList<>());
            tabHB.add(new ArrayList<>());
            //On crée des tableaux de haut en bas pour mieux manipuler les valeurs
            for (int i = 0; i < TAILLE; i++) {
                for (int j = 0; j < TAILLE; j++) {
                    if (grille.get(i).get(j).getY() == 0) {

                        tabHB.get(0).add(grille.get(i).get(j));
                    } else if (grille.get(i).get(j).getY() == 1) {

                        tabHB.get(1).add(grille.get(i).get(j));
                    } else {

                        tabHB.get(2).add(grille.get(i).get(j));
                    }
                }
            }
            //Si la direction est haut
            if (direction == HAUT) {
                for (int i = 0; i < TAILLE; i++) {
                    for (int k = 0; k < TAILLE; k++) {
                        boolean d = deplacementUneCase(HAUT, tabHB.get(i), k);
                        if (d) deplacement = d;
                    }
                }
            }
            if (direction == BAS) {
                for (int i = 0; i < TAILLE; i++) {
                    for (int k = TAILLE - 1; k >= 0; k--) {
                        boolean d = deplacementUneCase(BAS, tabHB.get(i), k);
                        if (d) deplacement = d;
                    }
                }
            }
        }
        if (direction == DROITE || direction == GAUCHE) {
            if (direction == DROITE) {
                for (int i = 0; i < TAILLE; i++) {
                    for (int k = TAILLE - 1; k >= 0; k--) {
                        boolean d = deplacementUneCase(DROITE, grille.get(i), k);
                        if (d) deplacement = d;
                    }
                }
            }
            if (direction == GAUCHE) {
                for (int i = 0; i < TAILLE; i++) {
                    for (int k = 0; k < TAILLE; k++) {
                        boolean d = deplacementUneCase(GAUCHE, grille.get(i), k);
                        if (d) deplacement = d;
                    }
                }
            }
        }
        return deplacement;
    }

    /**
     * Méthode qui permet de déplacer une case de la grille
     *
     * @param direc2 la direction vers laquelle on déplace la grille
     * @param tabHB la ligne/colonne où est située la case
     * @param k la colonne/ligne où est située la case
     */
    private boolean deplacementUneCase(int direc2, ArrayList<Case> tabHB, int k) {
        boolean deplacement = false;
        int voisin;
        if (direc2 == GAUCHE || direc2 == HAUT) {
            voisin = k - 1;
        } else {
            voisin = k + 1;
        }
        
        // Tant que mon voisin = 0, je me déplace avant de chercher à fusionner
        while (voisin >= 0 && voisin < TAILLE && tabHB.get(voisin).getValeur() == 0) {
            tabHB.get(voisin).setValeur(tabHB.get(k).getValeur());
            tabHB.get(k).setValeur(0);
            deplacement = true;
            
            // Je mets à jour mon index et celui de mon voisin
            if (direc2 == GAUCHE || direc2 == HAUT) {
                voisin--;
                k--;
            } else {
                voisin++;
                k++;
            }
        }
        
        if (voisin >= 0 && voisin < TAILLE) {
            //Si mon voisin a la même valeur que moi, je fusionne
            if (tabHB.get(voisin).getValeur()
                    == tabHB.get(k).getValeur()) {
                fusion(tabHB.get(voisin));
                tabHB.get(k).setValeur(0);
                deplacement = true;
            }
        }

        return deplacement;
    }

    /**
     * Méthode qui place une nouvelle case dans la grille s'il reste des cases
     * vides
     *
     * @return Un booléen qui indique si on a bougé la case ou non
     */
    public boolean nouvelleCase() {
        //On détermine si on prend 2 ou 4 pour la case
        int nombre_case = 0;
        Random rand = new Random();
        int nombreAlea = rand.nextInt(9 - 0 + 1);
        if (nombreAlea < 6.6) {
            nombre_case = 2;
        } else {
            nombre_case = 4;
        }
        if (caseLibreG() != null) {
            ArrayList<Case> cases_vides = caseLibreG();
            Random random_method = new Random();
            int index = random_method.nextInt(cases_vides.size());
            cases_vides.get(index).setValeur(nombre_case);
            return true;
        }
        return false;
    }

    /**
     * Méthode qui permet de dire s'il y a une case libre dans la grille
     *
     * @return Un tableau de cases libres
     */
    public ArrayList<Case> caseLibreG() {
        ArrayList<Case> cases_vides = new ArrayList<Case>();
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                if (grille.get(i).get(j).getValeur() == 0) {
                    cases_vides.add(grille.get(i).get(j));
                }
            }
        }
        if (cases_vides.size() != 0) {
            return cases_vides;
        }
        return null;
    }
    
    /**
     * 
     * @return la valeur maximale de la grille
     */
    public int getValeurMax (){
        int max = 0;

        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                if (grille.get(i).get(j).getValeur() > max) {
                   max = grille.get(i).get(j).getValeur();
                }
            }
        }
        return max;
    }

    /**
     * Méthode qui affiche la victoire et le score de la case
     */
    public void victory() {
        System.out.println("Vous avez gagné! Votre score est de :" + scoreg);
        System.exit(0);
    }

    /**
     * méthode qui affiche la défaite
     */
    public void gameOver() {
        System.out.println("Vous avez perdu, retentez votre chance!");
        System.exit(1);
    }
}
