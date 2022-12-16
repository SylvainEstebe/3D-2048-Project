
package Main;

import application.NewFXMain;
import modele.MainConsole;

/**
 * Classe qui permet de démarrer le jeu soit en console, soit dans l'interface
 * @author Alexanne
 */
public class Main {
    /**
     * Méthode qui démarre le jeu
     * @param args s'il y en a, le jeu démarre avec l'interface graphique. Sinon, en console
     */
    public static void main(String[] args) {
       if(args.length==0){
           MainConsole.main(args);
       }
       else{
           System.out.println("Lancement du jeu avec l'interface graphique");
           NewFXMain.main(args);
       }
    }
}
