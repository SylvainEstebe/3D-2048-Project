package modele;

import variables.Parametres;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import multijoueur.client.Client;
import multijoueur.serveur.Serveur;

/**
 * Classe principale qui lance le programme
 *
 * @author Alexanne WORM
 */
public class Main implements Parametres {

    public static void main(String[] args) {
        
        // TEST MULTI
        Scanner sc = new Scanner(System.in);
        System.out.println("Voulez-vous jouer en multijoueur ? (oui/non)");
        
        String s = sc.nextLine();
        s = s.toLowerCase();
        while(!(s.equals("oui") || s.equals("non"))) {
            System.out.println("oui ou non ?");
            s = sc.nextLine();
            s = s.toLowerCase();
        }
        
        if (s.equals("oui")) {
            System.out.println("Voulez-vous être hôte de la partie ? (oui/non)");
        
            s = sc.nextLine();
            s = s.toLowerCase();
            while(!(s.equals("oui") || s.equals("non"))) {
                System.out.println("oui ou non ?");
                s = sc.nextLine();
                s = s.toLowerCase();
            }
            
            String adresse = null;
            int port = 0;
            if (s.equals("oui")) {
                // Lancement du serveur
                Serveur serveur = new Serveur(3333, 4);
                new Thread(serveur).start();
                
                // Attente d'une seconde pour que le serveur ait le temps de se lancer (trouver autre chose de mieux)
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                adresse = serveur.getHost();
                port = serveur.getPort();
            } else {
                System.out.println("Adresse du serveur :");
                adresse = sc.nextLine();
                
                System.out.println("Port du serveur :");
                port = sc.nextInt();
            }
            
            // Lancement du client
            Client client = new Client(adresse, port);
            new Thread(client).start();
        }
        
        
        
        
        
        
        // FIN TEST MULTI
        
        
        /*
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
        }*/

    }

}
