/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multijoueur.serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import multijoueur.Routes;

/**
 * Connexion d'un client au serveur
 * Gestion des requêtes coté serveur
 * 
 * @author Manon
 */
public class Connexion implements Runnable {
    private Socket socket;
    private Serveur serveur;
    private BufferedReader in;
    private PrintWriter out;
    
    private String pseudo;

    /**
     * Consructeur
     * 
     * @param s Socket de la connexion entre le client et le serveur
     * @param srv Serveur
     */
    public Connexion(Socket s, Serveur srv) {
        this.socket = s;
        this.serveur = srv;
        
        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    /**
     * Lancement de la connexion
     */
    public void run() {
        try {
            while (true) {
                String ligne = this.in.readLine();
                System.out.println("[SERVEUR] Recu : " + ligne);
                
                if (ligne == null) break;
                
                if (ligne.startsWith(Routes.CHAT)) { // Message
                    int premierEspace = ligne.indexOf(" ");
                    this.envoyerATous(ligne.substring(premierEspace + 1));
                } else if (ligne.startsWith(Routes.VERIF_PSEUDO)) { // Vérification disponibilité pseudo
                    int premierEspace = ligne.indexOf(" ");
                    this.pseudoDispo(ligne.substring(premierEspace + 1));
                } else if (ligne.startsWith(Routes.ENREG_PSEUDO)) { // Enregistrement pseudo
                    int premierEspace = ligne.indexOf(" ");
                    this.enregistrerJoueur(ligne.substring(premierEspace + 1));
                } else {
                    this.out.println(ligne);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                this.socket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Envoie un message à tous les autres clients
     * 
     * @param message Message à envoyer  
     */
    private void envoyerATous(String message) {
        for (Connexion client : this.serveur.getClients()) {
            // Pour les clients autres que celui qui a envoyé le message (equals a implémenter pour aue cela fonctionne)
            if (!client.equals(this)) {
                client.out.println(message);
            }
        }
    }
    
    /**
     * Vérifie la disponibilité d'un pseudo
     * @param p 
     */
    private void pseudoDispo(String p) {
        boolean dispo = true;
        
        for (Connexion client : this.serveur.getClients()) {
            if (dispo && client.pseudo != null && client.pseudo.toLowerCase().equals(p.toLowerCase())) dispo = false;
        }
        
        this.out.println(Routes.VERIF_PSEUDO + " " + dispo);
    }
    
    /**
     * Enregistre le pseudo du joueur associe a cette connexion
     * @param p 
     */
    private void enregistrerJoueur(String p) {
        this.pseudo = p;
        
        this.out.println(p + " enregistré");
    }
    
    /**
     * Getter du pseudo
     * 
     * @return Pseudo du joueur associé à cette connexion
     */
    public String getPseudo() {
        return this.pseudo;
    }
    
    /**
     * Vérifie l'égalite avec une autre connexion (vérification de l'égalité entre les sockets)
     * 
     * @param c Autre connexion
     * @return true si égaux, false sinon
     */
    public boolean equals (Connexion c) {
        return this.socket.equals(c.socket);
    }
}
