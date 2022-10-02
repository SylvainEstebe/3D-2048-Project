
import java.util.ArrayList;
import java.util.Random;



/**
 * Classe qui réalise toutes les fonctionnalités sur une grille précise du jeu
 * @author Alexanne
 */
public class Grille implements Parametres {
    private ArrayList<ArrayList<Case>> grille;
    private int scoreg;
    private int type; //indique si c'est une grille du haut, du milieu ou du bas
    private boolean deplacement=false;
    
    /**
     * Constructeur qui initialise une grille vide
     */
    public Grille(){
        grille=new ArrayList<ArrayList<Case>>();
        for (int i=0;i<TAILLE;i++){
            grille.add(new ArrayList<Case>());
            for (int j=0;j<TAILLE;j++){
                grille.get(i).add(new Case(i,j,0,this));
            }
        }
    }
    
    @Override
    /**
     * Méthode qui affiche la grille dans le terminal.
     * @return La grille sous forme de String
     */
    public String toString() {
        String result = "";
        for (int i=0;i<TAILLE;i++){
            result+="[";
            for (int j=0;j<TAILLE;j++){
                if(j!=TAILLE-1){
                    result+=grille.get(i).get(j).getValeur()+", ";
                }
                else{
                    result+=grille.get(i).get(j).getValeur()+"]";
                }
            }
            result+="\n";
        }
        return result;
    }
    
    /**
     * Méthode qui retourne la grille sous forme de tableau
     * @return la grille sous forme de tableau à deux dimensions
     */
    public ArrayList<ArrayList<Case>> getGrille(){
        return grille;
    }
    
    /**
     * Méthode qui retourne le score obtenu lors du jeu pour une grille
     * @return le score sous forme d'entier
     */
    public int getScoreG(){
        return scoreg;
    }
    
    /**
     * Méthode qui retourne le type de la grille (haut/milieu/bas)
     * @return le type sous forme d'entier
     */
    public int getType(){
        return type;
    }
    
    /**
     * Méthode qui permet de modifier le type de la grille
     * @param type sous forme d'entier
     */
    public void setType(int type){
        this.type=type;
    }
    
