/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    static CountDownLatch latchToWaitForJavaFx = null;
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

    @Override
    protected Void call() throws Exception {
        cord = 0;
        System.out.println( latchToWaitForJavaFx);
        if (latchToWaitForJavaFx == null) {
            System.out.println(controleur.getDeplThread().size());
            latchToWaitForJavaFx = new CountDownLatch(controleur.getDeplThread().size());
        }
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
            Platform.runLater(new Runnable() { // classe anonyme
                @Override
                public void run() {
                    //javaFX operations should go here
                    caseABougeGraph.relocate(x, y); // on déplace la tuile d'un pixel sur la vue, on attend 5ms et on recommence jusqu'à atteindre l'objectif
                    caseABougeGraph.setVisible(true);

                }
            }
            );
            Thread.sleep(2);

        } // end while
        latchToWaitForJavaFx.countDown();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                   // System.out.println( latchToWaitForJavaFx);
                    //latchToWaitForJavaFx.
                    if (controleur.getNbTasks() == controleur.getCompteurTasks()) {                       
                        //  latchToWaitForJavaFx.await();
                        Jeu jeuAppli = controleur.getJeuAppli();
                        controleur.resetFondGrille();
                        controleur.resetEltsGrilles();
                        //mise à jour des cases
                        for (int k = 0; k < TAILLE; k++) {
                            for (int i = 0; i < TAILLE; i++) {
                                for (int j = 0; j < TAILLE; j++) {
                                    Case caseModele = jeuAppli.getGrilles().get(k).getGrille().get(i).get(j);
                                    if (caseModele.getValeur() != 0) {
                                        Label caseJeu = new Label("" + caseModele.getValeur());
                                        caseJeu.getStyleClass().add("caseJeu");
                                        Pane caseJeuCouleur = new Pane();
                                        caseJeuCouleur.getChildren().add(caseJeu);
                                        caseJeuCouleur.getStyleClass().add("couleurCase");
                                        controleur.getEltsGrilles().add(caseJeuCouleur);
                                        controleur.couleursCases(caseJeuCouleur, caseModele.getValeur());
                                        controleur.positionPane(caseJeuCouleur, caseModele, caseJeu, k);
                                        controleur.getFondGrille().getChildren().add(caseJeuCouleur);

                                    }
                                }
                            }
                        }
                  
                    }
                    //latchToWaitForJavaFx.countDown();
                    //  System.out.println(latchToWaitForJavaFx);
                  //  latchToWaitForJavaFx = null;

            }
        }
        );
        //  latchToWaitForJavaFx.await();
        return null;
    }

}
