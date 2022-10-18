
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

    private Grille topGrid; //grille du haut
    private Grille middleGrid;  //grille du milieu
    private Grille bottomGrid;  //grille du bas
    private int scoreFinal = 0;
    private int scoreDeplacement; //score gagné pour 1 déplacement

    //getters et setters
    public Grille getGrilleh() {
        return this.topGrid;
    }

    public Grille getGrillem() {
        return this.middleGrid;
    }

    public Grille getGrilleb() {
        return this.bottomGrid;
    }

    public void setTopGrid(Grille g) {
        this.topGrid = g;
    }

    public void setMiddleGrid(Grille g) {
        this.middleGrid = g;
    }

    public void setBottomGrid(Grille g) {
        this.bottomGrid = g;
    }

    public int getScoreDeplacement() {
        return this.scoreDeplacement;
    }

    public int getScoreFinal() {
        return this.scoreFinal;
    }

    public void setScoreFinal(int score) {
        this.scoreFinal = score;
    }

    public void setScoreDeplacement(int sc) {
        this.scoreDeplacement = sc;
    }

    @Override
    public String toString() {
        String result = "";
        result = topGrid.toString() + "\n" + middleGrid.toString() + "\n" + bottomGrid.toString();
        return result;
    }

    // Incrémente les points gagnés au scoreFinal à chaque déplacement
    public void updtadeScore() {
        scoreFinal = topGrid.getScoreG() + middleGrid.getScoreG() + bottomGrid.getScoreG();
    }

    // Affiche une partie perdue et ferme le programme
    public void gameOver() {
        System.out.println("Vous avez perdu, retentez votre chance!");
        topGrid.toString();
        middleGrid.toString();
        bottomGrid.toString();
        System.exit(1);
    }

    // Affiche une partie gagnée et ferme le programme
    public void victory() {
        System.out.println("Vous avez gagné! Votre score est de :" + scoreFinal);
        topGrid.toString();
        middleGrid.toString();
        bottomGrid.toString();
        System.exit(0);
    }

    /**
     * Méthode qui permet de déplacer la case entre les différentes grilles en
     * fonction du choix de l'utilisateur
     *
     * @param direction direction sélectionner par l'utilisateur entre grille
     * inférieur(Q) et supérieur(E)
     * @return deplacement
     */
    public boolean deplacerG(int direction) {
        boolean deplacement = false;

        ArrayList<Grille> grilles = new ArrayList<>();
        // Ajout des différentes grille à la liste
        grilles.add(this.topGrid);
        grilles.add(this.middleGrid);
        grilles.add(this.bottomGrid);

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
                    ArrayList<ArrayList<Case>> grille = grilles.get(k).getGrille();
                    int index = grille.get(i).get(j).getY() * 3 + grille.get(i).get(j).getX();
                    MoveUpAndDown.get(index).add(grille.get(i).get(j));
                }
            }
        }
        // Différentes conditions

        // Direction
        if (direction == DROITE || direction == GAUCHE || direction == BAS || direction == HAUT) {
            for (int i = 0; i < TAILLE; i++) {
                grilles.get(i).deplacerCases(direction);

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

        // Direction = Inférieur
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

    /**
     * Méthode qui renvoie vrai si une nouvelle case est ajouté, faux dans le
     * cas contraire
     *
     * @return true si une nouvelle case est ajoutée, faux dans le cas
     * contraire.
     */
    public boolean nouvCase() {
        //Array List des cases libres pour chaque grille
        ArrayList<Case> casesLibrestopGrid = topGrid.caseLibreG();
        ArrayList<Case> casesLibresmiddleGrid = middleGrid.caseLibreG();
        ArrayList<Case> casesLibresbottomGrid = bottomGrid.caseLibreG();

        boolean places[] = new boolean[3]; //[0] = true s'il reste de la place dans gh
        places[0] = casesLibrestopGrid != null;
        places[1] = casesLibresmiddleGrid != null;
        places[2] = casesLibresbottomGrid != null;

        if (places[0] || places[1] || places[2]) { //si au moins 1 grille n'est pas pleine
            Random ra = new Random();
            int index = ra.nextInt(places.length);

            while (!places[index]) {
                index = ra.nextInt(places.length); // on récupère aléatoirement une grille non pleine
            }

            Grille grilleChoisie = null;
            switch (index) {
                case 0 ->
                    grilleChoisie = topGrid;
                case 1 ->
                    grilleChoisie = middleGrid;
                case 2 ->
                    grilleChoisie = bottomGrid;
            }
            return grilleChoisie.nouvelleCase();
        } // else méthode où on a perdu car toutes les grilles sont pleines
        else {
            return false;
        }
    }

    public boolean partieFinie() {

        return false;

    }

    public void ajoutCase() {
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
    }

}
