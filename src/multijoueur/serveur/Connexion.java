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
    public void run() {
        try {
            while (true) {
                String ligne = this.in.readLine();
                System.out.println("[SERVEUR] Recu : " + ligne);
                
                if (ligne == null) break;
                
                // Gestion de la commande "aTous"
                if (ligne.startsWith("aTous")) {
                    int premierEspace = ligne.indexOf(" ");
                    envoyerATous(ligne.substring(premierEspace + 1));
                }
                
                this.out.println(ligne);
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

    // Envoie un message à tous les clients autre que celui lancant la commande (FONCTION DE TEST)
    private void envoyerATous(String message) {
        for (Connexion client : this.serveur.getClients()) {
            // Pour les clients autres que celui qui a envoyé le message (equals a implémenter pour aue cela fonctionne)
            if (!client.equals(this)) {
                client.out.println(message);
            }
        }
    }
    
    // Égalité entre deux Connexion (différentes si sockets différents)
    public boolean equals (Connexion c) {
        return this.socket.equals(c.socket);
    }
}
