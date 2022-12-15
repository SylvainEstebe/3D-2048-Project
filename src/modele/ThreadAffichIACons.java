package modele;

import java.util.Scanner;
import variables.Parametres;

/**
 * Thread qui permet l'affichage de l'IA en console
 * @author Alexanne
 */
public class ThreadAffichIACons extends Thread implements Parametres{
    
    /**
     * Constructeur vide
     */
    public ThreadAffichIACons(){
        
    }

    /**
     * Méthode qui permet de faire fonctionner l'IA en console 
     * tant que la touche s n'est pas enfoncée
     */
    @Override
    public void run() {
        Scanner sc1 = new Scanner(System.in);
        String s=sc1.next();
        s = s.toLowerCase();
        boolean est_stoppe=false;
        while (!est_stoppe) {
            s=sc1.next();
            if (s.equals("s")) {
                est_stoppe=true; 
            }
        }
    }  
}
