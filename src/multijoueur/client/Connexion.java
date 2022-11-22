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

/**
 * Connexion du serveur au client
 * Gestion des requêtes coté client
 *
 * @author Manon
 */
public class Connexion implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Constructeur
     * 
     * @param s Socket de la connexion entre le serveur et le client
     * @throws IOException 
     */
    public Connexion(Socket s) throws IOException {
        this.socket = s;
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
    }
    
    @Override
    public void run() {
        try {
            while(true) {
                String reponse = this.in.readLine();
                
                // Le serveur n'envoie plus rien
                if (reponse == null) break;
                
                System.out.println("[CLIENT] Le serveur dit : " + reponse);
            }
        } catch (IOException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
