/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import modele.Case;
import modele.Jeu;
import static variables.Parametres.TAILLE;

/**
 *
 * @author Mouna
 */
public class MajJeu extends Task<Void> {

    private FXMLController controleur;

    public MajJeu(FXMLController c) {
        this.controleur = c;
    }

    @Override
    protected Void call() throws Exception {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
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
        }
        );
        return null;

    }
}
