/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multijoueur.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client d'un serveur de jeu
 *
 * @author Manon
 */
public class Client implements Runnable {
    private final String ADDRESSE;
    private final int PORT;
    private Socket socket;
    private Connexion connexion;
    
    /**
     * Constructeur
     * 
     * @param a Adresse du serveur auquel se connecter
     * @param p Port du serveur auquel se connecter
     */
    public Client(String a, int p) {
        this.ADDRESSE = a;
        this.PORT = p;
    }

    @Override
    public void run() {
        try {
            System.out.println("[CLIENT] Conexion au serveur " + this.ADDRESSE + ":" + this.PORT);
            this.socket = new Socket(this.ADDRESSE, this.PORT);
            System.out.println("[CLIENT] Connecté au serveur");
            
            this.connexion = new Connexion(this.socket);
            
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            
            // Lancement de l'écoute
            new Thread(this.connexion).start();
            
            // Saisie des commandes
            Scanner sc = new Scanner(System.in);
            while(true) {
                System.out.println("Commande :");
                
                String commande = sc.nextLine();
                
                if (commande.equals("quitter")) break;
                out.println(commande);
            }
            
            this.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
