package jeuConsole;


import jeuConsole.Grille;
import jeuConsole.Case;
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
    private ArrayList<Grille> grids = new ArrayList<Grille>();
    private int scoreFinal = 0;

    /**
     * Constructeur qui initialise le tableau grids
     */
    public Jeu(){
        Grille g,g1,g2;
        g=new Grille();
        g1=new Grille();
        g2=new Grille();
        grids.add(g);
        grids.add(g1);
        grids.add(g2);
        //Initialise les 3 grilles dans le tableau grids
    }

    public int getScoreFinal() {
        return this.scoreFinal;
    }

    public void setScoreFinal(int score) {
        this.scoreFinal = score;
    }

    public ArrayList<Grille> getGrids(){
        return grids;
    }
    
    public void setGrids(ArrayList<Grille> newgrids){
        grids=newgrids;
    }

    @Override
    /**
     * Methode qui permet d'afficher les grilles dans l'axe horizontal.
     * @return String
     */
    public String toString() {
        //Affichage à l'horizontal des trois grilles
        String result = "";
        String result_interm="";
        //On boucle sur le nombre de lignes à afficher
        for (int i=0;i<TAILLE;i++){
            //On boucle sur le nombre de grilles à afficher
            for (int j=0;j<grids.size();j++){
                result_interm="";
                result_interm += "[";
                //Pour afficher une ligne
                for (int k = 0; k < TAILLE; k++) {
                    if (k != TAILLE - 1) {
                        result_interm +=  grids.get(j).getGrille().get(i).get(k).getValeur() + ", ";
                    } else {
                        result_interm += grids.get(j).getGrille().get(i).get(k).getValeur() + "]";
                    }
                }
                result+=result_interm+"\t";
            }
            result+="\n";
        }
        return result;
    }

    // Incrémente les points gagnés au scoreFinal à chaque déplacement
    public void updateScore() {
        scoreFinal = grids.get(0).getScoreG() + grids.get(1).getScoreG() + grids.get(2).getScoreG();
    }

    // Affiche une partie perdue et ferme le programme
    public void gameOver() {
        System.out.println("Vous avez perdu, retentez votre chance!");
        this.toString();
        System.exit(1);
    }

    // Affiche une partie gagnée et ferme le programme
    public void victory() {
        System.out.println("Vous avez gagné! Votre score est de :" + scoreFinal);
        this.toString();
        System.exit(0);
    }

    //TEMPLATE :
    /**
     * Bouge les cases selon la direction.
     * Si direction est locale (c'est à dire gauche, droite, haut, bas), on 
     * déplace localement les cases dans chaque grille.
     * Si direction est globale (déplacement q ou e), on déplace les cases en
     * conséquent selon les différents cas vus dans le jeu du prof.
     * @param direction
     * @return 
     */
    public boolean moveCases(int direction) {
        boolean deplacement = false;

        //Ajout de liste dans une liste
        ArrayList<ArrayList<Case>> MoveUpAndDown = new ArrayList<>();
        MoveUpAndDown.add(new ArrayList<>()); //x = 0, y = 0
        MoveUpAndDown.add(new ArrayList<>()); //x = 1, y = 0
        MoveUpAndDown.add(new ArrayList<>()); //x = 2, y = 0
        MoveUpAndDown.add(new ArrayList<>()); // x = 0, y = 1...
        MoveUpAndDown.add(new ArrayList<>());
        MoveUpAndDown.add(new ArrayList<>());
        MoveUpAndDown.add(new ArrayList<>());// x = 0, y = 2...
        MoveUpAndDown.add(new ArrayList<>());
        MoveUpAndDown.add(new ArrayList<>());

        // On crée des tableaux de haut en bas pour mieux manipuler les valeurs
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                for (int k = 0; k < TAILLE; k++) {
                    ArrayList<ArrayList<Case>> grille = grids.get(k).getGrille();
                    int index = grille.get(i).get(j).getY() * 3 + grille.get(i).get(j).getX();
                    MoveUpAndDown.get(index).add(grille.get(i).get(j));
                }
            }
        }
        

        // Direction
        if (direction == DROITE || direction == GAUCHE || direction == BAS || direction == HAUT) {
            for (int i = 0; i < TAILLE; i++) {
                grids.get(i).deplacerCases(direction);

            }
        }

        // Direction = Supérieure
        if (direction == MONTERG) {
            for (int i = 0; i < TAILLE; i++) {
                //Si le voisin a une valeur de 0 OU le voisin du voisin est nul
                if (MoveUpAndDown.get(i).get(1).getValeur() == 0
                        || (MoveUpAndDown.get(i).get(1).getValeur() != 0
                        && MoveUpAndDown.get(i).get(2).getValeur() == 0)) {
                    //Je vais de droite à gauche pour déplacer les éléments dans le bon sens
                    for (int k = TAILLE - 1; k >= 0; k--) {
                        deplacementUneCaseUD(GAUCHE, MoveUpAndDown.get(i), k);
                    }
                } else {
                    for (int k = 0; k < TAILLE; k++) {
                        deplacementUneCaseUD(GAUCHE, MoveUpAndDown.get(i), k);
                    }
                }

            }
        }

        // Direction = Inférieure
        if (direction == DESCG) {
            for (int i = 0; i < TAILLE; i++) {
                //Si le voisin a une valeur de 0 OU le voisin du voisin est nul
                if (MoveUpAndDown.get(i).get(1).getValeur() == 0
                        || (MoveUpAndDown.get(i).get(1).getValeur() != 0
                        && MoveUpAndDown.get(i).get(2).getValeur() == 0)) {
                    //Je vais de droite à gauche pour déplacer les éléments dans le bon sens
                    for (int k = TAILLE - 1; k >= 0; k--) {
                        deplacementUneCaseUD(GAUCHE, MoveUpAndDown.get(i), k);
                    }
                } else {
                    for (int k = 0; k < TAILLE; k++) {
                        deplacementUneCaseUD(GAUCHE, MoveUpAndDown.get(i), k);
                    }
                }

            }
        }
        return deplacement;
    }

    /**
     * Méthode qui permet de déplacer une case de la grille
     *
     * @param direction2 la direction vers laquelle on déplace la grille
     * @param tabHB la ligne/colonne où est située la case
     * @param tilesLocation la colonne/ligne où est située la case
     */
    private boolean deplacementUneCaseUD(int direction2, ArrayList<Case> MoveUpAndDown, int tilesLocation) {
        boolean deplacement = false;
        int neighboringTile;

        if (direction2 == GAUCHE) {
            neighboringTile = tilesLocation - 1;
        } else {
            neighboringTile = tilesLocation + 1;
        }
        if (neighboringTile >= 0 && neighboringTile < TAILLE) {
            //Si mon voisin a la même valeur que moi, je fusionne
            if (MoveUpAndDown.get(neighboringTile).getValeur()
                    == MoveUpAndDown.get(tilesLocation).getValeur()) {
                MoveUpAndDown.get(neighboringTile).getGrille().fusion(MoveUpAndDown.get(neighboringTile)); //on effectue la fusion sur la bonne grille
                MoveUpAndDown.get(tilesLocation).setValeur(0);
                deplacement = true;
            }
            //Si mon voisin =0, je me déplace
            if (MoveUpAndDown.get(neighboringTile).getValeur() == 0) {
                MoveUpAndDown.get(neighboringTile).setValeur(
                        MoveUpAndDown.get(tilesLocation).getValeur());
                MoveUpAndDown.get(tilesLocation).setValeur(0);
                deplacement = true;
            }
        }
        return deplacement;
    }