    /**
     * Méthode qui vérifie si une partie est finie (éléments impossibles 
     * à bouger).
     * @return un booléen qui indique si la partie est finie ou non
     */
    public boolean partieFinieG(){
        for (int i=0;i<TAILLE;i++){
            for (int j=0;j<TAILLE;j++){
                if (grille.get(i).get(j).getValeur()==0){
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
     * Fonction qui fusionne deux cases d'une grille
     * @param c la case qu'on fusionne
     */
    private void fusion(Case c) {
        c.setValeur(c.getValeur() * 2);
        if (this.scoreg < c.getValeur()) {
            this.scoreg = c.getValeur();
        }  
    }
    
    /**
     * Méthode qui permet de déplacer les cases dans une direction
     * @param direction un entier (gauche, droite, haut, bas)
     * @return un booléen qui indique si on a bougé des cases
     */
    public boolean deplacerCases(int direction){
        deplacement=false;
        if(direction ==HAUT||direction==BAS){
            ArrayList<ArrayList<Case>> tabHB=new ArrayList<ArrayList<Case>>();
            tabHB.add(new ArrayList<Case>());
            tabHB.add(new ArrayList<Case>());
            tabHB.add(new ArrayList<Case>());
            //On crée des tableaux de haut en bas pour mieux manipuler les valeurs
            for (int i=0;i<TAILLE;i++){
                for (int j=0;j<TAILLE;j++){
                    if(grille.get(i).get(j).getY()==0){
                        
                        tabHB.get(0).add(grille.get(i).get(j));
                    }
                    else if(grille.get(i).get(j).getY()==1){
                     
                        tabHB.get(1).add(grille.get(i).get(j));
                    }
                    else {
                        
                        tabHB.get(2).add(grille.get(i).get(j));
                    }
                }
            }
            //Si la direction est haut
            if(direction==HAUT){
                for (int i=0;i<TAILLE;i++){
                    //Si le voisin a une valeur de 0 OU le voisin du voisin est nul
                    if(tabHB.get(i).get(1).getValeur()==0
                        || (tabHB.get(i).get(1).getValeur()!=0 
                        && tabHB.get(i).get(2).getValeur()==0)){
                        //Je vais de droite à gauche pour déplacer les éléments dans le bon sens
                        for (int k=TAILLE-1;k>=0;k--){
                            deplacementUneCase(GAUCHE,tabHB.get(i),k);
                        }
                    }
                    else{
                        for (int k=0;k<TAILLE;k++){
                            deplacementUneCase(GAUCHE,tabHB.get(i),k);
                        }
                    }
                    
                }
            }
            if(direction==BAS){
                for (int i=0;i<TAILLE;i++){
                    if(tabHB.get(i).get(1).getValeur()==0
                        || (tabHB.get(i).get(1).getValeur()!=0 
                        && tabHB.get(i).get(2).getValeur()==0)){
                        //Je vais de droite à gauche pour déplacer les éléments dans le bon sens
                        for (int k=0;k<TAILLE;k++){
                            deplacementUneCase(DROITE,tabHB.get(i),k);
                        }
                    }
                    else{
                        for (int k=TAILLE-1;k>=0;k--){
                            deplacementUneCase(DROITE,tabHB.get(i),k);
                        }
                    }
                }
            } 
        }
        if(direction == DROITE || direction == GAUCHE){
            if(direction==DROITE){
               for (int i=0;i<TAILLE;i++){
                    if(grille.get(i).get(1).getValeur()==0
                        || (grille.get(i).get(1).getValeur()!=0 
                        && grille.get(i).get(2).getValeur()==0)){
                        //Je vais de droite à gauche pour déplacer les éléments dans le bon sens
                        for (int k=0;k<TAILLE;k++){
                            deplacementUneCase(DROITE,grille.get(i),k);
                        }
                    }
                    else{
                        for (int k=TAILLE-1;k>=0;k--){
                            deplacementUneCase(DROITE,grille.get(i),k);
                        }
                    }
                    
                } 
            }
            if(direction==GAUCHE){
               for (int i=0;i<TAILLE;i++){
                    if(grille.get(i).get(1).getValeur()==0
                        || (grille.get(i).get(1).getValeur()!=0 
                        && grille.get(i).get(2).getValeur()==0)){
                        //Je vais de droite à gauche pour déplacer les éléments dans le bon sens
                        for (int k=TAILLE-1;k>=0;k--){
                            deplacementUneCase(GAUCHE,grille.get(i),k);
                        }
                    }
                    else{
                        for (int k=0;k<TAILLE;k++){
                            deplacementUneCase(GAUCHE,grille.get(i),k);
                        }
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
    private void deplacementUneCase(int direc2,ArrayList<Case> tabHB, int k){
        int voisin;
        if (direc2==GAUCHE){
            voisin=k-1;
        }
        else{
            voisin=k+1;
        }
        if(voisin>=0&&voisin<TAILLE){
            
            //Si mon voisin a la même valeur que moi, je fusionne
            if (tabHB.get(voisin).getValeur()==
            tabHB.get(k).getValeur()){
                fusion(tabHB.get(voisin));
                tabHB.get(k).setValeur(0);
                deplacement=true;
            }
            
            //Si mon voisin =0, je me déplace
            if (tabHB.get(voisin).getValeur()==0){
                tabHB.get(voisin).setValeur(
                tabHB.get(k).getValeur());
                tabHB.get(k).setValeur(0);
                deplacement=true;
            }
        }
    }
    
    /**
     * Méthode qui place une nouvelle case dans la grille s'il reste 
     * des cases vides
     * @return Un booléen qui indique si on a bougé la case ou non
     */
    public boolean nouvelleCase(){
        //On détermine si on prend 2 ou 4 pour la case
        int nombre_case=0;
        Random rand = new Random(); 
        int nombreAlea = rand.nextInt(9 -0 + 1);
        if (nombreAlea <6.6){
            nombre_case=2;
        }
        else{
            nombre_case=4;
        }
        if(caseLibreG()!=null){
            ArrayList<Case> cases_vides=caseLibreG();
            Random random_method = new Random();
            int index = random_method.nextInt(cases_vides.size());
            cases_vides.get(index).setValeur(nombre_case);
            return true;
        }  
        return false;  
    }
    
    /**
     * Méthode qui permet de dire s'il y a une case libre dans la grille
     * @return Un tableau de cases libres
     */
    public ArrayList<Case> caseLibreG(){
        ArrayList<Case> cases_vides=new ArrayList<Case>();
        for (int i=0;i<TAILLE;i++){
            for (int j=0;j<TAILLE;j++){
                if(grille.get(i).get(j).getValeur()==0){
                    cases_vides.add(grille.get(i).get(j));
                }
            }
        }
        if(cases_vides.size()!=0){
           return cases_vides; 
        }
        return null;
    }
 
    /**
     * Méthode qui affiche la victoire et le score de la case
     */
    public void victory(){
        System.out.println("Vous avez gagné! Votre score est de :"+scoreg);
        System.exit(0);
    }
    
    /**
     * méthode qui affiche la défaite 
     */
    public void gameOver(){
        System.out.println("Vous avez perdu, retentez votre chance!");
        System.exit(1);
    }
}
