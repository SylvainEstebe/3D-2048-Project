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
    
    // Données de base
    private String pseudo;
    private boolean pret = false;
    
    // Données de parties compétitives
    private int score = 0;
    private int valeurMax = 0;
    private boolean victoire = false;
    private int classement = 0;
    private boolean defaite = false;

    /**
     * Constructeur
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
                    this.envoyerATous(this.escapeRequest(ligne));
                } else if (ligne.startsWith(Routes.VERIF_PSEUDO)) { // Vérification disponibilité pseudo
                    this.pseudoDispo(this.escapeRequest(ligne));
                } else if (ligne.startsWith(Routes.ENREG_PSEUDO)) { // Enregistrement pseudo
                    this.enregistrerJoueur(this.escapeRequest(ligne));
                } else if (ligne.equals(Routes.PRET_A_COMMENCER)) { // Joueur prêt
                    this.setJoueurPret(true);
                } else if (ligne.equals(Routes.JOUEURS_PRETS)) { // Vérifications que tous les joueurs sont prêts
                    this.joueursPrets();
                } else if (ligne.equals(Routes.COMMENCER_PARTIE)) { // Lancement de la partie
                    this.commencerPartie();
                } else if (ligne.startsWith(Routes.ENVOYER_SCORE)) { // Envoi du score
                    this.scoreEnvoye(this.escapeRequest(ligne));
                } else if (ligne.equals(Routes.VICTOIRE_VERSUS)) { // Victoire en compétitif
                    this.victoire();
                } else if (ligne.equals(Routes.DEFAITE_VERSUS)) { // Défaite en compétitif
                    this.defaite();
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
     * 
     * @param p Pseudo 
     */
    private void enregistrerJoueur(String p) {
        this.pseudo = p;
        
        this.out.println(p + " enregistré");
    }
    
    /**
     * Setter pour le statut prêt du joueur
     * 
     * @param p Statut prêt
     */
    private void setJoueurPret(boolean p) {
        this.pret = p;
    }
    
    /**
     * Vérifie que tous les joueurs sont prêts
     */
    private void joueursPrets() {
        this.pret = true;
        
        boolean prets = true;
        
        for (Connexion client : this.serveur.getClients()) {
            if (prets && !client.pret) prets = false;
        }
        
        this.out.println(Routes.JOUEURS_PRETS + " " + prets);
    }
    
    /**
     * Lance une partie multijoueur après confirmation 
     */
    private void commencerPartie() {
        for (Connexion client : this.serveur.getClients()) {
            client.out.println(Routes.COMMENCER_PARTIE + " " + this.serveur.estCompetitif());
        }
    }
    
    /**
     * Enregistre le score envoyé par le client et transmet l'information aux autres joueurs
     * 
     * @param data Données envoyées par le client 
     */
    private void scoreEnvoye(String data) {
        int splitter = data.indexOf("|");
        this.score = Integer.parseInt(data.substring(0, splitter));
        this.valeurMax = Integer.parseInt(data.substring(splitter + 1));
        this.envoyerATous(Routes.ENVOYER_SCORE + " " + this.pseudo + "#" + this.score + "|" + this.valeurMax);
    }
    
    /**
     * Enregistre la victoire, détermine le classement et transmet l'information aux autres joueurs et donne le classement au joueur concerné
     */
    private void victoire() {
        this.victoire = true;
        
        // Calcul du classement
        for (Connexion client : this.serveur.getClients()) {
            if (client.victoire) this.classement++;
        }
        
        this.envoyerATous(Routes.VICTOIRE_VERSUS_AUTRE + " " + this.pseudo + "#" + this.classement);
        this.out.println(Routes.VICTOIRE_VERSUS + " " + this.classement);
    }
    
    /**
     * Enregistre la défaite, détermine le classement et transmet l'information aux autres joueurs et donne le classement au joueur concerné
     */
    private void defaite() {
        this.defaite = true;
        
        this.classement = this.serveur.getClients().size() + 1;
        
        // Calcul du classement
        for (Connexion client : this.serveur.getClients()) {
            if (client.defaite) this.classement--;
        }
        
        this.envoyerATous(Routes.DEFAITE_VERSUS_AUTRE + " " + this.pseudo + "#" + this.classement);
        this.out.println(Routes.DEFAITE_VERSUS + " " + this.classement);
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
     * Getter du score du client
     * 
     * @return Score 
     */
    public int getScore() {
        return this.score;
    }
    
    /**
     * Getter de la valeur max du client
     * 
     * @return Valeur max 
     */
    public int getValeurMax() {
        return this.valeurMax;
    }
    
    /**
     * Vérifie l'égalite avec une autre connexion (vérification de l'égalité entre les sockets)
     * 
     * @param c Autre connexion
     * @return true si égaux, false sinon
     */
    public boolean equals(Connexion c) {
        return this.socket.equals(c.socket);
    }
    
    /**
     * Supprime l'entête de la requête
     * 
     * @param request Requête
     * @return Données de la requête sans l'entête
     */
    private String escapeRequest(String request) {
        int premierEspace = request.indexOf(" ");
        return request.substring(premierEspace + 1);
    }
}
