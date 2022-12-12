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
    private boolean debug = false;
    private ServerSocket serveur = null;
    private ArrayList<Connexion> clients;
    private boolean competitif = false;
    private int next = -1;
    
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
            if (this.debug) System.out.println("[SERVEUR] Lancement du serveur");
            this.serveur = new ServerSocket(this.PORT, this.LIMIT);
            if (this.debug) System.out.println("[SERVEUR] Serveur lancé à l'adresse " + this.getHost() + ":" + this.PORT);
            
            // Boucle d'attente d'une nouvelle connexion
            while (true) {
                if (this.debug) System.out.println("[SERVEUR] En attente de connexion");
                Socket socket = serveur.accept();
                
                // Connexion d'un nouveau client
                if (this.debug) System.out.println("[SERVEUR] Client connecté");
                Connexion client = new Connexion(socket, this);
                client.setDebug(this.debug);
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
     * Setter pour le débuggage (affichage de requêtes recus)
     * 
     * @param d 
     */
    public void setDebug(boolean d) {
        this.debug = d;
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
    
    /**
     * Détermine le prochain joueur à jouer lors d'une partie coopérative
     * 
     * @return Prochain joueur 
     */
    public Connexion getNextClient() {
        this.next++;
        
        if (this.next >= this.clients.size()) this.next = 0;
        
        return this.clients.get(this.next);
    }
}
