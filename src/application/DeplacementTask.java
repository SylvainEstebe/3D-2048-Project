
package application;

import static java.lang.Thread.State.TERMINATED;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import modele.Case;
import modele.Jeu;
import variables.Parametres;
import static variables.Parametres.BAS;
import static variables.Parametres.HAUT;
import static variables.Parametres.TAILLE;

/**
 *
 * @author Alexanne
 */
public class DeplacementTask extends Task<Void> implements Parametres {

    private CountDownLatch debut;
    private CountDownLatch fin;
    private int x = 0;
    private int y = 0;
    private int deplObj = 0;
    Pane caseABougeGraph;
    int direction;
    private FXMLController controleur;
    Pane fondGrille;
    int cord;

    public DeplacementTask(int x, int y, int deplobj, Pane caseABougeGraph, int d, FXMLController c) {
        this.x = x;
        this.y = y;
        this.deplObj = deplobj;
        this.caseABougeGraph = caseABougeGraph;
        this.direction = d;
        this.controleur = c;

    }

    public void setFin(CountDownLatch f) {
        this.fin = f;
    }

    public void setDebut(CountDownLatch d) {
        this.debut = d;
    }

    @Override
    protected Void call() throws Exception {
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
