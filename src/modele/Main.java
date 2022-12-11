package modele;

import ia.IA1;
import java.io.File;
import variables.Parametres;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import multijoueur.Routes;
import multijoueur.client.Client;
import multijoueur.serveur.Serveur;

/**
 * Classe principale 
 * qui lance le programme
 *
 * @author Alexanne WORM
 */
public class Main implements Parametres {

    public static void main(String[] args) {
        boolean multi = false;
        boolean estServeur = false;
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Voulez-vous jouer en multijoueur ? (oui/non)");
        
        String s = sc.nextLine();
        s = s.toLowerCase();
        while (!(s.equals("oui") || s.equals("non"))) {
            System.out.println("oui ou non ?");
            s = sc.nextLine();
            s = s.toLowerCase();
        }
        
        if (s.equals("oui")) {
            multi = true;
            Serveur serveur = null;
            
            System.out.println("Voulez-vous être hôte de la partie ? (oui/non)");
        
            s = sc.nextLine();
            s = s.toLowerCase();
            while (!(s.equals("oui") || s.equals("non"))) {
                System.out.println("oui ou non ?");
                s = sc.nextLine();
                s = s.toLowerCase();
            }
            
            String adresse = null;
            int port = 0;
            if (s.equals("oui")) {
                estServeur = true;
                // Lancement du serveur
                serveur = new Serveur(3333, 4);
                new Thread(serveur).start();
                
                // Attente d'une seconde pour que le serveur ait le temps de se lancer (trouver autre chose de mieux)
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                adresse = serveur.getHost();
                port = serveur.getPort();
                
                System.out.println("Partie compétitive ? (oui/non)");
        
                s = sc.nextLine();
                s = s.toLowerCase();
                while (!(s.equals("oui") || s.equals("non"))) {
                    System.out.println("oui ou non ?");
                    s = sc.nextLine();
                    s = s.toLowerCase();
                }
                
                serveur.setCompetitif(s.equals("oui"));
            } else {
                System.out.println("Adresse du serveur :");
                adresse = sc.nextLine();
                
                System.out.println("Port du serveur :");
                port = sc.nextInt();
            }
            
            // Lancement du client
            Client client = new Client(adresse, port, estServeur);
            Jeu jeu = new Jeu(client, multi);
            client.setJeu(jeu);
            new Thread(client).start();
            
            // Enregistrement du pseudo
            System.out.println("Votre pseudo ?");
            s = sc.nextLine();
            // Tant que la saisie est vide ou que le pseudo n'est pas disponible
            while (s == null || s.equals("") || !client.getConnexion().enregistrerJoueur(s)) {
                System.out.println("Veuillez saisir votre pseudo");
                s = sc.nextLine();
            }
            
            // Démarrage de la partie
            System.out.println("Taper \"" + Routes.COMMENCER_PARTIE + "\" lorsque vous serez prêt à commencer la partie");
                
            String commande = sc.nextLine();
            // Tant que la commande est incorrecte
            while (!commande.equals(Routes.COMMENCER_PARTIE)) {
                commande = sc.nextLine();
            }
            boolean result = client.getConnexion().lancementPartie();
            // Si le client est hôte et tant que les autres joueurs ne sont pas prêt, attente
            while (estServeur && !result) {
                System.out.println("Veuillez patienter, tous les joueurs ne sont pas prêts");
                commande = sc.nextLine();
                while (!commande.equals(Routes.COMMENCER_PARTIE)) {
                    commande = sc.nextLine();
                }
                result = client.getConnexion().lancementPartie();
            }
            
            if (estServeur) {
                System.out.println("La partie commence");
            } else {
                System.out.println("La partie va bientôt commencer");
            }
        } else {
            File save = new File("jeu.ser");
            
            // Vérification de l'existence d'une sauvegarde
            if (save.isFile() && save.canRead()) {
                try {
                    // Lecture de la sauvegarde
                    final FileInputStream fichierIn = new FileInputStream(save); 
                    ObjectInputStream ois;
                    try {
                        // Déserialisation
                        ois = new ObjectInputStream(fichierIn);
                        Jeu jeu = (Jeu) ois.readObject();
                        
                        if (jeu.rechargerPartie()) {// si le joueur veut continuer la partie précédente
                            new Thread(jeu).start();
                        } else {//si le joueur abandonne la partie précédente et commence  à nouveau 
                            Jeu j = new Jeu();
                            new Thread(j).start();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else { // Lancement d'une nouvelle partie en l'absence d'une sauvegarde
                Jeu j = new Jeu();
                new Thread(j).start();
            }
        }
    }
}
