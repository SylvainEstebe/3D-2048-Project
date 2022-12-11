/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multijoueur.serveur;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Serveur de jeu
 *
 * @author Manon
 */
public class Serveur implements Runnable {
    private final int PORT;
    private final int LIMIT;
    private ServerSocket serveur = null;
    private ArrayList<Connexion> clients;
    private boolean competitif = false;
    
    /**
     * Constructeur
     * 
     * @param p Port sur lequel lancer le serveur
     * @param l Limite de clients (de joueurs)
     */
    public Serveur(int p, int l) {
        this.PORT = p;
        this.LIMIT = l;
        
        this.clients = new ArrayList<>();
    }
    
    @Override
    /**
     * Lancement du serveur
     */
    public void run() {
        try {
            System.out.println("[SERVEUR] Lancement du serveur");
            this.serveur = new ServerSocket(this.PORT, this.LIMIT);
            System.out.println("[SERVEUR] Serveur lancé à l'adresse " + this.getHost() + ":" + this.PORT);
            
            // Boucle d'attente d'une nouvelle connexion
            while (true) {
                System.out.println("[SERVEUR] En attente de connexion");
                Socket socket = serveur.accept();
                
                // Connexion d'un nouveau client
                System.out.println("[SERVEUR] Client connecté");
                Connexion client = new Connexion(socket, this);
                clients.add(client);
                
                new Thread(client).start();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Getter de l'hôte
     * 
     * @return Hôte du serveur (adresse IP)
     */
    public String getHost() {
        if (serveur == null) return null;
        
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Serveur.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * Getter du port
     * 
     * @return Port du serveur 
     */
    public int getPort() {
        return this.PORT;
    }
    
    /**
     * Getter des clients connectés au serveur
     * 
     * @return Clients connectés au serveur 
     */
    public ArrayList<Connexion> getClients() {
        return this.clients;
    }
    
    /**
     * Setter du mode compétitif
     * 
     * @param c Mode compétitif activé ou non 
     */
    public void setCompetitif(boolean c) {
        this.competitif = c;
    }
    
    /**
     * Getter du mode compétitif
     * 
     * @return Mode compétitif 
     */
    public boolean estCompetitif() {
        return this.competitif;
    }
}
