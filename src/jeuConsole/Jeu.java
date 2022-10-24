package jeuConsole;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

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
        g=new Grille(this, GRILLEH);
        g1=new Grille(this, GRILLEM);
        g2=new Grille(this, GRILLEB);
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
    
    /**
     * 
     * @return la valeur maximale du jeu
     */
    public int getValeurMaxJeu (){
        int max = 0;

        for (int i = 0; i < TAILLE; i++) {
            if (grids.get(i).getValeurMax() > max) {
               max = grids.get(i).getValeurMax();
            }
        }
        return max;
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
    public boolean moveTiles(int direction) {
        boolean deplacement = false;
        
        // Direction
        if (direction == DROITE || direction == GAUCHE || direction == BAS || direction == HAUT) {
            for (int i = 0; i < TAILLE; i++) {
                boolean d = grids.get(i).deplacerCases(direction);
                if (d) deplacement = d;
            }
        }
        
        if (direction == MONTERG || direction == DESCG) {
            //Ajout de liste dans une liste
            ArrayList<ArrayList<Case>> moveUpAndDown = new ArrayList<>();
            moveUpAndDown.add(new ArrayList<>()); //x = 0, y = 0
            moveUpAndDown.add(new ArrayList<>()); //x = 1, y = 0
            moveUpAndDown.add(new ArrayList<>()); //x = 2, y = 0
            moveUpAndDown.add(new ArrayList<>()); // x = 0, y = 1...
            moveUpAndDown.add(new ArrayList<>());
            moveUpAndDown.add(new ArrayList<>());
            moveUpAndDown.add(new ArrayList<>());// x = 0, y = 2...
            moveUpAndDown.add(new ArrayList<>());
            moveUpAndDown.add(new ArrayList<>());

            // On crée des tableaux de haut en bas pour mieux manipuler les valeurs
            for (int i = 0; i < TAILLE; i++) {
                for (int j = 0; j < TAILLE; j++) {
                    for (int k = 0; k < TAILLE; k++) {
                        ArrayList<ArrayList<Case>> grille = grids.get(k).getGrille();
                        int index = grille.get(i).get(j).getY() * 3 + grille.get(i).get(j).getX();
                        moveUpAndDown.get(index).add(grille.get(i).get(j));
                    }
                }
            }

            // Direction = Supérieure
            if (direction == MONTERG) {
                for (int i = 0; i < TAILLE*TAILLE; i++) {
                    for (int k = 0; k < TAILLE; k++) {
                        boolean d = deplacementUneCaseUD(MONTERG, moveUpAndDown.get(i), k);
                        if (d) deplacement = d;
                    }
                }
            }

            // Direction = Inférieure
            if (direction == DESCG) {
                for (int i = 0; i < TAILLE*TAILLE; i++) {
                    for (int k = TAILLE- 1; k >= 0; k--) {
                        boolean d = deplacementUneCaseUD(DESCG, moveUpAndDown.get(i), k);
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
     * @param direction2 la direction vers laquelle on déplace la grille
     * @param tabHB la ligne/colonne où est située la case
     * @param tilesLocation la colonne/ligne où est située la case
     */
    private boolean deplacementUneCaseUD(int direction2, ArrayList<Case> moveUpAndDown, int tilesLocation) {
        boolean deplacement = false;
        int neighboringTile;

        if (direction2 == MONTERG) {
            neighboringTile = tilesLocation - 1;
        } else {
            neighboringTile = tilesLocation + 1;
        }
        
        // Tant que mon voisin = 0, je me déplace avant de chercher à fusionner
        while (neighboringTile >= 0 && neighboringTile < TAILLE && moveUpAndDown.get(neighboringTile).getValeur() == 0) {
            moveUpAndDown.get(neighboringTile).setValeur(moveUpAndDown.get(tilesLocation).getValeur());
            moveUpAndDown.get(tilesLocation).setValeur(0);
            deplacement = true;
            
            // Je mets à jour mon index et celui de mon voisin
            if (direction2 == MONTERG) {
                neighboringTile--;
                tilesLocation--;
            } else {
                neighboringTile++;
                tilesLocation++;
            }
        }
        
        if (neighboringTile >= 0 && neighboringTile < TAILLE) {
            //Si mon voisin a la même valeur que moi, je fusionne
            if (moveUpAndDown.get(neighboringTile).getValeur()
                    == moveUpAndDown.get(tilesLocation).getValeur()) {
                moveUpAndDown.get(neighboringTile).getGrille().fusion(moveUpAndDown.get(neighboringTile)); //on effectue la fusion sur la bonne grille
                moveUpAndDown.get(tilesLocation).setValeur(0);
                deplacement = true;
            }
        }
        
        return deplacement;
    }

    /**
     * On peut choisir si on ajoute une ou deux cases (au hasard) : plutôt dans la boucle de jeu que dans la méthode
     * Si on ajoute une case, on choisir une grille au hasard où la mettre.
     * Si on ajotue deux cases, on choisit deux grilles au hasard où les placer.
     * Les deux grilles choisies peuvent être identiques.
     * On ajoute les nouvelles cases.
     * @return true si une case a été ajoutée
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
    
     /**
     * l'ordinateur effectue un déplacement aléatoire pour le prochain coup
     *
     * @return true si le mouvement aléatoire a été effectué
     */
    
    public boolean randomMove () {
        Random ra = new Random(); 
        
        //on met les valeurs correspondant aux déplacements dans une liste
        ArrayList<Integer> values = new ArrayList<>();
        values.add(BAS);
        values.add(DROITE);
        values.add(GAUCHE);
        values.add(HAUT);
        values.add(MONTERG);
        values.add(DESCG);
        
        boolean[] moves = new boolean[6];
        moves[0] = true;
        moves[1] = true;
        moves[2] = true;
        moves[3] = true;
        moves[4] = true;
        moves[5] = true;
        
        int index = ra.nextInt(values.size()); 
        
        int chosenMove = values.get(index);
        while (!moveTiles(chosenMove)) {
            moves[index] = false; // On enregistre que le mouvement choisi est possible
            if (!(moves[0] || moves[1] || moves[2] || moves[3] || moves[4] || moves[5])) return false; // Retourne faux si toutes mouvements sont impossibles
            index = ra.nextInt(values.size());
        }

        return true; 
    }


    /**
     * Vérifie que la partie est terminée
     *
     * @return true si aucune possibilité de déplacement restante : la partie est terminée
     */
    public boolean finishGame() {
        for (int i = 0; i < TAILLE; i++) {
            boolean fini = grids.get(i).partieFinieG();
            if (fini) {
                return true;
            }
        }
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
        Scanner sc = new Scanner(System.in);
        Random ra = new Random(); 
        
        //le jeu commence avec 2 cases
        this.addTiles();
        this.addTiles();
        
        // Début de la partie
        System.out.println("Début du jeu");
        System.out.println(this);
        
        while (!this.finishGame()) {
            System.out.println("Déplacer vers la Droite (d), Gauche (g), Haut (h), ou Bas (b), niveau supérieur (e) niveau inférieur (q) ?");
            String s = sc.nextLine();
            s = s.toLowerCase();
            if (!(s.equals("d") || s.equals("droite")
                    || s.equals("g") || s.equals("gauche")
                    || s.equals("h") || s.equals("haut")
                    || s.equals("b") || s.equals("bas")
                    || s.equals("e") || s.equals("niveau supérieur")
                    || s.equals("q") || s.equals("niveau inférieur")
                    )) {
                System.out.println("Vous devez écrire d pour Droite, g pour Gauche, h pour Haut ou b pour Bas, e pour supérieur, q pour inférieur");
            } else {
                int direction;
                direction = switch (s) {
                    case "q", "niveau inférieur" -> DESCG;
                    case "e", "niveau supérieur" -> MONTERG;
                    case "d", "droite" -> DROITE;
                    case "g", "gauche" -> GAUCHE;
                    case "h", "haut" -> HAUT;
                    default -> BAS;
                };
                
                System.out.println("Avant de bouger");
                System.out.println(this);
                boolean b2 = this.moveTiles(direction);
                
                if (b2) {
                    //aléatoire : on ajoute 1 ou 2 cases : QUELLE PROBA D'AVOIR UN 2 ??? A VOIR
                    int random = ra.nextInt(2) +1;
                    for (int i = 0; i < random; i++) {
                        this.addTiles();
                    }
                }
                
                this.updateScore();
                System.out.println("Valeur max du jeu : " + this.getValeurMaxJeu());
                System.out.println("Score : " + scoreFinal);
                System.out.println("après avoir bougé");
                System.out.println(this);
                
            }
        } 
        
        if (this.getValeurMaxJeu() >= OBJECTIF) {
            this.victory();
        }
        else {
            this.gameOver();
        }
    }

}
