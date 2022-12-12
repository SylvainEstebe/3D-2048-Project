package application;

import ia.IA1;
import ia.IA2;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import modele.Case;
import static variables.Parametres.OBJECTIF;

/**
 * Classe qui réalisé l'affichage de l'IA de manière dynamique
 *
 * @author Alexanne
 */
public class IAThreadApp extends TimerTask {

    private FXMLController controleur;
    private int algo;

    public IAThreadApp(FXMLController controleur, int algo) {
        this.controleur = controleur;
        this.algo = algo;
    }

    @Override
    public void run() {
        int direction;
        boolean b2;
        switch (algo) {
            case 2:
                IA2 ai_algo2 = new IA2(controleur.getJeuAppli());
                direction = ai_algo2.choixMouvIA2();
                b2 = controleur.getJeuAppli().deplacerCases3G(direction);
                controleur.deplacementThread(direction, b2, false);
                break;
            case 1:
                IA1 ia1 = new IA1(controleur.getJeuAppli());
                int profondeur = 5;
                try {

                    System.out.println(controleur.getJeuAppli());
                    direction = ia1.meilleurMouvement(controleur.getJeuAppli(), profondeur);
                    Case nouvelleCase = ia1.meilleurCase(controleur.getJeuAppli(), profondeur);
                    b2 = controleur.getJeuAppli().deplacerCases3G(direction);
                    controleur.deplacementThread(direction, b2, true);
                    controleur.getJeuAppli().ajoutCase(nouvelleCase);
                    break;
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(IAThreadApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            default:
                direction = -2;
                break;
        }
        if (controleur.getJeuAppli().finJeu()) {
            if (controleur.getJeuAppli().getValeurMaxJeu() >= OBJECTIF) {
                controleur.victoireAppli();
            } else {
                controleur.jeuPerduAppli();
            }
        }
    }

}
