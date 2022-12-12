package application;

import ia.IA2;
import java.util.TimerTask;
import static variables.Parametres.OBJECTIF;

/**
 * Classe qui réalisé l'affichage de l'IA de manière dynamique
 * @author Alexanne
 */
public class IAThreadApp extends TimerTask {
    private FXMLController controleur;
    private int algo;
    
    
    public IAThreadApp(FXMLController controleur, int algo){
        this.controleur=controleur;
        this.algo=algo;
       
    }
    
    public void run(){
        int direction;
        if(algo==2){
            IA2 ai_algo2=new IA2(controleur.getJeuAppli());
            direction=ai_algo2.choixMouvIA2();
        }
        else{
            direction=-2;
        }
        
        boolean b2 = controleur.getJeuAppli().deplacerCases3G(direction);
        controleur.deplacementThread(direction, b2);
        if (controleur.getJeuAppli().finJeu()) {
            if (controleur.getJeuAppli().getValeurMaxJeu() >= OBJECTIF) {
                controleur.victoireAppli();
            } else {
                controleur.jeuPerduAppli();
            }
        }
    }
    
}
