package ia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import modele.Case;
import modele.Jeu;
import modele.ThreadAffichIACons;
import static variables.Parametres.BAS;
import static variables.Parametres.DESCG;
import static variables.Parametres.DROITE;
import static variables.Parametres.GAUCHE;
import static variables.Parametres.HAUT;
import static variables.Parametres.MONTERG;
import static variables.Parametres.OBJECTIF;

/**
 * Classe qui instancie l'IA qui respecte le 1er algorithme
 *
 * @author Mouna
 */
public class IA1 {

    private String joueur1 = "Max";
    private String joueur2 = "Min";
    private boolean arreter = false;
    private Jeu jeu;
    //int meilleurDirection = 0;

    /**
     * Constructeur de l'IA
     * @param j jeu associé à l'IA
     */
    public IA1(Jeu j) {
        jeu = j;
    }

    public void jeuIA1() {
        //Thread qui permet l'affichage de l'IA en console
        ThreadAffichIACons arreterIa = new ThreadAffichIACons();
        arreterIa.start();
        int i = 0;
        int total = 50;
        boolean b2;
        System.out.println("L'IA va jouer " + total + " tours");
        System.out.println(jeu);
        try {
            while (!jeu.finJeu() && !arreter && i < total) {
                int profondeur = 5;
                int direction = this.meilleurMouvement(jeu, profondeur);
                b2 = this.jeu.deplacerCases3G(direction);
                this.jeu.majScore();
                System.out.println(" \n Max effectue un déplacement");
                System.out.println(jeu.toString());
                Case nouvelleCase = this.meilleurCase(jeu, profondeur);
                jeu.ajoutCase(nouvelleCase);
                System.out.println("Min ajoute une case");
                System.out.println(jeu.toString());
                System.out.println(" Tapez 's' puis Entree pour stopper l'IA");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Jeu.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (!arreterIa.isAlive()) {
                    arreter = true;
                }
                i++;
                if(jeu.finJeu() || i==total){
                    jeu.jeuPerdu();
                }
                if(jeu.getValeurMaxJeu()>=OBJECTIF){
                   jeu.victoire();
                }
            }
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(IA1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
/**
     * Méthode qui cherche le meilleur mouvement à faire (associé à max)
     *
     * @param jeu l'état du jeu actuel
     * @param profondeur : profondeur maximale de l'arbre
     * @return le meilleur mouvement à faire
     * @throws CloneNotSupportedException exception possible quand clone
     */
    public int meilleurMouvement(Jeu jeu, int profondeur) throws CloneNotSupportedException {
        Map<String, Object> result = this.minimax(jeu, profondeur, joueur1);
        return (int) result.get("Direction");
    }

    /**
     * Méthode qui cherche le meilleur mouvement à faire (associé à min)
     *
     * @param jeu l'état actuel du jeu
     * @param profondeur : profondeur maximale de l'arbre
     * @return la case à rajouter
     * @throws CloneNotSupportedException exception possible quand clonage
     */
    public Case meilleurCase(Jeu jeu, int profondeur) throws CloneNotSupportedException {
        Map<String, Object> result = this.minimax(jeu, profondeur, joueur2);
        return (Case) result.get("Case");
    }
/**
     * Algorithme min-max      *
     * @param Jeu 
     * @param profondeur
     * @param joueur
     * @return alphabeta
     * @throws CloneNotSupportedException
     */
    private Map<String, Object> minimax(Jeu jeu, int profondeur, String joueur) throws CloneNotSupportedException {
        Map<String, Object> resultat = new HashMap<>();
        ArrayList<Integer> directions = new ArrayList<>();
        directions.add(HAUT);
        directions.add(BAS);
        directions.add(GAUCHE);
        directions.add(DROITE);
        directions.add(DESCG);
        directions.add(MONTERG);
        int meilleurScore;
        int meilleurDirection = -10;
        Case meilleurCase = null;
        if (profondeur == 0 || jeu.finJeu()) {
            meilleurScore = jeu.scoreHeuristique(jeu.getScoreFinal(), jeu.listeCaseVideMultiGrille().size(), jeu.scoreDispersion());
        } else {
            if (joueur.equals(joueur1)) {
                meilleurScore = Integer.MIN_VALUE;
                for (int i = 0; i < directions.size(); i++) {
                    Jeu nouveauJeu = jeu.clone();
                    boolean b = nouveauJeu.deplacerCases3G(directions.get(i));
                    if (b == false && nouveauJeu.equals(jeu)) {
                        continue;
                    }
                    this.jeu.majScore();
                    Map<String, Object> currentResult = minimax(nouveauJeu, (profondeur - 1), joueur);
                    int scoreActuelle = ((Number) currentResult.get("Score")).intValue();
                    if (scoreActuelle > meilleurScore) { 
                        meilleurScore = scoreActuelle;
                        meilleurDirection = directions.get(i);
                    }
                }
            } else {
                meilleurScore = Integer.MAX_VALUE;
                Jeu nouveauJeu = jeu.clone();
                ArrayList<Case> casesVides = nouveauJeu.listeCaseVideMultiGrille();
                if (casesVides.isEmpty()) {
                    meilleurScore = 0;
                }
                for (int i = 0; i < casesVides.size(); i++) {
                    //On détermine si on prend 2 ou 4 pour la case
                    int valeurCase = 0;
                    Random rand = new Random();
                    int nombreAlea = rand.nextInt(9 - 0 + 1);
                    if (nombreAlea < 6.6) {
                        valeurCase = 2;
                    } else {
                        valeurCase = 4;
                    }
                    this.jeu.majScore();
                    Map<String, Object> currentResult = minimax(nouveauJeu, profondeur - 1, joueur);
                    int currentScore = ((Number) currentResult.get("Score")).intValue();
                    if (currentScore < meilleurScore) { 
                        meilleurScore = currentScore;
                        meilleurCase = casesVides.get(i);
                        meilleurCase.setValeur(valeurCase);
                    }
                }
            }
        }

        resultat.put("Case", meilleurCase);
        resultat.put("Score", meilleurScore);
        resultat.put("Direction", meilleurDirection);
        return resultat;
    }

}
