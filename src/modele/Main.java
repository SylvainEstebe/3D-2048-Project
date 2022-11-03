package modele;

import variables.Parametres;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import modele.Jeu;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe principale qui lance le programme
 *
 * @author Alexanne WORM
 */
public class Main implements Parametres {

    public static void main(String[] args) {
       boolean existe = false; //variable si il existe une patie précédente non finie = true sinon =false
        ObjectInputStream ois = null;
        try {
            //verifier s'il existe un fichier: une partie précédente
            FileInputStream fichierIn = new FileInputStream("jeu.ser");
            
            existe = (fichierIn != null);

        } catch (FileNotFoundException ex) {
        }
        
        if (existe == false) { //il n'y a pas une partie enregistrée -->  on lance une nouvelle 
            Jeu j = new Jeu();
            
            j.lancementJeuConsole();
        } else {//il y a une partie précédente 
            try {
                
                final FileInputStream fichierIn = new FileInputStream("jeu.ser");
                ois = new ObjectInputStream(fichierIn);
                Jeu jeu = (Jeu) ois.readObject();
                if (jeu.rechargerPartie()) {// si le joueur veut continuer la partie précédente
                    jeu.lancementJeuConsole();
                } else {//si le joueur abandonne la partie précédente et commence  à nouveau 
                    Jeu j = new Jeu();
                    j.lancementJeuConsole();
                }

            } catch (final java.io.IOException e) {
                e.printStackTrace();
            } catch (final ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

}
