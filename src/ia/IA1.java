/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import modele.Case;
import modele.Jeu;
import static variables.Parametres.BAS;
import static variables.Parametres.DESCG;
import static variables.Parametres.DROITE;
import static variables.Parametres.GAUCHE;
import static variables.Parametres.HAUT;
import static variables.Parametres.MONTERG;
import static variables.Parametres.TAILLE;

/**
 * Classe qui instancie l'IA qui respecte le 1er algorithme
 *
 * @author Mouna
 */
public class IA1 {

    String joueur1 = "Max";
    String joueur2 = "Min";
    Jeu jeu;
    //int meilleurDirection = 0;

    /**
     * Constructeur de l'IA
     *
     * @param j jeu associé à l'IA
     */
    public IA1(Jeu j) {
        jeu = j;
    }

    public void IA1() {
        int wins = 0;
        int total = 20;
        System.out.println("Running " + total + " games to estimate the accuracy:");
        this.jeu.ajoutCases();
        this.jeu.ajoutCases();
        for (int i = 0; i < total; ++i) {
            try {
                int hintDepth = 5;
                System.out.println("Avant deplacement : ");
                System.out.println(jeu);
                int hint = this.findBestMove(this.jeu, hintDepth);
                System.out.println(hint);
                this.jeu.deplacerCases3G(hint);
                this.jeu.majScore();
                System.out.println("ajout d'une case");
                Case nouvelleCase = this.meilleurCase(jeu, hintDepth);
                System.out.println(nouvelleCase.toString());
                jeu.ajoutCase(nouvelleCase);
                System.out.println(jeu);

            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(IA1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println(wins + " wins out of " + total + " games.");
    }

    //Methode pour le joueur "Max"  cherche le meilleur déplacement
    public int findBestMove(Jeu jeu, int profondeur) throws CloneNotSupportedException {
        Map<String, Object> result = this.minimax(jeu, profondeur, joueur1);
        return (int) result.get("Direction");
    }
//Méthode pour le joueur "Min"  cherche le 

    public Case meilleurCase(Jeu jeu, int profondeur) throws CloneNotSupportedException {
        Map<String, Object> result = this.minimax(jeu, profondeur, joueur2);
        return (Case) result.get("Case");
    }

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
            meilleurScore = this.scoreHeuristique(jeu.getScoreFinal(), jeu.listeCaseVideMultiGrille().size(), jeu.scoreDispersion());
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
                    if (scoreActuelle > meilleurScore) { //maximize score
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
                    if (currentScore < meilleurScore) { //minimize best score
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

    private static int scoreHeuristique(int scoreGeneral, int nbCasesVides, int scoreCases) {
        int score = (int) (scoreGeneral + Math.log(scoreGeneral) * nbCasesVides - scoreCases);
        return Math.max(score, Math.min(scoreGeneral, 1));
    }

}
