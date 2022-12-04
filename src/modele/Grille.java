package modele;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import variables.Parametres;
import static variables.Parametres.BAS;
import static variables.Parametres.DROITE;
import static variables.Parametres.GAUCHE;
import static variables.Parametres.HAUT;
import static variables.Parametres.TAILLE;

/**
 * Classe qui réalise toutes les fonctionnalités sur une grille précise du jeu
 *
 * @author Alexanne
 */
public class Grille implements Parametres, Serializable{

    /**
     * Tableau de cases qui représente la grille
     */
    private  ArrayList<ArrayList<Case>> grille;
    /**
     * Score de la grille
     */
    private int scoreg;
    /**
     * Type de la grille (haut, milieu, bas)
     */
    private int type; //indique si c'est une grille du haut, du milieu ou du bas
    /**
     * Jeu auquel est associée la grille
     */
    private Jeu jeu; //jeu auquel appartient la grille
    
    /**
     * Constructeur qui initialise une grille vide
     * @param jeu jeu auquel appartient la grille
     * @param t  type de grille(haut/milieu/bas)
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
    
    /**
     * Crée une copie de la Grille
     * 
     * @param g Grille à copier 
     */
    private Grille(Grille g, Jeu jeu) {
        this.grille = new ArrayList<ArrayList<Case>>();
        for (int i = 0; i < TAILLE; i++) {
            this.grille.add(new ArrayList<Case>());
            for (int j = 0; j < TAILLE; j++) {
                this.grille.get(i).add(new Case(i, j, g.grille.get(i).get(j).getValeur(), this));
            }
        }
        this.scoreg = g.scoreg;
        this.type = g.type;
        this.jeu = jeu;
    }

    
    /**
     * Méthode qui affiche la grille dans le terminal.
     *@Override
     * @return La grille sous forme de String
     */
    public String toString() {
        String result = "";
        for (int i = 0; i < TAILLE; i++) {
            result += "[";
            for (int j = 0; j < TAILLE; j++) {
                if (j != TAILLE - 1) {
                    result += grille.get(i).get(j).getValeur() + "| ";
                } else {
                    result += grille.get(i).get(j).getValeur() + "]";
                }
            }
            result += "\n";
        }
        return result;
    }

    /**
     * Méthode qui affiche la grille sous forme de tableau
     * @return la grille sous forme de tableau à deux dimensions
     */
    public ArrayList<ArrayList<Case>> getGrille() {
        return grille;
    }

    /**
     * Méthode qui retourne le score obtenu lors du jeu pour une grille
     * @return le score sous forme d'entier
     */
    public int getScoreG() {
        return scoreg;
    }

    /**
     * Méthode qui retourne le type de la grille (haut/milieu/bas)
     * @return le type sous forme d'entier
     */
    public int getType() {
        return type;
    }
    
    /**
     * Méthode qui retourne le jeu auquel appartient la grille
     * @return le Jeu de la grille
     */
    public Jeu getJeu(){
        return this.jeu;
    }

    /**
     * Méthode qui permet de modifier le type de la grille
     * @param type sous forme d'entier
     */
    public void setType(int type) {
        this.type = type;
    }
    

    /**
     * Méthode qui vérifie s'il y a des déplacements (Gauche/Droite/Bas/Haut) 
     * possibles sur une grille 
     * @return un booléen qui indique s'il y a encore des déplacements possibles
     * ou non
     */
    public boolean deplacementFiniG() {
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
            }
        }
        return true;
    }

    /**
     * Fonction qui fusionne deux cases d'une grille et augmente le score de la
     * grille
     * @param c la case qu'on fusionne
     */
    public boolean fusion(Case c, Case voisin) {
        boolean fusion = false;
            voisin.setValeur(voisin.getValeur() * 2);
            voisin.setFusionnee(true);
            this.scoreg = scoreg + voisin.getValeur();
            c.setValeur(0);
            
            fusion = true;
        
        return fusion;
    }
    

    /**
     * Méthode qui permet de déplacer toutes les cases dans une direction
     * (sur la même grille)
     * @param direction un entier (gauche, droite, haut, bas)
     * @return un booléen qui indique si on a déplacé les cases ou non 
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
     * @return qui indique si on a déplacé une case ou non 
     */
    private boolean deplacementUneCase(int direc2, ArrayList<Case> tabHB, int k) {
        
        tabHB.get(k).setValAv(tabHB.get(k).getValeur());
        boolean deplacement = false;
        Case caseBouge=tabHB.get(k);
        
        if (tabHB.get(k).getValeur() > 0) { // Déplacement uniquement s'il s'agit d'une vraie case (avec une valeur)
           int voisin;
           if (direc2 == GAUCHE || direc2 == HAUT) {
               voisin = k - 1;
           } else {
               voisin = k + 1;
           }
           
           // Tant que mon voisin = 0, je me déplace avant de chercher à fusionner
            while (voisin >= 0 && voisin < TAILLE && tabHB.get(voisin).getValeur() == 0 ) {
               
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
               if (tabHB.get(voisin).getValeur() == tabHB.get(k).getValeur()) {
                   boolean b = fusion(tabHB.get(k), tabHB.get(voisin));
                   if(direc2==GAUCHE || direc2==HAUT){
                       k--;
                   }
                   else {
                       k++;
                   }
                   if (b) deplacement = true;
                   } 
                }
                   //Element qui sert au thread deplacement des cases graphiques
            if((direc2==HAUT || direc2==BAS)  ){

                caseBouge.setNbDeplac(k-caseBouge.getX());
            }
            else if((direc2==DROITE || direc2==GAUCHE)){
                caseBouge.setNbDeplac(k-caseBouge.getY()); 
                


            }
           
            return deplacement;
        }
        caseBouge.setNbDeplac(0);

        return deplacement;
    }
    
    /**
     * Méthode qui génère une nouvelle case dans la grille s'il reste des cases
     * vides
     *
     * @return Un booléen qui indique si on a placé une nouvelle case ou pas
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
     * @return une liste de cases libres
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
     * Méthode qui retourne la valeur maximale dans la grille
     * @return la valeur maximale de la grille
     */
    public int getValeurMax () {
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
     * Méthode qui réinitialise l'attribut fusionnee des cases à chaque coup
     */
    public void resetFusion () {
        for (int i = 0 ; i < TAILLE ; i++) {
            for (int j = 0 ; j < TAILLE ; j++) {
                this.grille.get(i).get(j).setFusionnee(false);
            }   
        }
    }

    /**
     * Méthode qui clone une grille
     * @param j le jeu associé à la grille
     * @return La grille copie
     */
    public Grille clone(Jeu j) {
        return new Grille(this, j);
    }
    
    
}

