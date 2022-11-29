/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multijoueur.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import multijoueur.Routes;

/**
 * Connexion du serveur au client
 * Gestion des requêtes coté client
 *
 * @author Manon
 */
public class Connexion implements Runnable {
    private Socket socket;
    private Client client;
    private BufferedReader in;
    private PrintWriter out;
    
    private boolean pseudoDispo = false;
    private String pseudo;
    
    /**
     * Constructeur
     * 
     * @param s Socket de la connexion entre le serveur et le client
     * @throws IOException 
     */
    public Connexion(Socket s, Client c) throws IOException {
        this.socket = s;
        this.client = c;
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
    }
    
    @Override
    /**
     * Lancement de la connexion
     */
    public void run() {
        try {
            while (true) {
                String reponse = this.in.readLine();
                
                System.out.println("[CLIENT] Le serveur dit : " + reponse);
                
                if (reponse.startsWith(Routes.VERIF_PSEUDO)) {
                    this.setPseudoDispo(reponse);
                }
                
                // Le serveur n'envoie plus rien
                if (reponse == null) break;
            }
        } catch (IOException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
                out.close();
                this.client.closeClient();
            } catch (IOException ex) {
                Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Définit la disponibilité du pseudo
     * 
     * @param message Réponse du serveur 
     */
    private synchronized void setPseudoDispo(String message) {
        this.pseudoDispo = message.equals(Routes.VERIF_PSEUDO + " true");
        this.notify();
    }
    
    /**
     * Getter/méthode pour vérifier la disponibilité d'un pseudo
     * 
     * @param p Pseudo
     * @return true si disponible, false sinon
     */
    private synchronized boolean pseudoDispo(String p) {
        this.out.println(Routes.VERIF_PSEUDO + " " + p);
        
        try {
            wait();            
        } catch (InterruptedException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return this.pseudoDispo;
    }
    
    /**
     * Enregistre le joueur
     * 
     * @param p Pseudo du joueur
     * @return true si enregistré, false sinon
     */
    public boolean enregistrerJoueur(String p) {
        if (pseudoDispo(p)) {
            this.pseudo = p;
            this.out.println(Routes.ENREG_PSEUDO + " " + p);
            return true;
        }
        return false;
    }
    
    /**
     * Getter du pseudo
     * 
     * @return Pseudo du joueur 
     */
    public String getPseudo() {
        return this.pseudo;
    }
}
