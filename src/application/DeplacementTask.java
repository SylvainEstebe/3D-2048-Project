package application;

import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.Pane;
import variables.Parametres;
import static variables.Parametres.BAS;
import static variables.Parametres.HAUT;

/**
 * Thread qui permet de déplacer une case du jeu de manière fluide sur
 * l'application
 *
 * @author Alexanne
 */
public class DeplacementTask extends Task<Void> implements Parametres {

    /**
     * Compteur de thread de début
     */
    private CountDownLatch debut;
    /**
     * Compteur de thread à la fin
     */
    private CountDownLatch fin;
    /**
     * Coordonnée abscisse de la case à bouger
     */
    private int x = 0;
    /**
     * Ordonnée de la case à bouger
     */
    private int y = 0;
    /**
     * Déplacement objectif de la case
     */
    private int deplObj = 0;
    /**
     * Le pane à bouger sur l'interface correspondant à la case
     */
    Pane caseABougeGraph;
    /**
     * La direction dans laquelle doit aller la case
     */
    int direction;

    /**
     * Le contrôleur relié à la case
     */
    private FXMLController controleur;

    /**
     * Constructeur du thread
     *
     * @param x abscisse de la case
     * @param y ordonnée de la case
     * @param deplobj le déplacement objectif de la case
     * @param caseABougeGraph le pane à déplacer correspondant à la case
     * @param d la direction dans laquelle doit aller la case
     * @param c le controleur relié à la case
     */
    public DeplacementTask(int x, int y, int deplobj, Pane caseABougeGraph, int d, FXMLController c) {
        this.x = x;
        this.y = y;
        this.deplObj = deplobj;
        this.caseABougeGraph = caseABougeGraph;
        this.direction = d;
        this.controleur = c;

    }

    /**
     * Changer les paramètres du compteur de fin de thread
     *
     * @param f le nombre de threads restant à compter à la fin
     */
    public void setFin(CountDownLatch f) {
        this.fin = f;
    }

    /**
     * Changer les paramètres du compteur de début de thread
     *
     * @param d le nombre de threads restant à compter au début
     */
    public void setDebut(CountDownLatch d) {
        this.debut = d;
    }

    @Override
    /**
     * Réalise le déplacement dynamique de la case et fait la mise à jour de son
     * déplacement.
     *
     * @throws Exception exception générale.
     */
    protected Void call() throws Exception {
        int cord;
        cord = 0;
        debut.await();
        if (direction == HAUT || direction == BAS) {
            cord = y;
        } else {
            cord = x;
        }
        while (cord != deplObj) { // si la tuile n'est pas à la place qu'on souhaite attendre en abscisse
            if (cord < deplObj) {
                cord += 1; // si on va vers la droite, on modifie la position de la tuile pixel par pixel vers la droite
            } else {
                cord -= 1; // si on va vers la gauche, idem en décrémentant la valeur de x
            }
            if (direction == HAUT || direction == BAS) {
                y = cord;
            } else {
                x = cord;
            }
            // Platform.runLater est nécessaire en JavaFX car la GUI ne peut être modifiée que par le Thread courant, contrairement à Swing où on peut utiliser un autre Thread pour ça
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //javaFX operations should go here
                    caseABougeGraph.relocate(x, y); // on déplace la tuile d'un pixel sur la vue, on attend 5ms et on recommence jusqu'à atteindre l'objectif
                    caseABougeGraph.setVisible(true);

                }
            }
            );
            Thread.sleep(1);
        } // end while        
        fin.countDown();
        fin.await();
        Thread th = new Thread((new MajJeu(controleur)));
        th.start();
        return null;
    }

}