/**public void ajoutCase() {
        int nombreAleatoireCase = (int) (Math.random() * ((1 - 0) + 1));

        if (nombreAleatoireCase == 1) {
            int nombreAleatoireGrille = (int) (Math.random() * ((2 - 0) + 1));
            int nombreAleatoireGrille2 = (int) (Math.random() * ((2 - 0) + 1));

            switch (nombreAleatoireGrille) {
                case (0):
                    this.topGrid.nouvelleCase();

                    break;
                case (1):

                    this.middleGrid.nouvelleCase();
                    break;

                case (2):
                    this.bottomGrid.nouvelleCase();
                    break;
            }

            switch (nombreAleatoireGrille2) {
                case (0):
                    this.topGrid.nouvelleCase();

                    break;
                case (1):

                    this.middleGrid.nouvelleCase();
                    break;

                case (2):
                    this.bottomGrid.nouvelleCase();
                    break;
            }

        } else {
            int nombreAleatoireGrille = (int) (Math.random() * ((2 - 0) + 1));

            switch (nombreAleatoireGrille) {
                case (0):
                    this.topGrid.nouvelleCase();

                    break;
                case (1):

                    this.middleGrid.nouvelleCase();
                    break;

                case (2):
                    this.bottomGrid.nouvelleCase();
                    break;
            }
        }
    }**/
    //TEMPLATE : 
    /**
     * On peut choisir si on ajoute une ou deux cases (au hasard) : plutôt dans la boucle de jeu que dans la méthode
     * Si on ajoute une case, on choisir une grille au hasard où la mettre.
     * Si on ajotue deux cases, on choisit deux grilles au hasard où les placer.
     * Les deux grilles choisies peuvent être identiques.
     * On ajoute les nouvelles cases.
     * @return 
     */
    public boolean addTiles() {
        boolean places[] = new boolean[3]; 
        places[0] = true; //on suppose qu'il y a de la place dans les grilles
        places[1] = true;
        places[2] = true;
        
        Random ra = new Random();
        int index = ra.nextInt(grids.size()); //on choisit un index

        while (!grids.get(index).nouvelleCase()) { //si une grille est pleine, on en choisit une autre, sinon on ajoute une case
            places[index] = false; // On enregistre que la grille testée est pleine
            if (!(places[0] || places[1] || places[2])) return false; // Retourne faux si toutes les grilles sont pleines
            index = ra.nextInt(grids.size()); // on récupère aléatoirement une grille
        }
        
        return true; //une nouvelle case a été ajoutée
    }
    

    //l'ordinateur effectue un déplacement aléatoire pour le prochain coup
    public boolean randomMove (){
        boolean moveDone = false; //faux si le déplacement n'a pas été effectué
        Random ra = new Random(); 
        
        //on met les valeurs correspondant aux déplacements dans une liste
        ArrayList<Integer> values = new ArrayList<>();
        values.add(BAS);
        values.add(DROITE);
        values.add(GAUCHE);
        values.add(HAUT);
        values.add(MONTERG);
        values.add(DESCG);
        
        int index = ra.nextInt(values.size()); 
        
        int chosenMove = values.get(index);
        moveDone = moveCases(chosenMove);
        
        return moveDone; 
    }


    //TEMPLATE :
    /**
     * Vérifie si le jeu est fini (plus aucun déplacement possible).
     * On vérifie que pour chaque grille, on ne peut aller ni en haut, ni
     * en bas, ni à gauche, ni à droite.
     * On vérifie que l'on ne peut pas faire le déplacement q et e (voir le jeu
     * du prof).
     * Si c'est possible, on return true, sinon false.
     * @return 
     */
    public boolean finishGame() {

        return false;
    }

    
    
    //TEMPLATE :
    /**
     * Lance le jeu et le fait marcher tant que le joueur n'a ni gagné ni perdu.
     * On crée le jeu (avec constructeur).
     * On le lance avec 2 cases déjà remplies 
     * On laisse l'utilisateur choisir son déplacement
     * On déplace les éléments en conséquent.
     * On update le score
     * On ajoute de nouvelles cases si c'est possible :  une ou deux cases (au hasard)
     * On vérifie que le jeu n'est pas fini.
     * Si le jeu est fini, on regarde si le joueur a gagné (score=2048) ou perdu.
     * Si le joueur a perdu, message de gameOver, sinon message de Victory, et 
     * on termine programme.
     */
    public void launchGame(){
        
    }

}
