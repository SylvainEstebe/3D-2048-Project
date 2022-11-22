
package application;

import javafx.application.Platform;
import javafx.concurrent.Task;

import javafx.scene.layout.Pane;

/**
 *
 * @author Alexanne
 */
public class DeplacementTask extends Task<Void>{
    private int x=0;
    private int y=0;
    private int deplObj=0;
    Pane caseABougeGraph;
    
    public DeplacementTask(int x,int y,int deplobj, Pane caseABougeGraph){
        this.x=x;
        this.y=y;
        this.deplObj=deplobj;
        this.caseABougeGraph =caseABougeGraph;
    }

    @Override
    protected Void call() throws Exception {
     
        while (x != deplObj) { // si la tuile n'est pas à la place qu'on souhaite attendre en abscisse
            if (x < deplObj) {

                x+= 1; // si on va vers la droite, on modifie la position de la tuile pixel par pixel vers la droite
            } else {
                x -= 1; // si on va vers la gauche, idem en décrémentant la valeur de x
            }
            // Platform.runLater est nécessaire en JavaFX car la GUI ne peut être modifiée que par le Thread courant, contrairement à Swing où on peut utiliser un autre Thread pour ça
            Platform.runLater(new Runnable() { // classe anonyme
                @Override
                public void run() {
                    //javaFX operations should go here
                    caseABougeGraph.relocate(x, y); // on déplace la tuile d'un pixel sur la vue, on attend 5ms et on recommence jusqu'à atteindre l'objectif
                    caseABougeGraph.setVisible(true);
                    //System.out.println(caseABougeGraph.getLayoutX());
                }
                
            }
            );
            Thread.sleep(5);
        } // end while
        return null;
    }   
       
}
