/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multijoueur.client;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import modele.Jeu;

/**
 * Client d'un serveur de jeu
 *
 * @author Manon
 */
public class Client implements Runnable {
    private final String ADDRESSE;
    private final int PORT;
    private Jeu jeu;
    private boolean estServeur;
    private Socket socket;
    private Connexion connexion;
    
    /**
     * Constructeur
     * 
     * @param a Adresse du serveur auquel se connecter
     * @param p Port du serveur auquel se connecter
     * @param estS Client hôte ou non
     */
    public Client(String a, int p, boolean estS) {
        this.ADDRESSE = a;
        this.PORT = p;
        this.estServeur = estS;
    }

    @Override
    /**
     * Lancement du client
     */
    public void run() {
        try {
            System.out.println("[CLIENT] Connexion au serveur " + this.ADDRESSE + ":" + this.PORT);
            this.socket = new Socket(this.ADDRESSE, this.PORT);
            System.out.println("[CLIENT] Connecté au serveur");
            
            this.connexion = new Connexion(this.socket, this);
            
            // Lancement de l'écoute
            new Thread(this.connexion).start();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Fermeture du client
     * 
     * @throws IOException 
     */
    protected void closeClient() throws IOException {
        this.socket.close();
    }
    
    /**
     * Setter pour le jeu du client
     * 
     * @param j Jeu 
     */
    public void setJeu(Jeu j) {
        this.jeu = j;
    }
    
    /**
     * Getter pour le jeu du client
     * 
     * @return Jeu 
     */
    protected Jeu getJeu() {
        return this.jeu;
    }
    
    /**
     * Getter pour le statut hôte du client
     * 
     * @return Statut hôte du client 
     */
    protected boolean estServeur() {
        return this.estServeur;
    }
    
    /**
     * Getter de la connexion
     * 
     * @return Connexion 
     */
    public Connexion getConnexion() {
        return this.connexion;
    }
}