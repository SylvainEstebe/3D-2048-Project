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
    private boolean joueursPrets = false;
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
                
                // Le serveur n'envoie plus rien
                if (reponse == null) break;
                
                if (reponse.startsWith(Routes.VERIF_PSEUDO)) { // Vérification du pseudo
                    this.setPseudoDispo(reponse);
                } else if (reponse.startsWith(Routes.JOUEURS_PRETS)) { // Vérification des joueurs prêts
                    this.setJoueursPrets(reponse);
                } else if (reponse.startsWith(Routes.COMMENCER_PARTIE)) { // Démarrage de la partie
                    this.commencerPartie(reponse);
                } else if (reponse.startsWith(Routes.ENVOYER_SCORE)) { // Score envoyé par un autre joueur
                    this.scoreRecu(this.escapeRequest(reponse));
                } else if (reponse.startsWith(Routes.VICTOIRE_VERSUS)) { // Victoire du joueur
                    this.victoireVersus(Integer.parseInt(this.escapeRequest(reponse)));
                } else if (reponse.startsWith(Routes.VICTOIRE_VERSUS_AUTRE)) { // Victoire d'un autre joueur
                    this.victoireVersusAutre(this.escapeRequest(reponse));
                } else if (reponse.startsWith(Routes.DEFAITE_VERSUS)) { // Défaite du joueur
                    this.defaiteVersus(Integer.parseInt(this.escapeRequest(reponse)));
                } else if (reponse.startsWith(Routes.DEFAITE_VERSUS_AUTRE)) { // Défaite d'un autre joueur
                    this.defaiteVersusAutre(this.escapeRequest(reponse));
                }
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
        if (this.pseudoDispo(p)) {
            this.pseudo = p;
            this.out.println(Routes.ENREG_PSEUDO + " " + p);
            return true;
        }
        return false;
    }
    
    /**
     * Définit si les joueurs sont tous prêts à commencer la partie
     * 
     * @param message Réponse du serveur 
     */
    private synchronized void setJoueursPrets(String message) {
        this.joueursPrets = message.equals(Routes.JOUEURS_PRETS + " true");
        this.notify();
    }
    
    /**
     * Vérifie que les joueurs sont tous prêts
     * 
     * @return true s'ils sont tous prêts, faux sinon 
     */
    private synchronized boolean joueursPrets() {
        this.out.println(Routes.JOUEURS_PRETS);
        
        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return this.joueursPrets;
    }
    
    /**
     * Envoie la commande de lancement de partie au serveur
     * 
     * @return true si la partie commence, false sinon
     */
    public boolean lancementPartie() {
        if (this.client.estServeur()) {
            if (this.joueursPrets()) {
                this.out.println(Routes.COMMENCER_PARTIE);
                return true;
            }
        } else {
            this.out.println(Routes.PRET_A_COMMENCER);
            return true;
        }
        return false;
    }
    
    /**
     * Démarre la partie multijoueurs
     * 
     * @param message Message du serveur
     */
    private void commencerPartie(String message) {
        this.client.getJeu().setCompetitif(message.equals(Routes.COMMENCER_PARTIE + " true"));
        new Thread(this.client.getJeu()).start();
    }
    
    /**
     * Envoie le score au serveur
     * 
     * @param score Score du joueur
     * @param valeurMax Valeur maximale du jeu
     */
    public void envoyerScore(int score, int valeurMax) {
        this.out.println(Routes.ENVOYER_SCORE + " " + score + "|" + valeurMax);
    }
    
    /**
     * Récupère et affiche le score envoyé par un autre joueur
     * 
     * @param data Données envoyées par le serveur  (joueur#score|valeurMax)
     */
    private void scoreRecu(String data) {
        int splitter = data.indexOf("#");
        int splitter2 = data.indexOf("|");
        
        String joueur = data.substring(0, splitter);
        int score = Integer.parseInt(data.substring(splitter + 1, splitter2));
        int valeurMax = Integer.parseInt(data.substring(splitter2 + 1));
        
        System.out.println("Progression de " + joueur + " :\n - Score: " + score + "\n - Valeur max: " + valeurMax);
    }
    
    /**
     * Envoie la notification de victoire au serveur
     */
    public void envoyerVictoireVersus() {
        this.out.println(Routes.VICTOIRE_VERSUS);
    }
    
    /**
     * Envoie la notification de défaite au serveur
     */
    public void envoyerDefaiteVersus() {
        this.out.println(Routes.DEFAITE_VERSUS);
    }
    
    /**
     * Victoire du joueur
     * 
     * @param classement Classement 
     */
    private void victoireVersus(int classement) {
        System.out.println("Félicitation, vous êtes arrivé " + classement + (classement == 1 ? "er" : "ème"));
    }
    
    /**
     * Victoire d'un autre joueur
     * 
     * @param data Données envoyées par le serveur (joueur#classement) 
     */
    private void victoireVersusAutre(String data) {
        int splitter = data.indexOf("#");
        
        String joueur = data.substring(0, splitter);
        int classement = Integer.parseInt(data.substring(splitter + 1));
        
        System.out.println(joueur + " a terminé la partie et est arrivé " + classement + (classement == 1 ? "er" : "ème"));
    }
    
    /**
     * Défaite du joueur
     * 
     * @param classement Classement 
     */
    private void defaiteVersus(int classement) {
        System.out.println("Dommage, vous êtes arrivé " + classement + (classement == 1 ? "er" : "ème"));
    }
    
    /**
     * Défaite d'un autre joueur
     * 
     * @param data Données envoyées par le serveur (joueur#classement) 
     */
    private void defaiteVersusAutre(String data) {
        int splitter = data.indexOf("#");
        
        String joueur = data.substring(0, splitter);
        int classement = Integer.parseInt(data.substring(splitter + 1));
        
        System.out.println(joueur + " a perdu et est classé " + classement + (classement == 1 ? "er" : "ème"));
    }
    
    /**
     * Getter du pseudo
     * 
     * @return Pseudo du joueur 
     */
    public String getPseudo() {
        return this.pseudo;
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
