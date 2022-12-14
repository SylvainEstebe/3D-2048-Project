package ia;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import modele.Jeu;
import modele.ThreadAffichIACons;
import variables.Parametres;

/**
 * Classe qui instancie l'IA qui respecte le deuxième algorithme
 *
 * @author Alexanne
 */
public class IA2 implements Parametres {

    /**
     * Jeu associé à l'IA
     */
    private Jeu jeu;
    /**
     * Indique si l'IA a été stoppée
     */
    private boolean est_stoppe = false;

    /**
     * Constructeur de l'IA
     *
     * @param jeu jeu associé à l'IA
     */
    public IA2(Jeu jeu) {
        this.jeu = jeu;
    }

    /**
     * Méthode qui choisit le mouvement le plus optimal pour le jeu à un certain
     * état
     *
     * @return un entier qui correspond au mouvement qui doit être pris par l'IA.
     */
    public int choixMouvIA2() {
        Jeu jeu_h, jeu_b, jeu_g, jeu_d, jeu_q, jeu_e = new Jeu();
        //Copie du jeu pour simuler des parties
        jeu_h = jeu.clone();
        jeu_b = jeu.clone();
        jeu_g = jeu.clone();
        jeu_d = jeu.clone();
        jeu_q = jeu.clone();
        jeu_e = jeu.clone();

        //Ensemble des scores obtenus pour des parties avec un mouvt de départ
        int[] scores_h = centPartiesRandom(jeu_h, HAUT);
        int[] scores_b = centPartiesRandom(jeu_b, BAS);
        int[] scores_g = centPartiesRandom(jeu_g, GAUCHE);
        int[] scores_d = centPartiesRandom(jeu_d, DROITE);
        int[] scores_q = centPartiesRandom(jeu_q, DESCG);
        int[] scores_e = centPartiesRandom(jeu_e, MONTERG);

        //Moyenne des scores pour un mouvt de départ
        int moy_h = (int) Arrays.stream(scores_h).average().orElse(Double.NaN);
        int moy_b = (int) Arrays.stream(scores_b).average().orElse(Double.NaN);
        int moy_g = (int) Arrays.stream(scores_g).average().orElse(Double.NaN);
        int moy_d = (int) Arrays.stream(scores_d).average().orElse(Double.NaN);
        int moy_q = (int) Arrays.stream(scores_q).average().orElse(Double.NaN);
        int moy_e = (int) Arrays.stream(scores_e).average().orElse(Double.NaN);
        int[] scores_max = {moy_h, moy_b, moy_g, moy_d, moy_q, moy_e};

        //On choisit le mouvement pour lequel le score est le plus optimal 
        int max_score = Arrays.stream(scores_max).max().getAsInt();
        if (max_score == moy_h) {
            return HAUT;
        }
        if (max_score == moy_b) {
            return BAS;
        }
        if (max_score == moy_g) {
            return GAUCHE;
        }
        if (max_score == moy_d) {
            return DROITE;
        }
        if (max_score == moy_q) {
            return DESCG;
        } else {
            return MONTERG;
        }
    }

    /**
     * Méthode qui permet de faire 100 parties en mode random
     *
     * @param jeu le jeu sur lequel on va réaliser les parties
     * @param direction le mouvement au départ de la partie
     * @return un tableau avec les scores obtenus pour chaque partie
     */
    public int[] centPartiesRandom(Jeu jeu, int direction) {
        int[] tab_scores = new int[100];
        for (int i = 0; i < 100; i++) {
            Jeu jeu_copie = jeu.clone();
            tab_scores[i] = partieRandom(jeu_copie, direction);
        }
        return tab_scores;
    }

    /**
     * Méthode qui réalise une partie random
     *
     * @param jeu le jeu sur lequel est effectué la partie
     * @param direction le mouvement qui est fait au début du jeu
     * @return le score obtenu à la fin du jeu
     */
    public int partieRandom(Jeu jeu, int direction) {
        jeu.deplacerCases3G(direction);
        boolean b = false;
        while (!jeu.finJeu()) {
            b = jeu.mouvementAlea();
            jeu.choixNbCasesAjout(b);
            jeu.majScore();
        }
        return jeu.getScoreFinal();
    }

    /**
     * Méthode qui fait fonctionner l'IA
     */
    public void jeuIA2() {
        //Thread qui permet l'affichage de l'IA en console
        ThreadAffichIACons stopia = new ThreadAffichIACons();
        stopia.start();

        int direction;
        //Fonctionnement de l'IA tant que le jeu n'est pas fini ou la case
        while (!jeu.finJeu() && !est_stoppe) {
            direction = choixMouvIA2();
            jeu.enregistrement();
            boolean b2 = jeu.deplacerCases3G(direction);
            jeu.choixNbCasesAjout(b2);
            System.out.println(jeu.toString());
            System.out.println("Tapez 's' puis Entree pour stopper l'IA");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Jeu.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (b2) {
                jeu.validerEnregistrement();
            } else {
                jeu.annulerEnregistrement();
            }
            jeu.majScore();
            jeu.resetFusion();
            if (!stopia.isAlive()) {
                est_stoppe = true;
            }
        }
    }

}
