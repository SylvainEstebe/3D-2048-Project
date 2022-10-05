
import java.util.ArrayList;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Manon
 */
public class Jeu implements Parametres {
    private Grille gh; //grille du haut
    private Grille gm;  //grille du milieu
    private Grille gb;  //grille du bas
    private int scoreFinal = 0;
    private int scoreDeplacement ; //score gagné pour 1 déplacement

    //getters et setters
    public Grille getGrilleh () {
        return this.gh;
    }
    public Grille getGrillem () {
        return this.gm;
    }
    public Grille getGrilleb () {
        return this.gb;
    }

    public int getScoreDeplacement () {
        return this.scoreDeplacement;
    }

    public int getScoreFinal () {
        return this.scoreFinal;
    }

    public void setScoreFinal (int score)  {
        this.scoreFinal = score;
    }

    public void setScoreDeplacement( int sc) {
        this.scoreDeplacement = sc;
    }
    
    @Override
    public String toString() {
        String result = "";
        result = gh.toString() + gm.toString() + gb.toString() ;
        return result;
    }

    public void majScore () { //méthode qui incrémente les points gagnés au scoreFinal à chaque déplacement
       scoreFinal = gh.getScoreG() + gm.getScoreG() + gb.getScoreG();  
    }

    //affiche une partie perdue et ferme le programme
    public void gameOver ()  {
        System.out.println("Vous avez perdu, retentez votre chance!");
        gh.toString();
        gm.toString();
        gb.toString();
        System.exit(1);
    }
    //affiche une partie gagnée et ferme le programme
    public void victory ()  { 
        System.out.println("Vous avez gagné! Votre score est de :"+scoreFinal);
        gh.toString();
        gm.toString();
        gb.toString();
        System.exit(0);
    } 

    
    // appui sur Q ou E : déplacement des cases entre les différentes grilles
    public boolean deplacerG(int direction){
        boolean deplacement=false;
        
        ArrayList<Grille> grilles = new ArrayList<>();
        grilles.add(this.gh);
        grilles.add(this.gm);
        grilles.add(this.gb);

        ArrayList<ArrayList<Case>> tabMD=new ArrayList<>();
        tabMD.add(new ArrayList<>()); //x = 0, y = 0
        tabMD.add(new ArrayList<>()); //x = 1, y = 0
        tabMD.add(new ArrayList<>()); //x = 2, y = 0
        tabMD.add(new ArrayList<>()); // x = 0, y = 1...
        tabMD.add(new ArrayList<>());
        tabMD.add(new ArrayList<>());
        tabMD.add(new ArrayList<>());// x = 0, y = 2...
        tabMD.add(new ArrayList<>());
        tabMD.add(new ArrayList<>());
        
        //On crée des tableaux de haut en bas pour mieux manipuler les valeurs
        for (int i=0;i<TAILLE;i++){
            for (int j=0;j<TAILLE;j++){
                for (int k = 0; k < TAILLE; k++) {
                    ArrayList<ArrayList<Case>> grille = grilles.get(k).getGrille();
                    int index = grille.get(i).get(j).getY() * 3 + grille.get(i).get(j).getX();
                    tabMD.get(index).add(grille.get(i).get(j));
                }
            }
        }
        //Si on clique sur Q
        if(direction==MONTERG){
            for (int i=0;i<TAILLE;i++){
                //Si le voisin a une valeur de 0 OU le voisin du voisin est nul
                if(tabMD.get(i).get(1).getValeur()==0
                    || (tabMD.get(i).get(1).getValeur()!=0 
                    && tabMD.get(i).get(2).getValeur()==0)){
                    //Je vais de droite à gauche pour déplacer les éléments dans le bon sens
                    for (int k=TAILLE-1;k>=0;k--){
                        deplacementUneCase(GAUCHE,tabMD.get(i),k);
                    }
                }
                else{
                    for (int k=0;k<TAILLE;k++){
                        deplacementUneCase(GAUCHE,tabMD.get(i),k);
                    }
                }

            }
        }   
        //Si on clique sur E
        if(direction==DESCG){
            for (int i=0;i<TAILLE;i++){
                //Si le voisin a une valeur de 0 OU le voisin du voisin est nul
                if(tabMD.get(i).get(1).getValeur()==0
                    || (tabMD.get(i).get(1).getValeur()!=0 
                    && tabMD.get(i).get(2).getValeur()==0)){
                    //Je vais de droite à gauche pour déplacer les éléments dans le bon sens
                    for (int k=TAILLE-1;k>=0;k--){
                        deplacementUneCase(GAUCHE,tabMD.get(i),k);
                    }
                }
                else{
                    for (int k=0;k<TAILLE;k++){
                        deplacementUneCase(GAUCHE,tabMD.get(i),k);
                    }
                }

            }
        }
        return deplacement;
    }
    
    /**
     * Méthode qui permet de déplacer une case de la grille
     * @param direc2 la direction vers laquelle on déplace la grille
     * @param tabHB la ligne/colonne où est située la case
     * @param k la colonne/ligne où est située la case
     */
    private boolean deplacementUneCase(int direc2,ArrayList<Case> tabMD, int k){
        boolean deplacement = false;
        int voisin;
        if (direc2==GAUCHE){
            voisin=k-1;
        }
        else{
            voisin=k+1;
        }
        if(voisin>=0&&voisin<TAILLE){
            //Si mon voisin a la même valeur que moi, je fusionne
            if (tabMD.get(voisin).getValeur()==
            tabMD.get(k).getValeur()){
                tabMD.get(voisin).getGrille().fusion(tabMD.get(voisin)); //on effectue la fusion sur la bonne grille
                tabMD.get(k).setValeur(0);
                deplacement=true;
            }
            //Si mon voisin =0, je me déplace
            if (tabMD.get(voisin).getValeur()==0){
                tabMD.get(voisin).setValeur(
                tabMD.get(k).getValeur());
                tabMD.get(k).setValeur(0);
                deplacement=true;
            }
        }
        return deplacement;
    }
    
    
    
    public boolean nouvCase () {  //renvoie vrai si on a ajouté une nouvelle case, faux sinon
        //Array List des cases libres pour chaque grille
        ArrayList<Case> casesLibresGh = gh.caseLibreG();
        ArrayList<Case> casesLibresGm = gm.caseLibreG();
        ArrayList<Case> casesLibresGb = gb.caseLibreG();

        boolean places[] = new boolean[3]; //[0] = true s'il reste de la place dans gh
        places[0] = casesLibresGh != null;
        places[1] = casesLibresGm != null;
        places[2] = casesLibresGb != null;

        if (places[0] || places[1] || places[2]) { //si au moins 1 grille n'est pas pleine
            Random ra = new Random(); 
            int index = ra.nextInt(places.length);

            while (!places[index])  {
                index = ra.nextInt(places.length); // on récupère aléatoirement une grille non pleine
            } 
            
            Grille grilleChoisie = null;
            switch (index) { 
                case 0 -> grilleChoisie = gh;
                case 1 -> grilleChoisie = gm;
                case 2 -> grilleChoisie = gb;
            }
            return grilleChoisie.nouvelleCase();
        } // else méthode où on a perdu car toutes les grilles sont pleines
        else return false;
    }
   
    
}
