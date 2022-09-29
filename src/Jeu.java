
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
public class Jeu {
    private Grille gh; //grille du haut
    private Grille gm;  //grille du milieu
    private Grille gb;  //grille du bas
    private int scoreFinal = 0;
    private int scoreDeplacement ; //score gagné pour 1 déplacement
    private boolean deplacement = false; //indique si on doit ajouter une nouvelle case ou non = s’il y a eu déplacement ou non. false s’il n’a pas encore eu lieu, true si c’est le cas.

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

    public boolean deplacement () {
        return this.deplacement;
    }

    public void setScoreFinal (int score)  {
        this.scoreFinal = score;
    }

    public void setDeplacement (boolean dep)  {
        this.deplacement = dep;
    }

    public void setScoreDeplacement( int sc) {
        this.scoreDeplacement = sc;
    }

    //non terminé
    public void majScore () { //méthode qui incrémente les points gagnés au scoreFinal à chaque déplacement
        if  (this.deplacement) {  	//si un développement a été effectué
            int sc = this.scoreFinal + this.scoreDeplacement;
            this.setScoreFinal(sc);
        }
    }


    public void gameOver ()  {

    }

    public void victory ()  {

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
